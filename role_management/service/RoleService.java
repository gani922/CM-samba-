package com.pramaindia.role_management.service;


import com.pramaindia.role_management.dto.RoleDTO;
import com.pramaindia.role_management.query.PageResult;
import com.pramaindia.role_management.query.RoleQuery;

import java.util.List;
import java.util.Map;

public interface RoleService {
    PageResult<List<RoleDTO>> queryPage(RoleQuery query);
    List<Map<String, Object>> getAvailableCountries();
    RoleDTO getRoleDetail(Long id);
    Long addRole(RoleDTO roleCreateDTO);
    boolean updateRole(RoleDTO roleUpdateDTO);
    boolean deleteRole(Long id);
    boolean cloneRole(Long sourceRoleId, String newRoleName);
    List<Map<String, Object>> getEnumList(String enumCode);

    Map<String, Object> selectCountryById(Long countryId);

    List<Map<String, Object>> getBusinessUnits();
}
