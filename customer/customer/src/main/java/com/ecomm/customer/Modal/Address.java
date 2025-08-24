package com.ecomm.customer.Modal;

import jakarta.persistence.Embeddable;
import lombok.*;

//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Embeddable
public class Address {
    private String street;
    private String houseNumber;
    private String zipCode;
}
