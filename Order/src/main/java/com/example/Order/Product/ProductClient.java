package com.example.Order.Product;

import com.example.Order.Exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Data
@RequiredArgsConstructor
public class ProductClient {

    private String productUrl = "http://localhost:8081/api/v1/products/";
    private final RestTemplate restTemplate;

    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requestBody, Integer customerId, UUID orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE,APPLICATION_JSON_VALUE);

        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(requestBody,headers);

        ParameterizedTypeReference<List<PurchaseResponse>> responseType =
                new ParameterizedTypeReference<>() {};

        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl+"/purchase/{customerId}/{orderId}",
                POST,
                requestEntity,
                responseType,
                customerId,
                orderId
        );
        if(responseEntity.getStatusCode().isError()){
            throw  new BusinessException("An error occurred while processing the products purchase "
                    +responseEntity.getStatusCode());
        }
        return responseEntity.getBody();
    }
}
