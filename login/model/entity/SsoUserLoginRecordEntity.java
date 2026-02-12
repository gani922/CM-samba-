package com.pramaindia.login.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SsoUserLoginRecordEntity {
    private Long id;
    private String sapId;
    private String loginEmail;
    private String companyName;
    private String region;
    private String branch;
    private String district;
    private String clientIp;
    private Date loginTime;
    private Date logoutTime;
    private Date createdTime;
}