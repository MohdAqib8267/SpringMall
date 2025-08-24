package com.ecomm.product_service.controller;

import com.ecomm.product_service.modal.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
//    private CategoryService categoryService;
//    @PostMapping("/add")
//    public Integer addCategory(Category categoryRequest){
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(productService.createProduct(request));
//    }
}
