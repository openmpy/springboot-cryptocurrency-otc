package com.openmpy.ecommerce.global.exception;

import java.util.Map;

public record ErrorResponseDto(
        String code,
        String message,
        Map<String, String> errors
) {
}
