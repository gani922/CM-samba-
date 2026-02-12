package com.pramaindia.role_management.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

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

    // Private constructor
    private DataResult(Integer code, String message, T data, Boolean success,
                       String errorMessage, String dtoClass) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
        this.failed = !success;
        this.error = !success;
        this.errorMessage = errorMessage;
        this.dtoClass = dtoClass;
        this.expMsg = null;
        this.failData = null;
        this.failMsgType = null;
        this.failMsg = null;
        this.voClass = null;
        this.genericsClass = null;
        this.sysConfig = null;
    }

    public static <T> DataResult<T> SUCCESS() {
        return new DataResult<>(200, "Success", null, true, null, null);
    }

    public static <T> DataResult<T> SUCCESS(T data) {
        return new DataResult<>(200, "Success", data, true, null,
                data != null ? data.getClass().getSimpleName() : null);
    }

    public static <T> DataResult<T> SUCCESS(T data, String message) {
        return new DataResult<>(200, message, data, true, null,
                data != null ? data.getClass().getSimpleName() : null);
    }

    public static <T> DataResult<T> SUCCESS(T data, String message, String dtoClass) {
        return new DataResult<>(200, message, data, true, null, dtoClass);
    }

    public static <T> DataResult<T> FAILED() {
        return new DataResult<>(500, "Failed", null, false, "Internal server error", null);
    }

    public static <T> DataResult<T> FAILED(String errorMessage) {
        return new DataResult<>(500, "Failed", null, false, errorMessage, null);
    }

    public static <T> DataResult<T> FAILED(String errorMessage, String expMsg) {
        DataResult<T> result = new DataResult<>(500, "Failed", null, false, errorMessage, null);
        result.setExpMsg(expMsg);
        return result;
    }

    public static <T> DataResult<T> PARAMEMPTY() {
        return new DataResult<>(400, "Parameter is empty", null, false,
                "Required parameter is missing", null);
    }

    public static <T> DataResult<T> USER_FORBIDDEN() {
        return new DataResult<>(403, "User forbidden", null, false,
                "Access denied", null);
    }

    public static <T> DataResult<T> NOT_FOUND() {
        return new DataResult<>(404, "Not found", null, false,
                "Resource not found", null);
    }

    public static <T> DataResult<T> UNAUTHORIZED() {
        return new DataResult<>(401, "Unauthorized", null, false,
                "Authentication required", null);
    }

    public static <T> DataResult<T> BAD_REQUEST() {
        return new DataResult<>(400, "Bad request", null, false,
                "Invalid request", null);
    }

    public static <T> DataResult<T> BAD_REQUEST(String errorMessage) {
        return new DataResult<>(400, "Bad request", null, false, errorMessage, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExpMsg() {
        return expMsg;
    }

    public void setExpMsg(String expMsg) {
        this.expMsg = expMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getFailData() {
        return failData;
    }

    public void setFailData(Object failData) {
        this.failData = failData;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Boolean getFailed() {
        return failed;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public String getFailMsgType() {
        return failMsgType;
    }

    public void setFailMsgType(String failMsgType) {
        this.failMsgType = failMsgType;
    }

    public Object getSysConfig() {
        return sysConfig;
    }

    public void setSysConfig(Object sysConfig) {
        this.sysConfig = sysConfig;
    }

    public String getGenericsClass() {
        return genericsClass;
    }

    public void setGenericsClass(String genericsClass) {
        this.genericsClass = genericsClass;
    }

    public String getDtoClass() {
        return dtoClass;
    }

    public void setDtoClass(String dtoClass) {
        this.dtoClass = dtoClass;
    }

    public String getVoClass() {
        return voClass;
    }

    public void setVoClass(String voClass) {
        this.voClass = voClass;
    }

    public String getFailMsg() {
        return failMsg;
    }

    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg;
    }
}
