package com.example.Order.Config;

import com.example.Order.Customer.CustomerContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;


public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate){
        String jwtToken = CustomerContext.getJwtToken(); //Retrieve from ThreadLocal (for a specific thread)
        if(jwtToken != null){
            requestTemplate.header(AUTHORIZATION_HEADER, jwtToken);
        }
    }
}
