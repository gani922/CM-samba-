package com.pramaindia.customer_management.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String resourceName, Integer id) {
        super(String.format("%s with ID %d not found", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String id) {
        super(String.format("%s with ID '%s' not found", resourceName, id));
    }
}