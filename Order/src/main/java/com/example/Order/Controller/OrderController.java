package com.example.Order.Controller;

import com.example.Order.Customer.CustomerContext;
import com.example.Order.Modal.OrderDetailsResponse;
import com.example.Order.Modal.OrderRequest;
import com.example.Order.Modal.OrderResponse;
import com.example.Order.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;
    @PostMapping
    public ResponseEntity<UUID> createOrder(@RequestBody @Valid OrderRequest request,
                                            @RequestHeader(name = "Authorization") String authorizationHeader){
        try {
            CustomerContext.setJwtToken(authorizationHeader);
            return ResponseEntity.ok(service.createOrder(request));
        } finally{
            CustomerContext.clearJwt(authorizationHeader);
        }

    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{customer-id}/{order-id}")
    public ResponseEntity<OrderDetailsResponse> findProductWithOrderDetails(
            @PathVariable("customer-id") Integer customerId,
            @PathVariable("order-id") UUID orderId,
            @RequestHeader(name="Authorization") String authorization
            ){
        try{
            CustomerContext.setJwtToken(authorization);
            return ResponseEntity.ok(service.findProductWithOrderDetails(orderId,customerId));
        } finally {
            CustomerContext.clearJwt(authorization);
        }

    }

}
