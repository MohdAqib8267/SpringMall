package com.ecomm.payment.Handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {
}
