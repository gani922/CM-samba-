package com.pramaindia.role_management.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PagePermissionDTO {
    private Long roleId;
    private Map<String, Boolean> permissions;

    @Data
    public static class PermissionItem {
        private String key;
        private String label;
        private String description;
        private Boolean enabled;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }
}
