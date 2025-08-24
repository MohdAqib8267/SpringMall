package com.ecomm.product_service.controller;

import com.ecomm.product_service.config.JsonNodeUtils;
import com.ecomm.product_service.dtos.ProductPurchaseRequest;
import com.ecomm.product_service.dtos.ProductPurchaseResponse;
import com.ecomm.product_service.dtos.ProductRequest;
import com.ecomm.product_service.dtos.ProductResponse;
import com.ecomm.product_service.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Integer> createProduct(@RequestBody @Valid ProductRequest request,
                                                 @RequestHeader(name = "Authorization", required = false) String authorizationHeader){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request,authorizationHeader));
    }

    //for purchase only authenticated users can purchase item, if not login, redirect to login
    @PostMapping("/purchase/{user-id}/{order-id}")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody List<ProductPurchaseRequest> request,
            @PathVariable("user-id") Integer userId,
            @PathVariable("order-id") UUID orderId
            ){
        return ResponseEntity.ok(productService.purchaseProduct(request,userId,orderId));
    }
    //public urls
    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable("product-id") Integer productId
    ) {
        return ResponseEntity.ok(productService.findById(productId));
    }

    @Retry(name="findCustomer", fallbackMethod = "findAllProductsFallback")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        System.out.println("Fetching All products from DB");
        return ResponseEntity.ok(productService.findAll());
    }

    public ResponseEntity<List<ProductResponse>> findAllProductsFallback(Throwable throwable) throws IOException {
        //Here we can pass products from some cache products if after retries not able to find
        // for now just creating some products for failure.
        System.out.println("Fetching All products from Cache");
        List<ProductResponse> cachedResponse = JsonNodeUtils.readJsonFileAsList("FallbackJson/ProductResponse.json", new TypeReference<List<ProductResponse>>() {
        });
        return ResponseEntity.ok(cachedResponse);
    }

    @PostMapping("/compansate/{order-id}")
    public void compensateProductsPurchase(@RequestBody List<ProductPurchaseRequest> productsToRestore,
                                           @PathVariable("order-id") UUID orderId){
        productService.compensateProductsPurchase(productsToRestore,orderId);
    }
}
