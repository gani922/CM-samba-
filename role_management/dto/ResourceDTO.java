package com.pramaindia.role_management.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ResourceDTO {

    private Long id;
    private String name;
    private String type;
    private String authorityAttr;
    private String url;
    private Long parentId;
    private String createdBy;
    private Date creationTime;
    private String modifiedBy;
    private Date modificationTime;
    private String deleteStatus;
    private String isDefault;
    private String code;
    private Integer sort;
    private String parentName;
    private List<ResourceDTO> children;

}
