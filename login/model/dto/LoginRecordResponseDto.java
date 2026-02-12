package com.pramaindia.login.model.dto;

import lombok.Data;

@Data
public class LoginRecordResponseDto {
    private Integer code = 200;
    private String message = "Success";
    private Object data;
    private Boolean success = true;
}