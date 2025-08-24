package com.example.Order.Customer;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerFallback implements CustomerClient{
    @Override
    public Optional<CustomerResponse> findCustomerById(int id) {
        return null;
    }
}
