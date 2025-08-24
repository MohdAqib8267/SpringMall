package com.ecomm.customer.Service;

import com.ecomm.customer.Exceptions.UserNotFoundException;
import com.ecomm.customer.Modal.*;
import com.ecomm.customer.Repository.CustomerRespository;
import com.ecomm.customer.Repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {


    private final CustomerRespository customerRespository;
    private final CustomerMapper mapper;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final ApplicationContext context;
    private final MessageProducer messageProducer;



    public CustomerRegisterResponse createCustomer(CustomerRegisterRequest request){
        if(customerRespository.findByEmail(request.getEmail()).isPresent()){
            return new CustomerRegisterResponse(null,null,"User Already exists with email: "+request.getEmail());
        }
        Customer customer = mapper.toCustomer(request);
        var SavedCustomer = customerRespository.save(customer);

        String accessToken = jwtService.generateAccessToken(SavedCustomer.getEmail(),SavedCustomer.getRole());
        String refreshToken = jwtService.refreshAccessToken(SavedCustomer.getEmail(),SavedCustomer.getRole());
        saveCustomerTokens(accessToken,refreshToken,SavedCustomer);
        System.out.println(accessToken);
        CustomerRegisterResponse customerRegisterResponse =new CustomerRegisterResponse(accessToken,refreshToken,"User Registed Successfully");
        messageProducer.send(new AccountMsgDto(SavedCustomer.getId(),SavedCustomer.getFirstname(),SavedCustomer.getLastname(),SavedCustomer.getEmail()));
        return customerRegisterResponse;
    }
    private void saveCustomerTokens(String accessToken,String refreshAccessToken,Customer customer){

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshAccessToken)
                .customer(customer)
                .revoked(false)
                .revoked(false)
        .build();
        tokenRepository.save(token);
    }

    public CustomerLoginResponse verify(CustomerLoginRequest request){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password()));
        Customer customer = customerRespository.findByEmail(request.email()).orElseThrow(()-> new UserNotFoundException("user not found for this email: "+request.email()));
        String username=null, token=null, refreshToken=null;
        if(authentication.isAuthenticated()){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            username=userDetails.getUsername();
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            String role = authorities.stream().map(auth->auth.getAuthority()).collect(Collectors.joining(","));
            System.out.println(role);

            token = jwtService.generateAccessToken(customer.getEmail(),customer.getRole());
            refreshToken = jwtService.refreshAccessToken(customer.getEmail(),customer.getRole());
            revokeAllTokensByUser(customer);
            saveCustomerTokens(token,refreshToken,customer);
        }
        return mapper.toCustomerLoginResponse(customer,token,refreshToken);
    }
    public void revokeAllTokensByUser(Customer customer){
        List<Token> validTokens = tokenRepository.findAllValidTokensByUser(customer.getId());
        if(validTokens.isEmpty()){
            return;
        }
        validTokens.forEach(token->{
            token.setRevoked(true);
            token.setExpired(true);

        });
        tokenRepository.saveAll(validTokens);
    }

    public CustomerResponse findCustomer(int id) {
        var customer = customerRespository.findById(id).orElseThrow(
                ()-> new UserNotFoundException(String.format("customer not exists for %d",id))
        );
        return mapper.toCustomerResponse(customer);
    }

    public List<CustomerResponse> findAllCustomer() {
        return customerRespository.findAll().stream()
                .map(mapper::toCustomerResponse)
                .collect(Collectors.toList());
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String username;

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if (username != null){
            var customer = customerRespository.findByEmail(username).orElseThrow(
                    ()->
                new UserNotFoundException("Invalid token")
            );

            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

            if(jwtService.validateToken(refreshToken,userDetails)){
                var accessToken = jwtService.generateAccessToken(customer.getEmail(), customer.getRole());
                revokeAllTokensByUser(customer);
                saveCustomerTokens(accessToken,refreshToken,customer);
                var authResponse = mapper.toCustomerLoginResponse(customer,accessToken,refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }

        }
        return;
    }

    public boolean updateCommunicationStatus(Integer customerId) {
        boolean isUpdated = false;
        if(null != customerId){
            Customer customer = customerRespository.findById(customerId).orElseThrow(
                    ()-> new RuntimeException("customer not found")
            );
            customer.setCommunicationSw(true);
            customerRespository.save(customer);
        }
        return isUpdated;
    }
}
