package com.ecomm.customer.Handler;


import com.ecomm.customer.Exceptions.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentException(MethodArgumentNotValidException ex){
        Map<String,Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            var feildName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(feildName,errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errors));
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex){
        Map<String,Object> errors = new HashMap<>();
       errors.put("message",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(errors));
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(Exception exception) {


        // TODO send this stack trace to an observability tool
        exception.printStackTrace();

        if (exception instanceof BadCredentialsException) {
            Map<String, Object> errorDetail = new HashMap<>();
            errorDetail.put("description", "The username or password is incorrect");
            errorDetail.put("Status Code", HttpStatusCode.valueOf(401));
            errorDetail.put("message",exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(errorDetail));
        }

        if (exception instanceof AccountStatusException) {

            Map<String, Object> errorDetail = new HashMap<>();
            errorDetail.put("description", "The account is locked");
            errorDetail.put("Status Code", HttpStatusCode.valueOf(403));
            errorDetail.put("message",exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(errorDetail));
        }

        if (exception instanceof AccessDeniedException) {


            Map<String, Object> errorDetail = new HashMap<>();
            errorDetail.put("description", "You are not authorized to access this resource");
            errorDetail.put("Status Code", HttpStatusCode.valueOf(403));
            errorDetail.put("message",exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(errorDetail));
        }

        if (exception instanceof SignatureException) {

            Map<String, Object> errorDetail = new HashMap<>();
            errorDetail.put("description", "The JWT signature is invalid");
            errorDetail.put("Status Code", HttpStatusCode.valueOf(403));
            errorDetail.put("message",exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(errorDetail));
        }

        if (exception instanceof ExpiredJwtException) {


            Map<String, Object> errorDetail = new HashMap<>();
            errorDetail.put("description", "The JWT token has expired");
            errorDetail.put("Status Code", HttpStatusCode.valueOf(403));
            errorDetail.put("message",exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(errorDetail));
        }
        if(exception instanceof InternalAuthenticationServiceException){
            Map<String, Object> errorDetail = new HashMap<>();
            errorDetail.put("description", "invalid credentials");
            errorDetail.put("Status Code", HttpStatusCode.valueOf(400));
            errorDetail.put("message",exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(errorDetail));
        }
        if(exception instanceof UserNotFoundException){
            Map<String,Object> errors = new HashMap<>();
            errors.put("message",exception.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(errors));
        }
        Map<String, Object> errorDetail = new HashMap<>();
        errorDetail.put("message","Something went wrong");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(errorDetail));
    }
}
