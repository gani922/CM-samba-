package com.pramaindia.customer_management.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private Integer code;
    private String message;
    private String errorMessage;
    private String expMsg;
    private T data;
    private Boolean success;
    private Boolean failed;
    private Boolean error;
    private Object failData;
    private String failMsgType;
    private String failMsg;
    private String voClass;
    private String dtoClass;
    private String genericsClass;
    private Object sysConfig;

    // Success responses
    public static <T> Response<T> success(T data, String message) {
        return Response.<T>builder()
                .code(200)
                .message(message)
                .errorMessage(null)
                .expMsg(null)
                .data(data)
                .success(true)
                .failed(false)
                .error(false)
                .failData(null)
                .failMsgType(null)
                .failMsg(null)
                .voClass(null)
                .dtoClass(null)
                .genericsClass(null)
                .sysConfig(null)
                .build();
    }

    public static <T> Response<T> success(T data) {
        return success(data, "Success");
    }

    public static <T> Response<T> success(String message) {
        return success(null, message);
    }

    public static <T> Response<T> success() {
        return success(null, "Success");
    }

    // Error responses
    public static <T> Response<T> error(Integer code, String message, String errorMessage) {
        return Response.<T>builder()
                .code(code)
                .message(message)
                .errorMessage(errorMessage)
                .expMsg(null)
                .data(null)
                .success(false)
                .failed(true)
                .error(true)
                .failData(null)
                .failMsgType(null)
                .failMsg(null)
                .voClass(null)
                .dtoClass(null)
                .genericsClass(null)
                .sysConfig(null)
                .build();
    }

    public static <T> Response<T> error(Integer code, String errorMessage) {
        return error(code, "Error", errorMessage);
    }

    // Failed responses (non-error failures)
    public static <T> Response<T> failed(Integer code, String message, String failMsg, Object failData) {
        return Response.<T>builder()
                .code(code)
                .message(message)
                .errorMessage(null)
                .expMsg(null)
                .data(null)
                .success(false)
                .failed(true)
                .error(false)
                .failData(failData)
                .failMsgType(null)
                .failMsg(failMsg)
                .voClass(null)
                .dtoClass(null)
                .genericsClass(null)
                .sysConfig(null)
                .build();
    }

    public static <T> Response<T> failed(Integer code, String failMsg, Object failData) {
        return failed(code, "Failed", failMsg, failData);
    }

    // Authentication failure
    public static <T> Response<T> authFailed(String errorMessage) {
        return Response.<T>builder()
                .code(401)
                .message("Authentication Failed")
                .errorMessage(errorMessage)
                .expMsg(null)
                .data(null)
                .success(false)
                .failed(true)
                .error(true)
                .failData(null)
                .failMsgType(null)
                .failMsg(null)
                .voClass(null)
                .dtoClass(null)
                .genericsClass(null)
                .sysConfig(null)
                .build();
    }

    // Validation failure
    public static <T> Response<T> validationFailed(Object failData) {
        return Response.<T>builder()
                .code(400)
                .message("Validation Failed")
                .errorMessage("Input validation failed")
                .expMsg(null)
                .data(null)
                .success(false)
                .failed(true)
                .error(false)
                .failData(failData)
                .failMsgType("VALIDATION_ERROR")
                .failMsg("Validation errors occurred")
                .voClass(null)
                .dtoClass(null)
                .genericsClass(null)
                .sysConfig(null)
                .build();
    }

    // Resource not found
    public static <T> Response<T> notFound(String message) {
        return Response.<T>builder()
                .code(404)
                .message("Not Found")
                .errorMessage(message)
                .expMsg(null)
                .data(null)
                .success(false)
                .failed(true)
                .error(false)
                .failData(null)
                .failMsgType(null)
                .failMsg(null)
                .voClass(null)
                .dtoClass(null)
                .genericsClass(null)
                .sysConfig(null)
                .build();
    }

    // Internal server error
    public static <T> Response<T> serverError(String expMsg) {
        return Response.<T>builder()
                .code(500)
                .message("Internal Server Error")
                .errorMessage("An unexpected error occurred")
                .expMsg(expMsg)
                .data(null)
                .success(false)
                .failed(true)
                .error(true)
                .failData(null)
                .failMsgType(null)
                .failMsg(null)
                .voClass(null)
                .dtoClass(null)
                .genericsClass(null)
                .sysConfig(null)
                .build();
    }

    // Custom response with all fields
    public static <T> Response<T> custom(Integer code, String message, String errorMessage,
                                         T data, Boolean success, Boolean failed,
                                         Boolean error, Object failData, String failMsgType,
                                         String failMsg, String voClass, String dtoClass,
                                         String genericsClass, Object sysConfig) {
        return Response.<T>builder()
                .code(code)
                .message(message)
                .errorMessage(errorMessage)
                .expMsg(null)
                .data(data)
                .success(success)
                .failed(failed)
                .error(error)
                .failData(failData)
                .failMsgType(failMsgType)
                .failMsg(failMsg)
                .voClass(voClass)
                .dtoClass(dtoClass)
                .genericsClass(genericsClass)
                .sysConfig(sysConfig)
                .build();
    }
}