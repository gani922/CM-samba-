package com.pramaindia.role_management.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleUpdateDTO {
    private Long id;
    private String roleName;
    private Integer type;
    private String description;
    private String pagePermission;
    private List<Long> resourceIds;
}