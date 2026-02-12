package com.pramaindia.role_management.service;

import com.pramaindia.customer_management.exception.BusinessException;
import com.pramaindia.role_management.dao.CountryDAO;
import com.pramaindia.role_management.dao.ResourceDAO;
import com.pramaindia.role_management.dao.RoleDAO;
import com.pramaindia.role_management.dto.ResourceDTO;
import com.pramaindia.role_management.dto.RoleDTO;
import com.pramaindia.role_management.enums.PortalUserRoleType;
import com.pramaindia.role_management.model.RoleDO;
import com.pramaindia.role_management.query.PageResult;
import com.pramaindia.role_management.query.RoleQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private ResourceDAO resourceDAO;

    @Autowired
    private CountryDAO countryDAO;

    @Override
    public List<Map<String, Object>> getAvailableCountries() {
        try {
            List<Map<String, Object>> countries = countryDAO.selectAllCountries();


            if (countries.isEmpty()) {

                throw new BusinessException("No countries found in the database");
            }

            return countries;
        } catch (Exception e) {
            throw new BusinessException("Error fetching countries from database: " + e.getMessage());
        }
    }


    public List<Map<String, Object>> getBusinessUnits() {
        try {
            return countryDAO.selectBusinessUnits();
        } catch (Exception e) {
            throw new BusinessException("Error fetching business units: " + e.getMessage());
        }
    }

