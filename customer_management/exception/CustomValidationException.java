package com.pramaindia.customer_management.exception;
import lombok.Getter;
import java.util.Map;

@Getter
public class CustomValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public CustomValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }
}