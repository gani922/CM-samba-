package com.pramaindia.login.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LoginRecordEntity {
    private Long id;
    private String sapId;
    private String loginEmail;
    private String companyName;
    private String region;
    private String branch;
    private String district;
    private Date loginTime;
    private String clientIp;
    private Date logoutTime;
    private String sessionId;
    private Integer status;
    private Date createdTime;
}
