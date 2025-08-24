package com.ecomm.customer.Handler;


import java.util.Map;


public record ErrorResponse(
        Map<String, Object> errors
) {

}
