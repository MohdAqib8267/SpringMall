package com.ecomm.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class gatewayServerConfig {

    @Bean
    public RouteLocator ecommRouteConfig(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/ecomm/customer-service/**") // This predicate will now match any request that starts with /ecomm/customer-service/
                        .filters(f->f.rewritePath("/ecomm/customer-service/(?<segment>.*)","/${segment}") // this predefined rewrite filter :The regular expression /ecomm/customer-service/(?<segment>.*) correctly captures everything after /ecomm/customer-service/ into a named group called segment.
                                .circuitBreaker(config->config.setName("customerServiceCircuitBreaker")
                                        .setFallbackUri("forward:/contact-support"))
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://CUSTOMER-SERVICE")) //now this load balancer for customer-service, load the request to downstream customer services
                .route(p -> p
                        .path("/ecomm/product-service/**")
                        .filters(f->f.rewritePath("/ecomm/product-service/(?<segment>.*)","/${segment}")
                                .circuitBreaker(config->config.setName("productServiceCircuitBreaker")
                                        .setFallbackUri("forward:/contact-support"))
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://PRODUCT-SERVICE"))
                .route(p -> p
                        .path("/ecomm/order-service/**")
                        .filters(f->f.rewritePath("/ecomm/order-service/(?<segment>.*)","/${segment}")
                                .circuitBreaker(config->config.setName("orderServiceCircuitBreaker")
                                        .setFallbackUri("forward:/contact-support"))
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://ORDER-SERVICE"))
                .route(p -> p
                        .path("/ecomm/payment-service/**")
                        .filters(f->f.rewritePath("/ecomm/payment-service/(?<segment>.*)","/${segment}")
                                .circuitBreaker(config->config.setName("paymentServiceCircuitBreaker")
                                        .setFallbackUri("forward:/contact-support"))
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                        .uri("lb://PAYMENT-SERVICE"))
                .build();
    }
}
