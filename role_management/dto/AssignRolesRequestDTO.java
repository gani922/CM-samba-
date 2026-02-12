package com.pramaindia.role_management.dto;

import java.util.List;

public class AssignRolesRequestDTO {
    private Long userId;
    private List<Long> roleIds;

    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<Long> getRoleIds() { return roleIds; }
    public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
}