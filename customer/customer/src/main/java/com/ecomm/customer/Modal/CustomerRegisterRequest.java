package com.ecomm.customer.Modal;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Locale;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Setter
@Getter
public class CustomerRegisterRequest{
        @NotNull(message = "firstname can't be null")
        private String firstname;
        @NotNull(message = "lastName can't be null")
        private String lastname;
        @NotNull(message = "email can't be null")
                @Email(message = "Email should be in correct format")
        private String email;

        @NotNull(message = "Password can't be null")
        @Size(min=8, max=15, message="password must contains at least 8 characters")
                @Pattern(
                        regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^A-Za-z0-9]).{8,15}$",
                        message = "Password must contains at least one Captial, one Small, one numeric and One special Character "

                )
        private String password;
        private String role="USER";
//        @NotNull(message = "street can't be null")
//        private String street;
//        @NotNull(message = "houseNumber can't be null")
//        private String houseNumber;
//        @NotNull(message = "zipCode can't be null")
//        @Positive(message = "zipCode should be positive")
//        @Max(value = 6, message = "PinCode should be 6 digit")
//        @Min(value = 6, message = "PinCode should be 6 digit")
//        private String zipCode;

        private Address address;

}
