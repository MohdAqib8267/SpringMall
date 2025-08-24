package com.example.Order.Customer;


import com.example.Order.Config.CustomerClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "CUSTOMER-SERVICE",
//        url = "http://localhost:8761/api/v1/customers",
        fallback = CustomerFallback.class,
        configuration = CustomerClientConfig.class

)
public interface CustomerClient {

    @GetMapping("/api/v1/customers/{id}")
    Optional<CustomerResponse> findCustomerById(@PathVariable int id);
}
