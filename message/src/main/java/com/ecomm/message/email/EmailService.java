package com.ecomm.message.email;

import com.ecomm.message.dtos.AccountMsgDto;
import com.ecomm.message.dtos.OrderConfirmation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void signUpNotification(AccountMsgDto accountMsgDto) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,UTF_8.name());
        messageHelper.setFrom("contact@ecomm.com");

        String templateName = EmailTemplates.ACCOUNT_CONFIRMATION.getTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("firstName",accountMsgDto.firstname());

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplates.ACCOUNT_CONFIRMATION.getSubject());
        try {
            String htmlTemplate = templateEngine.process(templateName,context);
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setTo(accountMsgDto.email());
            javaMailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", accountMsgDto.email(), templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", accountMsgDto.email());
        }

    }

    public void sendOrderConfirmationEmail(OrderConfirmation orderConfirmation) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,UTF_8.name());
        messageHelper.setFrom("contact@ecomm.com");

        String templateName = EmailTemplates.ORDER_CONFIRMATION.getTemplate();
        Map<String, Object> variables = new HashMap<>();
        variables.put("firstName",orderConfirmation.customer().firstname());
        variables.put("totalAmount", orderConfirmation.totalAmount());
        variables.put("orderReference", orderConfirmation.orderReference());
        variables.put("products", orderConfirmation.products());

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplates.ORDER_CONFIRMATION.getSubject());
        try {
            String htmlTemplate = templateEngine.process(templateName,context);
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setTo(orderConfirmation.customer().email());
            javaMailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", orderConfirmation.customer().email(), templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", orderConfirmation.customer().email());
        }
    }
}
