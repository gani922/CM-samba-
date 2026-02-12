package com.pramaindia.customer_management.exception;

import com.pramaindia.customer_management.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Response<?>> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication failed: {}", ex.getMessage());

        Response<?> response = Response.<Object>builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Authentication Failed")
                .errorMessage(ex.getMessage())
                .expMsg(null)
                .data(null)
                .success(false)
                .failed(true)
                .error(true)
                .failData(null)
                .failMsgType(null)
                .failMsg(null)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());

        Response<?> response = Response.<Object>builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Resource Not Found")
                .errorMessage(ex.getMessage())
                .expMsg(null)
                .data(null)
                .success(false)
                .failed(true)
                .error(false)
                .failData(null)
                .failMsgType(null)
                .failMsg(null)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<Response<?>> handleCustomValidationException(CustomValidationException ex) {
        log.error("Validation failed: {}", ex.getErrors());

        Response<?> response = Response.<Object>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation Failed")
                .errorMessage("Input validation failed")
                .expMsg(null)
                .data(null)
                .success(false)
                .failed(true)
                .error(false)
                .failData(ex.getErrors())
                .failMsgType("VALIDATION_ERROR")
                .failMsg("Validation errors occurred")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Request validation failed: {}", errors);

        Response<?> response = Response.<Object>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation Failed")
                .errorMessage("Request validation failed")
                .expMsg(null)
                .data(null)
                .success(false)
                .failed(true)
                .error(false)
                .failData(errors)
                .failMsgType("REQUEST_VALIDATION_ERROR")
                .failMsg("Request parameter validation failed")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Response<?>> handleGenericException(Exception ex) {
//        log.error("Unexpected error occurred: ", ex);
//
//        Response<?> response = Response.<Object>builder()
//                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .message("Internal Server Error")
//                .errorMessage("An unexpected error occurred")
//                .expMsg(ex.getMessage())
//                .data(null)
//                .success(false)
//                .failed(true)
//                .error(true)
//                .failData(null)
//                .failMsgType(null)
//                .failMsg(null)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
}