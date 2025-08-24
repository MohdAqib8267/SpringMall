package com.ecomm.customer.functions;

import com.ecomm.customer.Service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AccountFunctions {
    private static final Logger log = LoggerFactory.getLogger(AccountFunctions.class);

    @Bean
    public Consumer<Integer> updateCommunication(CustomerService customerService){
        return customerId -> {
            log.info("Updating Communication status for customer ID: {}",customerId);
            customerService.updateCommunicationStatus(customerId);
        };
    }
}
