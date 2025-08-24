package com.ecomm.notification.Email;

import com.ecomm.notification.Kafka.Order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.UTF8;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(String destinationEmail, BigDecimal amount, String customerName, String orderReference) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("contact@aqibMohd.com");

        final String templateName = EmailTemplates.PAYMENT_CONFIRMATION.getTemplate();
        Map<String,Object> variables =new HashMap <>();
        variables.put("customerName",customerName);
        variables.put("amount",amount);
        variables.put("orderReference", orderReference);

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplates.PAYMENT_CONFIRMATION.getSubject());

        try{
            String htmlTemplate = templateEngine.process(templateName,context);
            messageHelper.setText(htmlTemplate,true);
            messageHelper.setTo(destinationEmail);
            mailSender.send(mimeMessage);
            log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e){
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }
    }

    public void sendOrderConfirmationEmail(String destinationEmail, BigDecimal amount, String customerName, List<Product> products, String orderReference) throws MessagingException {



        //setup mail sender
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,UTF_8.name());
        messageHelper.setFrom("contact@aqibMohd.com");
        messageHelper.setTo(destinationEmail);
        messageHelper.setSubject(EmailTemplates.ORDER_CONFIRMATION.getSubject());

        //setup varibales for template and mailSender
        Map<String,Object> variables = new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("amount",amount);
        variables.put("orderReference",orderReference);

        //setup thymeleaf
        String templateName = EmailTemplates.ORDER_CONFIRMATION.getTemplate();
        Context context = new Context();
        String htmlTemplate = templateEngine.process(templateName,context);
        try{
           messageHelper.setText(htmlTemplate,true);
           mailSender.send(mimeMessage);
           log.info(String.format("INFO- Email successfully send to %s with template %s",destinationEmail,templateName));
        }catch (MessagingException e){
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }


    }
}
