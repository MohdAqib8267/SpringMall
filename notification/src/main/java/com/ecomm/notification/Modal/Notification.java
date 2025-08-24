package com.ecomm.notification.Modal;


import com.ecomm.notification.Kafka.Order.OrderConfirmation;
import com.ecomm.notification.Kafka.Payment.PaymentConfirmation;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Notification {
    @Id
    Integer id;
    private NotificationType notificationType;
    private LocalDateTime notificationDate;
    private OrderConfirmation orderConfirmation;
    private PaymentConfirmation paymentConfirmation;
}
