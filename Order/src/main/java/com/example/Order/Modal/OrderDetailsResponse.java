package com.example.Order.Modal;

import com.example.Order.Customer.CustomerResponse;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrderDetailsResponse(
        CustomerResponse customerResponse,
        UUID orderId,
        List<OrderDetailsItem> orderDetailsItemList
) {


}
