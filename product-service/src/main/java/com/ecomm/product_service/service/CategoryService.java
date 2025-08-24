//package com.ecomm.product_service.service;
//
//import com.ecomm.product_service.customer.CustomerClient;
//import com.ecomm.product_service.customer.CustomerResponse;
//import com.ecomm.product_service.exception.BusinessException;
//import com.ecomm.product_service.modal.Category;
//import com.ecomm.product_service.modal.Product;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class CategoryService {
//
////    private CustomerClient customerClient;
////
////    public Integer addCategory(Category request){
////        CustomerResponse customerResponse = customerClient.findCustomer(request.userId()).orElseThrow(()->{
////            throw new BusinessException("User not found with the id: "+request.userId());
////        });
////        if(customerResponse.roles() != "ROLE_ADMIN"){
////            throw new BusinessException("You don't have rights to access this resource");
////        }
////        return null;
//    }
//}
