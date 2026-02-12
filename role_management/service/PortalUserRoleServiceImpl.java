package com.pramaindia.role_management.service;

import com.pramaindia.role_management.dao.PortalUserRoleDAO;
import com.pramaindia.role_management.model.PortalUserRoleDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortalUserRoleServiceImpl implements PortalUserRoleService {

    @Autowired
    private PortalUserRoleDAO portalUserRoleDAO;

    @Override
    public List<PortalUserRoleDO> getRolesByUserId(Long userId) {
        return portalUserRoleDAO.selectByUserId(userId);
    }

    @Override
    public List<PortalUserRoleDO> getUsersByRoleId(Long roleId) {
        return portalUserRoleDAO.selectByRoleId(roleId);
    }

    @Override
    @Transactional
    public boolean assignRoleToUser(Long userId, Long roleId) {
        try {
            // Check if already assigned
            List<PortalUserRoleDO> existing = portalUserRoleDAO.selectByUserId(userId);
            boolean alreadyAssigned = existing.stream()
                    .anyMatch(ur -> ur.getRoleId().equals(roleId));

            if (alreadyAssigned) {
                return true; // Already assigned, consider as success
            }

            // Assign role
            PortalUserRoleDO portalUserRoleDO = new PortalUserRoleDO();
            portalUserRoleDO.setUserId(String.valueOf(userId));
            portalUserRoleDO.setRoleId(roleId);

            int result = portalUserRoleDAO.insert(portalUserRoleDO);
            return result > 0;
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean assignRolesToUser(Long userId, List<Long> roleIds) {
        try {
            if (CollectionUtils.isEmpty(roleIds)) {
                return true;
            }

            // Get existing role assignments
            List<PortalUserRoleDO> existing = portalUserRoleDAO.selectByUserId(userId);
            List<Long> existingRoleIds = existing.stream()
                    .map(PortalUserRoleDO::getRoleId)
                    .collect(Collectors.toList());

            // Filter out already assigned roles
            List<Long> newRoleIds = roleIds.stream()
                    .filter(roleId -> !existingRoleIds.contains(roleId))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(newRoleIds)) {
                return true; // All roles already assigned
            }

            // Assign new roles
            for (Long roleId : newRoleIds) {
                PortalUserRoleDO portalUserRoleDO = new PortalUserRoleDO();
                portalUserRoleDO.setUserId(String.valueOf(userId));
                portalUserRoleDO.setRoleId(roleId);
                portalUserRoleDAO.insert(portalUserRoleDO);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeRoleFromUser(Long userId, Long roleId) {
        try {
            int result = portalUserRoleDAO.deleteByUserAndRole(userId, roleId);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeAllRolesFromUser(Long userId) {
        try {
            int result = portalUserRoleDAO.deleteByUserId(userId);
            return result >= 0; // Could be 0 if no roles were assigned
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeRoleFromAllUsers(Long roleId) {
        try {
            int result = portalUserRoleDAO.deleteByRoleId(roleId);
            return result >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean hasRole(Long userId, Long roleId) {
        List<PortalUserRoleDO> userRoles = portalUserRoleDAO.selectByUserId(userId);
        return userRoles.stream()
                .anyMatch(ur -> ur.getRoleId().equals(roleId));
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        List<PortalUserRoleDO> userRoles = portalUserRoleDAO.selectByUserId(userId);
        return userRoles.stream()
                .map(PortalUserRoleDO::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserIdsByRoleId(Long roleId) {
        List<PortalUserRoleDO> roleUsers = portalUserRoleDAO.selectByRoleId(roleId);
        return roleUsers.stream()
                .map(PortalUserRoleDO::getUserId)
                .collect(Collectors.toList());
    }
}
