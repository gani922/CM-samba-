package com.pramaindia.role_management.model;

import lombok.Data;

@Data
public class PortalUserRoleDO {
    private String userId;
    private Long roleId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
