package com.example.Order.producer;

import com.example.Order.Customer.CustomerResponse;
import com.example.Order.Modal.PaymentMethod;
import com.example.Order.Product.PurchaseResponse;


import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {

}
