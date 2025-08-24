package com.ecomm.message.email;

import lombok.Getter;

public enum EmailTemplates {
    ACCOUNT_CONFIRMATION("account-confirmation.html","Ecomm"),
    ORDER_CONFIRMATION("order-confirmation.html","Ecomm");

    @Getter
    private String subject;
    @Getter
    private String template;
    EmailTemplates(String template,String subject){
        this.template = template;
        this.subject = subject;
    }
}
