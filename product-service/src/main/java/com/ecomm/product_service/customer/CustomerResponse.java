package com.ecomm.product_service.customer;

public record CustomerResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        String roles,
        Address address
) {
}
