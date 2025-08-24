package com.ecomm.customer.Modal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerLoginRequest(
        @NotNull(message = "email can't be null")
                @Email(
                        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                        message = "email should be in stander format ex:abc@domain.com"
                )
        String email,

        @Size(min=8, max=15, message="password must contains at least 8 characters")
        @Pattern(
                regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^A-Za-z0-9]).{8,15}$",
                message = "Password must contains at least one Captial, one Small, one numeric and One special Character "

        )
        String password
) {
}
