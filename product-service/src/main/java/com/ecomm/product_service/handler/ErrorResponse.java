package com.ecomm.product_service.handler;

import java.util.Map;
import java.util.Objects;

public record ErrorResponse(Map<String, String>errors) {

}
