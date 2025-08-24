package com.ecomm.customer.Service;

import com.ecomm.customer.Modal.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    BCryptPasswordEncoder paaswordEncoder = new BCryptPasswordEncoder();
    public Customer toCustomer(CustomerRegisterRequest request) {
//        return new Customer(
//                request.getFirstname(),
//                request.getLastname(),
//                request.getEmail(),
//                paaswordEncoder.encode(request.getPassword()),
//                request.getRole(),
//                request.getAddress()
//        );
        Customer newCustomer = new Customer();
        newCustomer.setFirstname(request.getFirstname());
        newCustomer.setLastname(request.getLastname());
        newCustomer.setEmail(request.getEmail());
        newCustomer.setPassword(paaswordEncoder.encode(request.getPassword()));
        newCustomer.setRole(request.getRole());
        newCustomer.setAddress(request.getAddress());
        return newCustomer;

    }

    public CustomerRegisterResponse toCustomerResponse(Customer savedCustomer,String access_token,String refresh_token) {
        return new CustomerRegisterResponse(
                access_token,
                refresh_token,
                "Customer register successfully."
        );
    }
    public CustomerLoginResponse toCustomerLoginResponse(Customer customer,String access_toke,String refresh_token){
        return new CustomerLoginResponse(
            customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress(),
                access_toke,
                refresh_token

        );
    }
    public CustomerResponse toCustomerResponse(Customer customer){
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getRole(),
                customer.getAddress()

        );
    }
}
