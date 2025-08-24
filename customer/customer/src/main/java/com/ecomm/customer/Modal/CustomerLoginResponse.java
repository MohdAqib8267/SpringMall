package com.ecomm.customer.Modal;

public record CustomerLoginResponse(
        Integer id,
        String firstname,
        String lastname,
        String email,
        Address address,

        String access_token,
        String refresh_token
) {
}
