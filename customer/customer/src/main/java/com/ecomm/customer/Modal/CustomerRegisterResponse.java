package com.ecomm.customer.Modal;

public record CustomerRegisterResponse(
        String access_token,
        String refresh_token,
        String message

) {
}
