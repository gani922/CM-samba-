package com.pramaindia.role_management.model;

import lombok.Data;

import java.util.Date;

@Data
public class RoleDO {

    private Long id;
    private Long buId;
    private String roleName;
    private String deleteStatus;
    private String isDefault;
    private String createdBy;
    private Date creationTime;
    private String modifiedBy;
    private Date modificationTime;
    private Integer type;
    private String pagePermission;
    private Long subBuId;
    private Long countryId;
    private String description;

}
