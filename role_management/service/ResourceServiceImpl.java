package com.pramaindia.role_management.service;

import com.pramaindia.role_management.dao.ResourceDAO;
import com.pramaindia.role_management.dto.ResourceDTO;
import com.pramaindia.role_management.model.ResourceDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceDAO resourceDAO;

    @Override
    public List<ResourceDTO> getAllResources() {
        return resourceDAO.selectAll().stream().map(resourceDO -> {
            ResourceDTO dto = new ResourceDTO();
            BeanUtils.copyProperties(resourceDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ResourceDTO> getResourcesWithHierarchy() {
        List<ResourceDO> allResources = resourceDAO.selectResourcesWithHierarchy();

        // Build hierarchy
        Map<Long, ResourceDTO> resourceMap = allResources.stream()
                .map(resourceDO -> {
                    ResourceDTO dto = new ResourceDTO();
                    BeanUtils.copyProperties(resourceDO, dto);
                    return dto;
                })
                .collect(Collectors.toMap(ResourceDTO::getId, dto -> dto));

        List<ResourceDTO> rootResources = resourceMap.values().stream()
                .filter(dto -> dto.getParentId() == 0 || dto.getParentId() == null)
                .collect(Collectors.toList());

        for (ResourceDTO dto : resourceMap.values()) {
            if (dto.getParentId() != null && dto.getParentId() > 0) {
                ResourceDTO parent = resourceMap.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        return rootResources;
    }

    @Override
    public List<ResourceDTO> getResourcesByRoleId(Long roleId) {
        return resourceDAO.selectByRoleId(roleId).stream().map(resourceDO -> {
            ResourceDTO dto = new ResourceDTO();
            BeanUtils.copyProperties(resourceDO, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
