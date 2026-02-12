package com.pramaindia.role_management.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleCreateDTO {

    private Long buId;
    private String roleName;
    private Integer type;
    private Long subBuId;
    private Long countryId;
    private String description;
    private String pagePermission;
    private List<Long> resourceIds;

}
