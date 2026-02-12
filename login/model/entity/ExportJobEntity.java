package com.pramaindia.login.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ExportJobEntity {
    private String id;
    private String userEmail;
    private String searchCriteria;
    private String filePath;
    private String fileName;
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED, EXPIRED
    private String errorMessage;
    private Date createdTime;
    private Date completedTime;
    private Date expiryTime;
    private Integer downloadCount;
}