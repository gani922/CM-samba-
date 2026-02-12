package com.pramaindia.login.model.entity;

import lombok.Data;

@Data
public class CompanyEntity {
    private String sapId;
    private String companyName;
    private String region;
    private String branch;
    private String district;
}