package com.ecomm.message.dtos;

public record AccountMsgDto(
        Integer id,
        String firstname,
        String lastname,
        String email
) {
}
