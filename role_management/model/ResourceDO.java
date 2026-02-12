package com.pramaindia.role_management.model;

import lombok.Data;

import java.util.Date;

@Data
public class ResourceDO {

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

}
