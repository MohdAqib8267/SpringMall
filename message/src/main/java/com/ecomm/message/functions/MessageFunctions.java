package com.ecomm.message.functions;

import com.ecomm.message.dtos.AccountMsgDto;
import com.ecomm.message.dtos.OrderConfirmation;
import com.ecomm.message.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@ToString
public class MessageFunctions {
    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    public Function<AccountMsgDto,Integer> email(EmailService emailService) {
        return accountsMsgDto -> {
            log.info("Sending email with the details : {}", accountsMsgDto.toString());
            try {
                emailService.signUpNotification(accountsMsgDto);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return accountsMsgDto.id();
        };
    }
    @Bean
    public Consumer<OrderConfirmation> orderEmail(EmailService emailService){
        return emailService::sendOrderConfirmationEmail;
    }
}
