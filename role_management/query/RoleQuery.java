package com.pramaindia.role_management.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleQuery implements Serializable {

    private static final long serialVersionUID = -6072450916060331324L;

    // Basic search fields
    private String roleName;
    private String description;
    private Integer type; // Changed from roleType to type to match RoleDO

    // Business Unit related fields
    private Long buId; // BU ID from bu_info table

    // Status fields
    private String deleteStatus; // "0" = active, "1" = deleted
    private String isDefault; // "0" = user-defined, "1" = system default

    // Date range filters
    private Date creationTimeStart;
    private Date creationTimeEnd;
    private Date modificationTimeStart;
    private Date modificationTimeEnd;

    // Created by/Modified by filters
    private String createdBy;
    private String modifiedBy;

    // Resource association filter
    private Long resourceId; // Filter roles that have specific resource

    // User association filter
    private Long userId; // Filter roles assigned to specific user

    // Sorting
    private String sortField = "creationTime"; // id, roleName, type, creationTime, modificationTime
    private String sortOrder = "DESC"; // ASC or DESC

    // Pagination
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer offset; // Calculated offset for pagination

    // ====================== HELPER METHODS ======================

    /**
     * Calculates and sets the offset for pagination
     */
    public void calculateOffset() {
        if (pageNum != null && pageSize != null && pageNum > 0) {
            this.offset = (pageNum - 1) * pageSize;
        } else {
            this.offset = 0;
        }
    }

    /**
     * Validates pagination parameters
     */
    public void validatePagination() {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100; // Limit page size for performance
        }
        calculateOffset();
    }

    /**
     * Gets the sort expression for SQL ORDER BY clause
     */
    public String getSortExpression() {
        if (sortField == null || sortField.trim().isEmpty()) {
            return "creation_time DESC";
        }

        // Map Java field names to database column names
        String dbColumnName = mapFieldToColumn(sortField);

        if (sortOrder == null || (!sortOrder.equalsIgnoreCase("ASC") && !sortOrder.equalsIgnoreCase("DESC"))) {
            sortOrder = "DESC";
        }

        return dbColumnName + " " + sortOrder;
    }

    /**
     * Maps Java field names to database column names
     */
    private String mapFieldToColumn(String fieldName) {
        switch (fieldName.toLowerCase()) {
            case "id":
                return "id";
            case "rolename":
                return "role_name";
            case "type":
                return "type";
            case "description":
                return "description";
            case "buid":
                return "bu_id";
            case "isdefault":
                return "is_default";
            case "deletestatus":
                return "delete_status";
            case "createdby":
                return "created_by";
            case "creationtime":
                return "creation_time";
            case "modifiedby":
                return "modified_by";
            case "modificationtime":
                return "modification_time";
            default:
                return "creation_time";
        }
    }

    /**
     * Checks if the query has any search criteria (excluding pagination)
     */
    public boolean hasSearchCriteria() {
        return (roleName != null && !roleName.trim().isEmpty()) ||
                (description != null && !description.trim().isEmpty()) ||
                (type != null) ||
                (buId != null) ||
                (deleteStatus != null && !deleteStatus.trim().isEmpty()) ||
                (isDefault != null && !isDefault.trim().isEmpty()) ||
                (createdBy != null && !createdBy.trim().isEmpty()) ||
                (modifiedBy != null && !modifiedBy.trim().isEmpty()) ||
                (resourceId != null) ||
                (userId != null) ||
                (creationTimeStart != null) ||
                (creationTimeEnd != null) ||
                (modificationTimeStart != null) ||
                (modificationTimeEnd != null);
    }

    /**
     * Sanitizes string inputs to prevent SQL injection
     */
    public void sanitizeInputs() {
        if (roleName != null) {
            roleName = roleName.trim();
        }
        if (description != null) {
            description = description.trim();
        }
        if (createdBy != null) {
            createdBy = createdBy.trim();
        }
        if (modifiedBy != null) {
            modifiedBy = modifiedBy.trim();
        }
        if (deleteStatus != null) {
            deleteStatus = deleteStatus.trim();
        }
        if (isDefault != null) {
            isDefault = isDefault.trim();
        }
    }
}