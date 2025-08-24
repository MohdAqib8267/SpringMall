package com.ecomm.customer.Controller;

import com.ecomm.customer.Modal.*;
import com.ecomm.customer.Service.CustomerService;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    @PostMapping("/register")
    public ResponseEntity<CustomerRegisterResponse> createCustomer(@RequestBody @Validated CustomerRegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }
    @PostMapping("/login")
    public ResponseEntity<CustomerLoginResponse> loginCustomer(@RequestBody @Validated CustomerLoginRequest request){
        return ResponseEntity.ok(customerService.verify(request));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findCustomer(@PathVariable("id") Integer id){
        System.out.println("call");
        return ResponseEntity.ok(customerService.findCustomer(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerResponse>> findAllCustomer(){
        System.out.println("call");
        return ResponseEntity.ok(customerService.findAllCustomer());
    }
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        customerService.refreshToken(request,response);
    }
}