//    public Map<String, Object> getCountryDetails(Lo) {
//        try {
//            List<Map<String, Object>> countries = getAvailableCountries();
//
//            return countries.stream()
//                    .filter(country -> countryId.equals(country.get("countryId")))
//                    .findFirst()
//                    .orElseThrow(() -> new BusinessException("Country not found with ID: " + countryId));
//        } catch (Exception e) {
//            throw new BusinessException("Error fetching country details: " + e.getMessage());
//        }
//    }

    @Override
    public RoleDTO getRoleDetail(Long id) {
        RoleDO roleDO = roleDAO.selectById(id);
        if (roleDO == null) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(roleDO, roleDTO);

        // Set role type name
        if (roleDO.getType() != null) {
            String roleTypeName = PortalUserRoleType.getValueByCode(roleDO.getType());
            roleDTO.setRoleTypeName(roleTypeName != null ? roleTypeName : "User Defined");
        } else {
            roleDTO.setRoleTypeName("User Defined");
        }

        // Get assigned resources
        List<ResourceDTO> resources = resourceDAO.selectByRoleId(id).stream().map(resourceDO -> {
            ResourceDTO resourceDTO = new ResourceDTO();
            BeanUtils.copyProperties(resourceDO, resourceDTO);
            return resourceDTO;
        }).collect(Collectors.toList());

        roleDTO.setResources(resources);

        return roleDTO;
    }


    @Override
    public List<Map<String, Object>> getEnumList(String enumCode) {
        List<Map<String, Object>> enumList = new ArrayList<>();

        if ("roleType".equals(enumCode)) {
            for (PortalUserRoleType type : PortalUserRoleType.values()) {
                Map<String, Object> enumItem = new HashMap<>();
                enumItem.put("code", type.getCode());
                enumItem.put("name", type.getValue());
                enumItem.put("description", type.getValue());
                enumList.add(enumItem);
            }
        }

        return enumList;
    }

    @Override
    public Map<String, Object> selectCountryById(Long countryId) {
        validateCountryId(countryId);

        try {
            // Method 1: Using findCountryById (returns single map)
            Map<String, Object> country = countryDAO.selectCountryById(countryId);

            if (country == null || country.isEmpty()) {
                throw new BusinessException("Country not found with ID: " + countryId);
            }

            return country;

        } catch (Exception e) {
            throw new BusinessException("Error fetching country details: " + e.getMessage());
        }
    }


    // Update deleteRole method if needed
    @Override
    @Transactional
    public boolean deleteRole(Long id) {
        RoleDO role = roleDAO.selectById(id);
        if (role == null) {
            return false;
        }

        // Check if role is default
        if ("1".equals(role.getIsDefault())) {
            throw new RuntimeException("Default roles cannot be deleted");
        }

        // Delete user-role associations first (important!)
//        portalUserRoleService.removeRoleFromAllUsers(id);

        // Delete role-resource associations
        resourceDAO.deleteRoleResources(id);

        // Logical delete
        return roleDAO.deleteLogical(id) > 0;
    }

    private void validateCountryId(Long countryId) {
        if (countryId == null) {
            throw new IllegalArgumentException("Country ID cannot be null");
        }
        if (countryId <= 0) {
            throw new IllegalArgumentException("Invalid country ID: " + countryId);
        }
    }

    @Override
    public PageResult<List<RoleDTO>> queryPage(RoleQuery query) {
        // Validate and set default pagination values
        if (query.getPageNum() == null || query.getPageNum() <= 0) {
            query.setPageNum(1);
        }
        if (query.getPageSize() == null || query.getPageSize() <= 0) {
            query.setPageSize(10);
        }
        if (query.getPageSize() > 100) {
            query.setPageSize(100); // Limit page size
        }

        // Calculate offset for pagination
        int offset = (query.getPageNum() - 1) * query.getPageSize();
        query.setOffset(offset);

        List<RoleDO> roleList = roleDAO.selectByQuery(query);
        Long total = roleDAO.countByQuery(query);

        List<RoleDTO> roleDTOList = roleList.stream().map(roleDO -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(roleDO, roleDTO);

            // Set role type name
            if (roleDO.getType() != null) {
                String roleTypeName = PortalUserRoleType.getValueByCode(roleDO.getType());
                roleDTO.setRoleTypeName(roleTypeName != null ? roleTypeName : "User Defined");
            } else {
                roleDTO.setRoleTypeName("User Defined");
            }

            return roleDTO;
        }).collect(Collectors.toList());

        PageResult<List<RoleDTO>> pageResult = new PageResult<>();
        pageResult.setPageNum(query.getPageNum());
        pageResult.setPageSize(query.getPageSize());
        pageResult.setTotal(total);
        pageResult.setTotalPages((int) Math.ceil((double) total / query.getPageSize()));
        pageResult.setList(roleDTOList);

        return pageResult;
    }

    @Override
    @Transactional
    public Long addRole(RoleDTO roleCreateDTO) {
        // Validate input
        if (!StringUtils.hasText(roleCreateDTO.getRoleName())) {
            throw new IllegalArgumentException("Role name is required");
        }

        // Check if role name already exists
        RoleQuery query = new RoleQuery();
        query.setRoleName(roleCreateDTO.getRoleName());
        Long existingCount = roleDAO.countByQuery(query);
        if (existingCount > 0) {
            throw new IllegalArgumentException("Role name already exists");
        }

        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(roleCreateDTO, roleDO);

        // Set default values
        roleDO.setDeleteStatus("0");
        roleDO.setIsDefault("0");
        roleDO.setCreatedBy("system");
        roleDO.setCreationTime(new Date());

        // Insert role
        int result = roleDAO.insert(roleDO);
        if (result <= 0) {
            throw new RuntimeException("Failed to create role");
        }

        // Insert role-resource associations
        if (roleCreateDTO.getResourceIds() != null && !roleCreateDTO.getResourceIds().isEmpty()) {
            resourceDAO.insertRoleResources(roleDO.getId(), roleCreateDTO.getResourceIds());
        }

        return roleDO.getId();
    }

    @Override
    @Transactional
    public boolean updateRole(RoleDTO roleUpdateDTO) {
        // Validate input
        if (roleUpdateDTO.getId() == null) {
            throw new IllegalArgumentException("Role ID is required");
        }
        if (!StringUtils.hasText(roleUpdateDTO.getRoleName())) {
            throw new IllegalArgumentException("Role name is required");
        }

        RoleDO existingRole = roleDAO.selectById(roleUpdateDTO.getId());
        if (existingRole == null) {
            return false;
        }

        // Check if role name already exists (excluding current role)
        RoleQuery query = new RoleQuery();
        query.setRoleName(roleUpdateDTO.getRoleName());
        List<RoleDO> existingRoles = roleDAO.selectByQuery(query);
        boolean nameExists = existingRoles.stream()
                .anyMatch(role -> !role.getId().equals(roleUpdateDTO.getId()));
        if (nameExists) {
            throw new IllegalArgumentException("Role name already exists");
        }

        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(roleUpdateDTO, roleDO);
        roleDO.setModifiedBy("system");
        roleDO.setModificationTime(new Date());

        // Update role
        int result = roleDAO.update(roleDO);

        if (result > 0 && roleUpdateDTO.getResourceIds() != null) {
            // Update role-resource associations
            resourceDAO.deleteRoleResources(roleUpdateDTO.getId());
            if (!roleUpdateDTO.getResourceIds().isEmpty()) {
                resourceDAO.insertRoleResources(roleUpdateDTO.getId(), roleUpdateDTO.getResourceIds());
            }
        }

        return result > 0;
    }

    @Override
    @Transactional
    public boolean cloneRole(Long sourceRoleId, String newRoleName) {
        // Validate input
        if (!StringUtils.hasText(newRoleName)) {
            throw new IllegalArgumentException("New role name is required");
        }

        RoleDO sourceRole = roleDAO.selectById(sourceRoleId);
        if (sourceRole == null) {
            throw new IllegalArgumentException("Source role not found");
        }

        // Check if new role name already exists
        RoleQuery query = new RoleQuery();
        query.setRoleName(newRoleName);
        Long existingCount = roleDAO.countByQuery(query);
        if (existingCount > 0) {
            throw new IllegalArgumentException("Role name already exists");
        }

        // Create new role based on source
        RoleDO newRole = new RoleDO();
        BeanUtils.copyProperties(sourceRole, newRole);
        newRole.setId(null);
        newRole.setRoleName(newRoleName);
        newRole.setIsDefault("0");
        newRole.setCreatedBy("system");
        newRole.setCreationTime(new Date());
        newRole.setModifiedBy(null);
        newRole.setModificationTime(null);

        // Insert new role
        int insertResult = roleDAO.insert(newRole);
        if (insertResult <= 0) {
            throw new RuntimeException("Failed to clone role");
        }

        // Copy role-resource associations
        List<Long> resourceIds = resourceDAO.selectResourceIdsByRoleId(sourceRoleId);
        if (!resourceIds.isEmpty()) {
            resourceDAO.insertRoleResources(newRole.getId(), resourceIds);
        }

        return true;
    }


}