package com.pramaindia.role_management.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleDTO {

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
    private String countryName;
    private String roleTypeName;
    private List<ResourceDTO> resources;

    private List<Long> resourceIds;
}
