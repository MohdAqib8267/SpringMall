package com.example.Order.Service;

import com.example.Order.Customer.CustomerClient;
import com.example.Order.Customer.CustomerResponse;
import com.example.Order.Exception.BusinessException;
import com.example.Order.producer.OrderConfirmation;
import com.example.Order.Kafka.OrderProducer;
import com.example.Order.Modal.OrderDetailsItem;
import com.example.Order.Modal.OrderDetailsResponse;
import com.example.Order.Modal.OrderRequest;
import com.example.Order.Modal.OrderResponse;
import com.example.Order.OrderLine.OrderLineRepository;
import com.example.Order.OrderLine.OrderLineRequest;
import com.example.Order.OrderLine.OrderLineResponse;
import com.example.Order.OrderLine.OrderLineService;
import com.example.Order.Payment.PaymentClient;
import com.example.Order.Payment.PaymentRequest;
import com.example.Order.Product.ProductClient;
import com.example.Order.Product.ProductResponse;
import com.example.Order.Product.PurchaseResponse;
import com.example.Order.Repository.OrderRepository;

import com.example.Order.producer.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository repo;
    @Autowired
    private OrderLineRepository orderLineRepository;
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
    @Autowired
    private com.example.Order.Product.productClientFeign productClientFeign;
    @Autowired
    private MessageProducer messageProducer;

//    If customer retrieval fails → no compensation needed.
//    If product purchase fails → no compensation needed.
//    If order saving fails → compensate product purchase.
//    If order lines saving fails → compensate product purchase and order.
//    If payment fails → compensate all previously done (order lines, order, product purchase).

    @Transactional(rollbackFor = Exception.class) // now spring will roll back for both checked and unchecked exception for entitys that exists in same DB, so maybe we don't need to handle compansation for order and orderLines
    public UUID createOrder(OrderRequest request){
        UUID orderId = UUID.randomUUID();
        boolean productsPurchased = false;
        boolean orderSaved = false;
        boolean orderLinesSaved = false;
        boolean paymentProcessed = false;
        try {

            var customer = customerClient.findCustomerById(request.customerId())
                    .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

            var purchasedProducts = productClient.purchaseProducts(request.products(), customer.id(),orderId);
            productsPurchased = true;
            var order = repo.save(mapper.toOrder(request,orderId));
            orderSaved = true;
            for (PurchaseResponse purchaseResponse : purchasedProducts) {
                orderLineService.saveOrderLine(
                        new OrderLineRequest(
                                purchaseResponse.productLineId(),
                                order.getOrderId(),
                                purchaseResponse.productId(),
                                purchaseResponse.quantity()
                        )
                );
            }
            orderLinesSaved = true;
            //start payement process
            var paymentRequest = new PaymentRequest(
                    request.amount(),
                    request.paymentMethod(),
                    order.getOrderId(),
                    order.getReference(),
                    customer
            );
            UUID paymentId = paymentClient.requestOrderPayment(paymentRequest);
            paymentProcessed = true;

            //sending order confirmation to rabbitmq broker
            messageProducer.send(new OrderConfirmation(
                    order.getReference(),
                    order.getTotalAmount(),
                    order.getPaymentMethod(),
                    customer,
                    purchasedProducts
            ));

            return order.getOrderId();
        } catch (Exception e){
            // Orchestrator triggers compensations in reverse order
            compensateOrder(
                    productsPurchased,
                    orderSaved,
                    orderLinesSaved,
                    paymentProcessed,
                    request,
                    orderId
            );
//
            throw new BusinessException("Order creation failed and rolling back due to: "+e.getMessage());
        }
    }
    private void compensateOrder(boolean productsPurchased,
                                boolean orderSaved,
                                boolean orderLinesSaved,
                                boolean paymentProcessed,
                                OrderRequest request,
                                UUID orderId
    ) {
        try {
            if (paymentProcessed) {
                // Optionally trigger compensation for payment (if supported)
//                paymentClient.compensatePayment(orderId);
            }
        } catch (Exception e) {
            log.error("Payment compensation failed", e);
        }

        try {
            if (orderLinesSaved) {
                // Add compensation for order lines if implemented
                orderLineService.compensateOrderLines(orderId);
            }
        } catch (Exception e) {
            log.error("Order lines compensation failed", e);
        }

        try {
//            if (orderSaved && order != null) {
//                repo.delete(order);
//            }
            if(orderSaved){
                repo.deleteByOrderId(orderId);
            }
        } catch (Exception e) {
            log.error("Order compensation failed", e);
        }

        try {
            if (productsPurchased) {
                productClientFeign.compensateProductsPurchase(request.products(), orderId);
            }
        } catch (Exception e) {
            log.error("Product purchase compensation failed", e);
        }


    }

    public List<OrderResponse> findAll() {
        return repo.findAll()
                .stream()
                .map(mapper:: formOrder)
                .collect(Collectors.toList());
    }

    public OrderDetailsResponse findProductWithOrderDetails(UUID orderId,Integer customerId) {
        try{
            var customer = customerClient.findCustomerById(customerId)
                    .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

                List<OrderLineResponse> orderLineResponses = orderLineService.findAllByOrderId(orderId);

            if (orderLineResponses.isEmpty()) {
                throw new BusinessException("No order lines found for orderId: " + orderId);
            }
            List<OrderDetailsItem> orderListItems = orderLineResponses.stream()
                        .map(orderLine -> {
                            ProductResponse productResponse = productClientFeign.findById(orderLine.productId());
                            if(productResponse!=null){
                                return mapper.formOrderDetails(productResponse,orderLine.quantity());
                            }else {
                                return null;
                            }
                        })
                    .collect(Collectors.toList());
            return buildOrderDetailsResponse(customer,orderId,orderListItems);

        } catch (Exception ex){
            throw new BusinessException("Failed to retrieve order details due to an internal error: "+ex);
        }
    }

    private OrderDetailsResponse buildOrderDetailsResponse(CustomerResponse customer, UUID orderId, List<OrderDetailsItem> orderListItems) {
        return OrderDetailsResponse.builder()
                .customerResponse(customer)
                .orderId(orderId)
                .orderDetailsItemList(orderListItems)
                .build();
    }
}
