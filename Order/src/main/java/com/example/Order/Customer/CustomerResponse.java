package com.example.Order.Customer;

public record CustomerResponse(
        int id,
        String firstName,
        String lastName,
        String email
) {
}
