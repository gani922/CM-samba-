package com.pramaindia.role_management.service;


import com.pramaindia.role_management.model.PortalUserRoleDO;

import java.util.List;

public interface PortalUserRoleService {

    /**
     * Get all roles assigned to a user
     */
    List<PortalUserRoleDO> getRolesByUserId(Long userId);

    /**
     * Get all users assigned to a role
     */
    List<PortalUserRoleDO> getUsersByRoleId(Long roleId);

    /**
     * Assign a role to a user
     */
    boolean assignRoleToUser(Long userId, Long roleId);

    /**
     * Assign multiple roles to a user
     */
    boolean assignRolesToUser(Long userId, List<Long> roleIds);

    /**
     * Remove a role from a user
     */
    boolean removeRoleFromUser(Long userId, Long roleId);

    /**
     * Remove all roles from a user
     */
    boolean removeAllRolesFromUser(Long userId);

    /**
     * Remove a role from all users
     */
    boolean removeRoleFromAllUsers(Long roleId);

    /**
     * Check if a user has a specific role
     */
    boolean hasRole(Long userId, Long roleId);

    /**
     * Get all role IDs for a user
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * Get all user IDs for a role
     */
    List<String> getUserIdsByRoleId(Long roleId);
}
