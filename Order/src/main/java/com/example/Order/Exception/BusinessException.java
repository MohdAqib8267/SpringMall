package com.example.Order.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;


public class BusinessException extends RuntimeException{
    public BusinessException(String msg){
        super(msg);
    }
}
