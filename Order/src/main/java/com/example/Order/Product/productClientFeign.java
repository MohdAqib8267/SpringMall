package com.example.Order.Product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "PRODUCT-SERVICE",
        fallback = ProductFallback.class
)
public interface productClientFeign {
    @PostMapping("/api/v1/products/compansate/{order-id}")
    public void compensateProductsPurchase(@RequestBody List<PurchaseRequest> productsToRestore,
                                           @PathVariable("order-id") UUID orderId);
    @GetMapping("/api/v1/products/{product-id}")
    public ProductResponse findById(@PathVariable("product-id") Integer productId);
}
