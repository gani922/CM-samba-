package com.pramaindia.customer_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS) // Show all fields including nulls
public class BUInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Database mapping fields
    private Integer id;
    private String name;
    private String friendlyName;

    // Nullable fields
    private String countryCode;

    private Integer layer;

    @Builder.Default
    private Integer parentId = 0; // Default as 0

    @Builder.Default
    private String deleteStatus = "0";

    private String createdBy;

    // Timestamps as Long (milliseconds since epoch)
    private Long creationTime; // Will show as 1558582775000

    private String modifiedBy;

    private Long modificationTime;

    // Organization fields
    private Integer saleOrganizeId;

    @JsonProperty("buId")
    private Integer buId;

    private Integer buAdminId;

    // Internal reference field
    private String adminId;

    // Tree structure
    private List<BUInfoDTO> children; // Changed from List<Object> to List<BUInfoDTO>

    // Permission fields
    private String permissionName;

    private String permissionValue; // Changed from Object to String if it's a string value

    // Sales organizations
    private List<Object> salesOrgnizes; // Keeping as Object if structure is unknown

    // User configuration
    private Object userConfigDTO; // Keeping as Object if structure is unknown

    // Special support times
    private Long specialSupportTimeStart;

    private Long specialSupportTimeEnd;

    // Helper method to create a simple BUInfoDTO from database fields
    public static BUInfoDTO fromDatabase(Integer id, String name, String friendlyName,
                                         Integer layer, String adminId, Integer buAdminId) {
        return BUInfoDTO.builder()
                .id(id)
                .name(name)
                .friendlyName(friendlyName)
                .layer(layer)
                .adminId(adminId)
                .buAdminId(buAdminId)
                .parentId(0)
                .deleteStatus("0")
                .buId(id) // Assuming buId is same as id
                .build();
    }

    // Helper method to create a BUInfoDTO with all null values for testing
    public static BUInfoDTO createWithNulls() {
        return BUInfoDTO.builder()
                .id(null)
                .name(null)
                .friendlyName(null)
                .countryCode(null)
                .layer(null)
                .parentId(0)
                .deleteStatus("0")
                .createdBy(null)
                .creationTime(null)
                .modifiedBy(null)
                .modificationTime(null)
                .saleOrganizeId(null)
                .buId(null)
                .buAdminId(null)
                .adminId(null)
                .children(null)
                .permissionName(null)
                .permissionValue(null)
                .salesOrgnizes(null)
                .userConfigDTO(null)
                .specialSupportTimeStart(null)
                .specialSupportTimeEnd(null)
                .build();
    }

    // Helper method to check if this is a valid BU (has required fields)
    public boolean isValid() {
        return id != null && name != null && adminId != null;
    }

    // Add empty children list if null for tree structure
    public List<BUInfoDTO> getChildren() {
        if (children == null) {
            children = java.util.Collections.emptyList();
        }
        return children;
    }

    // Add empty salesOrgnizes list if null
    public List<Object> getSalesOrgnizes() {
        if (salesOrgnizes == null) {
            salesOrgnizes = java.util.Collections.emptyList();
        }
        return salesOrgnizes;
    }
}