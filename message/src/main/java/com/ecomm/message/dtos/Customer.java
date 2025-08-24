package com.ecomm.message.dtos;

public record Customer(
        String id,
        String firstname,
        String lastname,
        String email
) {

}