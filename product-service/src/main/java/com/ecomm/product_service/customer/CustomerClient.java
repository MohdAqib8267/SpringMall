package com.ecomm.product_service.customer;

import com.ecomm.product_service.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerClient {

    @Value("${application.baseurl.customer}")
    private String customerUrl;

    private final RestTemplate restTemplate;


    public Optional<CustomerResponse> findCustomer(Integer id,String jwtToken){

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        if (jwtToken != null && !jwtToken.isEmpty()) {
            headers.set(HttpHeaders.AUTHORIZATION, jwtToken);
        }

        HttpEntity<Integer> requestEntity = new HttpEntity<>(id,headers);
        ParameterizedTypeReference<CustomerResponse> responseType =
                new ParameterizedTypeReference<>() {
        };
        ResponseEntity<CustomerResponse> responseEntity = restTemplate.exchange(
                customerUrl+"/{id}",
                HttpMethod.GET,
                requestEntity,
                responseType,
                id
        );
        if(responseEntity.getStatusCode().isError()){
            throw new BusinessException("An error occurred while processing the products purchase: " + responseEntity.getStatusCode());
        }
        return Optional.ofNullable(responseEntity.getBody());
    }
}
