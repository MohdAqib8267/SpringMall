package com.ecomm.customer.Service;

import com.ecomm.customer.Exceptions.UserNotFoundException;
import com.ecomm.customer.Modal.Customer;
import com.ecomm.customer.Modal.UserPrincipal;
import com.ecomm.customer.Repository.CustomerRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRespository customerRespository;
    @Override
    public UserDetails loadUserByUsername(String username){
        Customer customer = customerRespository.findByEmail(username).orElseThrow(()->
                new UserNotFoundException(String.format("Customer not exists for email: %s",username)));
        return new UserPrincipal(customer);
    }
}
