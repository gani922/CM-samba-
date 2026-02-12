package com.pramaindia.login.model.dto;

import lombok.Data;

@Data
public class LogoutRequestDto {
    private String sessionId;
    private Long logoutTime;
}