package com.pramaindia.login.model.dto;

import lombok.Data;

@Data
public class ExportRequestDto {
    private String searchJson;
    private Long startTime;
    private Long endTime;
    private String userEmail;
}