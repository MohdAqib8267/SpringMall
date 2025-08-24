package com.ecomm.payment.Service;

import com.ecomm.payment.Modal.PaymentRequest;
import com.ecomm.payment.Notification.NotificationProducer;
import com.ecomm.payment.Notification.PaymentNotificationRequest;
import com.ecomm.payment.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;
    public UUID createPayment(PaymentRequest request) {
        var payment = paymentRepository.save(mapper.toPayment(request));

//        notificationProducer.sendNotification(
//                new PaymentNotificationRequest(
//                        request.orderReference(),
//                        request.amount(),
//                        request.paymentMethod(),
//                        request.customer().firstname(),
//                        request.customer().lastname(),
//                        request.customer().email()
//                )
//        );
        return payment.getId();
    }
}
