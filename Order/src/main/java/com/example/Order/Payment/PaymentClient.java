package com.example.Order.Payment;

import com.example.Order.Customer.CustomerFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "PAYMENT-SERVICE"
//        fallback = PaymentFallback.class
)
public interface PaymentClient {
    @PostMapping("/api/v1/payments")
    UUID requestOrderPayment(@RequestBody PaymentRequest request);
}
