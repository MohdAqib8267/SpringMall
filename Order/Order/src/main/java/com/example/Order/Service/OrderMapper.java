package com.example.Order.Service;

import com.example.Order.Modal.Order;
import com.example.Order.Modal.OrderRequest;
import com.example.Order.Modal.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {
    public Order toOrder(OrderRequest request) {
        if(request==null){
            return null;
        }
        Order newOrder = new Order();

        newOrder.setId(request.id());
        newOrder.setCustomerId(request.customerId());
        newOrder.setReference(request.reference());
        newOrder.setTotalAmount(request.amount());
        newOrder.setPaymentMethod(request.paymentMethod());
        return newOrder;
    }

    public OrderResponse formOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }
}
