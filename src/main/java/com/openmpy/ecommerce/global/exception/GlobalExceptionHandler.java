package com.openmpy.ecommerce.global.exception;

import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(CustomException e) {
        ErrorResponseDto responseDto = new ErrorResponseDto(e.getErrorCode().getCode(), e.getMessage(), null);
        return new ResponseEntity<>(responseDto, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponseDto responseDto =
                new ErrorResponseDto(ErrorCode.VALIDATION_FAIL.getCode(), ErrorCode.VALIDATION_FAIL.getMessage(), errors);
        return new ResponseEntity<>(responseDto, ErrorCode.VALIDATION_FAIL.getHttpStatus());
    }
}
