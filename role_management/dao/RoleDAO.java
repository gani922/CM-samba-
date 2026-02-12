package com.pramaindia.role_management.dao;

import com.pramaindia.role_management.model.RoleDO;
import com.pramaindia.role_management.query.RoleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleDAO {
    List<RoleDO> selectByQuery(RoleQuery query);
    Long countByQuery(RoleQuery query);
    RoleDO selectById(Long id);
    int insert(RoleDO roleDO);
    int update(RoleDO roleDO);
    int delete(Long id);
    int deleteLogical(Long id);
    List<RoleDO> selectByCountryIds(@Param("countryIds") List<Long> countryIds);
    List<RoleDO> selectDefaultRoles();
    List<RoleDO> selectByRoleTypes(@Param("roleTypes") List<Integer> roleTypes, @Param("countryId") Long countryId);
}