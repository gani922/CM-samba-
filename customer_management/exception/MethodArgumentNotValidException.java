package com.pramaindia.customer_management.exception;

import org.springframework.validation.Errors;

import java.util.Map;

public class MethodArgumentNotValidException extends RuntimeException {
    private final Map<String, String> errors;

    public MethodArgumentNotValidException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public Errors getBindingResult() {
        return getBindingResult();
    }
}