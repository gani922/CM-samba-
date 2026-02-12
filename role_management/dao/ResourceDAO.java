package com.pramaindia.role_management.dao;

import com.pramaindia.role_management.model.ResourceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceDAO {
    List<ResourceDO> selectAll();
    List<ResourceDO> selectByRoleId(Long roleId);
    List<ResourceDO> selectByParentId(Long parentId);
    ResourceDO selectById(Long id);
    List<Long> selectResourceIdsByRoleId(Long roleId);
    int insertRoleResources(@Param("roleId") Long roleId, @Param("resourceIds") List<Long> resourceIds);
    int deleteRoleResources(Long roleId);
    List<ResourceDO> selectResourcesWithHierarchy();
}
