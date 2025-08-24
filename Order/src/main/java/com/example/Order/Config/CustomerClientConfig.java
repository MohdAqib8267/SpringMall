package com.example.Order.Config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class CustomerClientConfig {

    @Bean
    public RequestInterceptor feignClientInterceptor(){
        return new FeignClientInterceptor();
    }
}
