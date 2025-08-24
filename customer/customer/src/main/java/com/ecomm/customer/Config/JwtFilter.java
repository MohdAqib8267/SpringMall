package com.ecomm.customer.Config;

import com.ecomm.customer.Exceptions.UserNotFoundException;
import com.ecomm.customer.Service.JwtService;
import com.ecomm.customer.Service.MyUserDetailsService;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        String path = request.getServletPath();
        System.out.println(path);
        if(path.equals("/api/v1/customers/register") || path.equals("/api/v1/customers/login")
        || path.equals("/actuator/shutdown")){
            filterChain.doFilter(request,response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
//        System.out.println(authHeader);
        String token=null, username=null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        } 
        else{
            throw new UserNotFoundException("Token is missing");
        }
        UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
        if(username != null && SecurityContextHolder.getContext().getAuthentication()==null){
            if(jwtService.validateToken(token,userDetails)){
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else{
                System.out.println("invalid token");
                throw new SignatureException("JWT signature does not match locally computed signature.");
            }

        }
        filterChain.doFilter(request,response);
    }
}
