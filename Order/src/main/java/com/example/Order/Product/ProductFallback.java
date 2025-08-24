package com.example.Order.Product;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductFallback implements productClientFeign{
    @Override
    public void compensateProductsPurchase(List<PurchaseRequest> productsToRestore, UUID orderId) {
        return;
    }

    @Override
    public ProductResponse findById(Integer productId) {
        return null;
    }
}
