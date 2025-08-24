package com.example.Order.Service;

import com.example.Order.Customer.CustomerClient;
import com.example.Order.Exception.BusinessException;
import com.example.Order.Kafka.OrderConfirmation;
import com.example.Order.Kafka.OrderProducer;
import com.example.Order.Modal.OrderRequest;
import com.example.Order.Modal.OrderResponse;
import com.example.Order.OrderLine.OrderLine;
import com.example.Order.OrderLine.OrderLineRequest;
import com.example.Order.OrderLine.OrderLineService;
import com.example.Order.Payment.PaymentClient;
import com.example.Order.Payment.PaymentRequest;
import com.example.Order.Product.ProductClient;
import com.example.Order.Product.PurchaseRequest;
import com.example.Order.Repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repo;
    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private OrderMapper mapper;
    @Autowired
    private OrderLineService orderLineService;
    @Autowired
    private OrderProducer orderProducer;
    @Autowired
    private PaymentClient paymentClient;

    @Transactional
    public Integer createOrder(OrderRequest request){
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(()-> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        var purchasedProducts = productClient.purchaseProducts(request.products());

        var order = repo.save(mapper.toOrder(request));

        for(PurchaseRequest purchaseRequest:request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity())
            );

        }
        //start payement process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

//         Kafka setup for sending order confirmation to kafka broker
        orderProducer.sendOrderConfirmation(new OrderConfirmation(
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                customer,
                purchasedProducts
        ));

        return null;
    }


    public List<OrderResponse> findAll() {
        return repo.findAll()
                .stream()
                .map(mapper:: formOrder)
                .collect(Collectors.toList());
    }
}
