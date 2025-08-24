package com.ecomm.customer.Modal;

public record CustomerResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        String roles,
        Address address

) {
}
