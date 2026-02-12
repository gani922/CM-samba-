package com.pramaindia.customer_management.model;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BUInfo {
    private Integer id;
    private String name;
    private String friendlyName;
    private String countryCode;
    private Integer layer;
    private Integer parentId;
    private String deleteStatus;
    private String createdBy;
    private LocalDateTime creationTime;
    private String modifiedBy;
    private LocalDateTime modificationTime;
    private String saleOrganizeId;
    private Integer buId;
    private Integer buAdminId;
    private String adminId;
    private List<BUInfo> children;
    private String permissionName;
    private Integer permissionValue;
    private Object salesOrgnizes;
    private Object userConfigDTO;
    private String specialSupportTimeStart;
    private String specialSupportTimeEnd;
}