package com.pramaindia.role_management.dao;

import com.pramaindia.role_management.model.PortalUserRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PortalUserRoleDAO {
    List<PortalUserRoleDO> selectByUserId(Long userId);
    List<PortalUserRoleDO> selectByRoleId(Long roleId);
    int insert(PortalUserRoleDO portalUserRoleDO);
    int deleteByRoleId(Long roleId);
    int deleteByUserId(Long userId);
    int deleteByUserAndRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
