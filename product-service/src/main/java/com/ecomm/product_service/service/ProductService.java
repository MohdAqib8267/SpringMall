package com.ecomm.product_service.service;

import com.ecomm.product_service.customer.CustomerClient;
import com.ecomm.product_service.customer.CustomerResponse;
import com.ecomm.product_service.dtos.ProductPurchaseRequest;
import com.ecomm.product_service.dtos.ProductPurchaseResponse;
import com.ecomm.product_service.dtos.ProductRequest;
import com.ecomm.product_service.dtos.ProductResponse;
import com.ecomm.product_service.exception.BusinessException;
import com.ecomm.product_service.exception.ProductPurchaseException;
import com.ecomm.product_service.modal.Category;
import com.ecomm.product_service.modal.Product;
import com.ecomm.product_service.modal.ShoppingRecord;
import com.ecomm.product_service.productRepository.ProductRepository;
import com.ecomm.product_service.productRepository.ShoppingRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ShoppingRecordRepository shoppingRecordRepository;
    private ProductRepository productRepository;
    private CustomerClient customerClient;
    private ShoppingRecordRepository recordRepository;

    public Integer createProduct(ProductRequest request, String authorizationHeader) {
        CustomerResponse customerResponse = customerClient.findCustomer(request.userId(),authorizationHeader).orElseThrow(()->{
            throw new BusinessException("User not found with the id: "+request.userId());
        });
        if(!customerResponse.roles().equals("ADMIN")){
            throw new BusinessException("You don't have rights to access this resource");
        }
        Product product = buildProduct(request);
        return productRepository.save(product).getId();
    }
    private Product buildProduct(ProductRequest request){
        return Product.builder()
                .userId(request.userId())
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .price(request.price())
                .category(
                        Category.builder()
                                .id(request.categoryId())
                                .build()
                )
                .build();
    }
    @Transactional(rollbackFor = ProductPurchaseException.class)
    public List<ProductPurchaseResponse> purchaseProduct(List<ProductPurchaseRequest> request,Integer userId,UUID orderId) {

//        CustomerResponse customer = customerClient.findCustomer(userId,authorizationHeader).orElseThrow(()->{
//            throw new BusinessException("user not found, please login to proceed.");
//        });

        List<Integer> productIds = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .map(ProductPurchaseRequest::productId)
                .collect(Collectors.toList());
        List<Product> storedProducts = productRepository.findByIdInOrderById(productIds);
        if(productIds.size() != storedProducts.size()){
            throw new ProductPurchaseException("One or more products not available");
        }
        List<ProductPurchaseRequest> SortedRequestProducts = request.stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .collect(Collectors.toList());
        List<ProductPurchaseResponse> productPurchaseResponses = new ArrayList<>();
        for(int i=0;i<storedProducts.size();i++){
            if(storedProducts.get(i).getAvailableQuantity()<SortedRequestProducts.get(i).quantity()){
                throw new ProductPurchaseException("Insufficient stock quantity for product with ID:: " + SortedRequestProducts.get(i).productId());
            }
            var newAvailableQuantity = storedProducts.get(i).getAvailableQuantity()-SortedRequestProducts.get(i).quantity();
            storedProducts.get(i).setAvailableQuantity(newAvailableQuantity);

            productRepository.save(storedProducts.get(i));
            var shopping_record = recordRepository.save(ShoppingRecord.builder()
                    .productId(storedProducts.get(i).getId())
                    .orderId(orderId)
                    .userId(userId)
                    .dateOfPurchase(LocalDateTime.now())
                    .build());
            productPurchaseResponses.add(ProductPurchaseResponse.builder()
                            .productLineId(shopping_record.getOrderLineId())
                            .productId(storedProducts.get(i).getId())
                            .name(storedProducts.get(i).getName())
                            .description(storedProducts.get(i).getDescription())
                            .price(storedProducts.get(i).getPrice())
                            .quantity(SortedRequestProducts.get(i).quantity())
                    .build());
        }
        return productPurchaseResponses;
    }



    public ProductResponse findById(Integer productId) {
        return productRepository.findById(productId)
                .map(product -> buildProductResponse(product))
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID:: " + productId));
    }
    private ProductResponse buildProductResponse(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
    }
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(product -> buildProductResponse(product))
                .collect(Collectors.toList());
    }
    @Transactional
    public void compensateProductsPurchase(List<ProductPurchaseRequest> productsToRestore, UUID orderId){
        for(ProductPurchaseRequest request: productsToRestore){
            Product product = productRepository.findById(request.productId()).orElseThrow(()->{
                throw new ProductPurchaseException("Product not found with ID: "+request.productId());
            });
            product.setAvailableQuantity(product.getAvailableQuantity()+request.quantity());
            productRepository.save(product);
            shoppingRecordRepository.deleteAllByOrderId(orderId);
        }
    }
}
