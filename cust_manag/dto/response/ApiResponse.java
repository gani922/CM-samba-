package com.pramaindia.cust_manag.dto.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private String errorMessage;
    private T data;
    private Boolean success;
    private Boolean failed;
    private Boolean error;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setData(data);
        response.setSuccess(true);
        response.setFailed(false);
        response.setError(false);
        return response;
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(500);
        response.setMessage("Error");
        response.setErrorMessage(errorMessage);
        response.setSuccess(false);
        response.setFailed(true);
        response.setError(true);
        return response;
    }
}