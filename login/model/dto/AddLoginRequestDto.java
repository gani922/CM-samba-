package com.pramaindia.login.model.dto;

import lombok.Data;

@Data
public class AddLoginRequestDto extends LoginRecordRequestDto {
    private String sapId;
    private String loginEmail;
    private String companyName;
    private String region;
    private String branch;
    private String district;
    private String clientIp;
    private Long loginTime;
    private String sessionId;
    private String userAgent;
}