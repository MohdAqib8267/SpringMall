package com.example.Order.Service;

import com.example.Order.Modal.Order;
import com.example.Order.Modal.OrderDetailsItem;
import com.example.Order.Modal.OrderRequest;
import com.example.Order.Modal.OrderResponse;
import com.example.Order.Product.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderMapper {
    public Order toOrder(OrderRequest request, UUID orderId) {
        if(request==null){
            return null;
        }
        Order newOrder = new Order();
        newOrder.setOrderId(orderId);
        newOrder.setCustomerId(request.customerId());
        newOrder.setReference(request.reference());
        newOrder.setTotalAmount(request.amount());
        newOrder.setPaymentMethod(request.paymentMethod());
        return newOrder;
    }

    public OrderResponse formOrder(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }

    public OrderDetailsItem formOrderDetails(ProductResponse productResponse, double quantity) {
        return OrderDetailsItem.builder()
                .id(productResponse.id())
                .name(productResponse.name())
                .price(productResponse.price())
                .quantity(quantity)
                .description(productResponse.description())
                .categoryName(productResponse.categoryName())
                .categoryDescription(productResponse.categoryDescription())
                .build();
    }
}
