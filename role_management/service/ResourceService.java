package com.pramaindia.role_management.service;


import com.pramaindia.role_management.dto.ResourceDTO;

import java.util.List;

public interface ResourceService {
    List<ResourceDTO> getAllResources();
    List<ResourceDTO> getResourcesWithHierarchy();
    List<ResourceDTO> getResourcesByRoleId(Long roleId);
}
