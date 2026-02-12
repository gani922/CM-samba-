package com.pramaindia.cust_manag.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pramaindia.cust_manag.dao.SapInfoMapper;
import com.pramaindia.cust_manag.dto.request.SapInfoRequestDTO;
import com.pramaindia.cust_manag.dto.request.SapInfoUpdateDTO;
import com.pramaindia.cust_manag.dto.response.PageResponseDTO;
import com.pramaindia.cust_manag.dto.response.SapInfoResponseDTO;
import com.pramaindia.cust_manag.dto.response.UserConfigResponseDTO;
import com.pramaindia.cust_manag.entity.SapInfo;
import com.pramaindia.cust_manag.entity.UserConfigDTO;
import com.pramaindia.cust_manag.enums.SapEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SapInfoServiceImpl implements SapInfoService {

    @Autowired
    private SapInfoMapper sapInfoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public SapInfoResponseDTO getSapInfoBySapId(String userSapId) {
        try {
            SapInfo sapInfo = sapInfoMapper.findBySapId(userSapId);
            if (sapInfo == null) {
                throw new RuntimeException("SAP info not found for SAP ID: " + userSapId);
            }
            return convertToResponseDTO(sapInfo);
        } catch (Exception e) {
            log.error("Error getting SAP info for SAP ID: {}", userSapId, e);
            throw new RuntimeException("Failed to get SAP info: " + e.getMessage());
        }
    }

    @Override
    public PageResponseDTO<SapInfoResponseDTO> getAllSapInfo(SapInfoRequestDTO requestDTO) {
        try {
            Map<String, Object> params = buildQueryParams(requestDTO);

            // Calculate offset for pagination
            int offset = (requestDTO.getPageNum() - 1) * requestDTO.getPageSize();
            params.put("offset", offset);
            params.put("pageSize", requestDTO.getPageSize());
            params.put("orderBy", requestDTO.getOrderBy());

            List<SapInfo> sapInfoList = sapInfoMapper.findAll(params);
            Long totalCount = sapInfoMapper.countByCondition(params);

            List<SapInfoResponseDTO> responseList = sapInfoList.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());

            return new PageResponseDTO<>(
                    responseList,
                    totalCount.intValue(),
                    requestDTO.getPageNum(),
                    requestDTO.getPageSize()
            );
        } catch (Exception e) {
            log.error("Error getting all SAP info", e);
            throw new RuntimeException("Failed to get SAP info list: " + e.getMessage());
        }
    }

    @Override
    public PageResponseDTO<SapInfoResponseDTO> searchSapInfo(SapInfoRequestDTO requestDTO) {
        return getAllSapInfo(requestDTO);
    }

    @Override
    @Transactional
    public SapInfoResponseDTO createSapInfo(SapInfo sapInfo) {
        try {
            // Check if SAP ID already exists
            SapInfo existing = sapInfoMapper.findBySapId(sapInfo.getUserSapId());
            if (existing != null) {
                throw new RuntimeException("SAP ID already exists: " + sapInfo.getUserSapId());
            }

            // Check if email already exists
            if (StringUtils.hasText(sapInfo.getUserEmail())) {
                existing = sapInfoMapper.findByEmail(sapInfo.getUserEmail());
                if (existing != null) {
                    throw new RuntimeException("Email already exists: " + sapInfo.getUserEmail());
                }
            }

            // Check if mobile already exists
            if (StringUtils.hasText(sapInfo.getUserMobile())) {
                existing = sapInfoMapper.findByMobile(sapInfo.getUserMobile());
                if (existing != null) {
                    throw new RuntimeException("Mobile number already exists: " + sapInfo.getUserMobile());
                }
            }

            // Check if PAN number already exists
            if (StringUtils.hasText(sapInfo.getPanNumber())) {
                existing = sapInfoMapper.findByPanNumber(sapInfo.getPanNumber());
                if (existing != null) {
                    throw new RuntimeException("PAN number already exists: " + sapInfo.getPanNumber());
                }
            }

            // Check if GST number already exists
            if (StringUtils.hasText(sapInfo.getGstNumber())) {
                existing = sapInfoMapper.findByGstNumber(sapInfo.getGstNumber());
                if (existing != null) {
                    throw new RuntimeException("GST number already exists: " + sapInfo.getGstNumber());
                }
            }

            // Set default values
            if (!StringUtils.hasText(sapInfo.getIsLock())) {
                sapInfo.setIsLock("N");
            }
            if (!StringUtils.hasText(sapInfo.getDeleteStatus())) {
                sapInfo.setDeleteStatus("0");
            }
            if (!StringUtils.hasText(sapInfo.getAuthLaw())) {
                sapInfo.setAuthLaw("0");
            }
            if (!StringUtils.hasText(sapInfo.getUserLevel())) {
                sapInfo.setUserLevel("1");
            }
            if (!StringUtils.hasText(sapInfo.getShortMessage())) {
                sapInfo.setShortMessage("0");
            }
            if (!StringUtils.hasText(sapInfo.getUserStockShow())) {
                sapInfo.setUserStockShow("0");
            }
            if (!StringUtils.hasText(sapInfo.getDownloadStockListShow())) {
                sapInfo.setDownloadStockListShow("0");
            }
            if (!StringUtils.hasText(sapInfo.getAddCoupons())) {
                sapInfo.setAddCoupons("0");
            }
            if (!StringUtils.hasText(sapInfo.getShowCouponsWindows())) {
                sapInfo.setShowCouponsWindows("1");
            }
            if (!StringUtils.hasText(sapInfo.getSaleChannel())) {
                sapInfo.setSaleChannel("0");
            }
            if (!StringUtils.hasText(sapInfo.getAddFlashSales())) {
                sapInfo.setAddFlashSales("0");
            }
            if (!StringUtils.hasText(sapInfo.getResetDevicePassword())) {
                sapInfo.setResetDevicePassword("1");
            }
            if (!StringUtils.hasText(sapInfo.getUserAddressLock())) {
                sapInfo.setUserAddressLock("0");
            }

            // Generate password salt and hash if not provided
            if (!StringUtils.hasText(sapInfo.getUserSalt())) {
                sapInfo.setUserSalt(generateRandomSalt());
            }
            if (!StringUtils.hasText(sapInfo.getUserPassword())) {
                String defaultPassword = generateDefaultPassword();
                sapInfo.setUserPassword(hashPassword(defaultPassword, sapInfo.getUserSalt()));
            }

            // Insert into database
            int result = sapInfoMapper.insert(sapInfo);
            if (result <= 0) {
                throw new RuntimeException("Failed to create SAP info");
            }

            // Return created entity
            SapInfo createdSapInfo = sapInfoMapper.findBySapId(sapInfo.getUserSapId());
            return convertToResponseDTO(createdSapInfo);

        } catch (Exception e) {
            log.error("Error creating SAP info: {}", sapInfo.getUserSapId(), e);
            throw new RuntimeException("Failed to create SAP info: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public SapInfoResponseDTO updateSapInfo(SapInfoUpdateDTO updateDTO) {
        try {
            // Check if SAP info exists
            SapInfo existingSapInfo = sapInfoMapper.findBySapId(updateDTO.getUserSapId());
            if (existingSapInfo == null) {
                throw new RuntimeException("SAP info not found for SAP ID: " + updateDTO.getUserSapId());
            }

            // Update fields
            BeanUtils.copyProperties(updateDTO, existingSapInfo,
                    "userId", "userSapId", "creationTime", "createdBy");

            // Update modification time and user
            existingSapInfo.setModificationTime(new Date());
            if (StringUtils.hasText(updateDTO.getModifiedBy())) {
                existingSapInfo.setModifiedBy(updateDTO.getModifiedBy());
            }

            // Update in database
            int result = sapInfoMapper.update(existingSapInfo);
            if (result <= 0) {
                throw new RuntimeException("Failed to update SAP info");
            }

            // Return updated entity
            SapInfo updatedSapInfo = sapInfoMapper.findBySapId(updateDTO.getUserSapId());
            return convertToResponseDTO(updatedSapInfo);

        } catch (Exception e) {
            log.error("Error updating SAP info: {}", updateDTO.getUserSapId(), e);
            throw new RuntimeException("Failed to update SAP info: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteSapInfo(String userSapId, String modifiedBy) {
        try {
            // Check if SAP info exists
            SapInfo existingSapInfo = sapInfoMapper.findBySapId(userSapId);
            if (existingSapInfo == null) {
                throw new RuntimeException("SAP info not found for SAP ID: " + userSapId);
            }

            // Soft delete
            int result = sapInfoMapper.deleteBySapId(userSapId, modifiedBy);
            return result > 0;

        } catch (Exception e) {
            log.error("Error deleting SAP info: {}", userSapId, e);
            throw new RuntimeException("Failed to delete SAP info: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateLockStatus(String userSapId, String isLock, String modifiedBy) {
        try {
            // Check if SAP info exists
            SapInfo existingSapInfo = sapInfoMapper.findBySapId(userSapId);
            if (existingSapInfo == null) {
                throw new RuntimeException("SAP info not found for SAP ID: " + userSapId);
            }

            // Update lock status
            int result = sapInfoMapper.updateLockStatus(userSapId, isLock, modifiedBy);
            return result > 0;

        } catch (Exception e) {
            log.error("Error updating lock status for SAP info: {}", userSapId, e);
            throw new RuntimeException("Failed to update lock status: " + e.getMessage());
        }
    }

    @Override
    public SapInfoResponseDTO getSapInfoByEmail(String email) {
        try {
            SapInfo sapInfo = sapInfoMapper.findByEmail(email);
            if (sapInfo == null) {
                throw new RuntimeException("SAP info not found for email: " + email);
            }
            return convertToResponseDTO(sapInfo);
        } catch (Exception e) {
            log.error("Error getting SAP info for email: {}", email, e);
            throw new RuntimeException("Failed to get SAP info: " + e.getMessage());
        }
    }

    @Override
    public SapInfoResponseDTO getSapInfoByMobile(String mobile) {
        try {
            SapInfo sapInfo = sapInfoMapper.findByMobile(mobile);
            if (sapInfo == null) {
                throw new RuntimeException("SAP info not found for mobile: " + mobile);
            }
            return convertToResponseDTO(sapInfo);
        } catch (Exception e) {
            log.error("Error getting SAP info for mobile: {}", mobile, e);
            throw new RuntimeException("Failed to get SAP info: " + e.getMessage());
        }
    }

    @Override
    public SapInfoResponseDTO getSapInfoByPanNumber(String panNumber) {
        try {
            SapInfo sapInfo = sapInfoMapper.findByPanNumber(panNumber);
            if (sapInfo == null) {
                throw new RuntimeException("SAP info not found for PAN number: " + panNumber);
            }
            return convertToResponseDTO(sapInfo);
        } catch (Exception e) {
            log.error("Error getting SAP info for PAN number: {}", panNumber, e);
            throw new RuntimeException("Failed to get SAP info: " + e.getMessage());
        }
    }

    @Override
    public SapInfoResponseDTO getSapInfoByGstNumber(String gstNumber) {
        try {
            SapInfo sapInfo = sapInfoMapper.findByGstNumber(gstNumber);
            if (sapInfo == null) {
                throw new RuntimeException("SAP info not found for GST number: " + gstNumber);
            }
            return convertToResponseDTO(sapInfo);
        } catch (Exception e) {
            log.error("Error getting SAP info for GST number: {}", gstNumber, e);
            throw new RuntimeException("Failed to get SAP info: " + e.getMessage());
        }
    }

    @Override
    public List<SapInfoResponseDTO> getLockedUsers() {
        try {
            List<SapInfo> sapInfoList = sapInfoMapper.findLockedUsers();
            return sapInfoList.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting locked users", e);
            throw new RuntimeException("Failed to get locked users: " + e.getMessage());
        }
    }

    @Override
    public List<SapInfoResponseDTO> getUsersByUserGroup(String userGroup) {
        try {
            List<SapInfo> sapInfoList = sapInfoMapper.findByUserGroup(userGroup);
            return sapInfoList.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting users by user group: {}", userGroup, e);
            throw new RuntimeException("Failed to get users by user group: " + e.getMessage());
        }
    }

    @Override
    public List<SapInfoResponseDTO> getUsersByRegion(String regionId) {
        try {
            List<SapInfo> sapInfoList = sapInfoMapper.findByRegion(regionId);
            return sapInfoList.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting users by region: {}", regionId, e);
            throw new RuntimeException("Failed to get users by region: " + e.getMessage());
        }
    }

    @Override
    public List<SapInfoResponseDTO> getUsersByDistrict(String districtId) {
        try {
            List<SapInfo> sapInfoList = sapInfoMapper.findByDistrict(districtId);
            return sapInfoList.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting users by district: {}", districtId, e);
            throw new RuntimeException("Failed to get users by district: " + e.getMessage());
        }
    }

    @Override
    public void updateLastLoginTime(String userSapId) {
        try {
            sapInfoMapper.updateLastLoginTime(userSapId);
        } catch (Exception e) {
            log.error("Error updating last login time for SAP ID: {}", userSapId, e);
            throw new RuntimeException("Failed to update last login time: " + e.getMessage());
        }
    }

    @Override
    public List<SapInfoResponseDTO> getUsersWithActiveSpecialSupport() {
        try {
            List<SapInfo> sapInfoList = sapInfoMapper.findBySpecialSupportTimeRange();
            return sapInfoList.stream()
                    .map(this::convertToResponseDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting users with active special support", e);
            throw new RuntimeException("Failed to get users with active special support: " + e.getMessage());
        }
    }

    @Override
    public SapInfoResponseDTO convertToResponseDTO(SapInfo sapInfo) {
        if (sapInfo == null) {
            return null;
        }

        SapInfoResponseDTO responseDTO = new SapInfoResponseDTO();
        BeanUtils.copyProperties(sapInfo, responseDTO);

        // Add descriptions for enum fields
        responseDTO.setLockStatusDesc(SapEnums.getDescriptionByCode(sapInfo.getIsLock()));
        responseDTO.setUserLevelDesc(SapEnums.getDescriptionByCode(sapInfo.getUserLevel()));
        responseDTO.setSapUserGroupDesc(SapEnums.getDescriptionByCode(sapInfo.getSapUserGroup()));
        responseDTO.setSaleChannelDesc(SapEnums.getDescriptionByCode(sapInfo.getSaleChannel()));

        // Parse user config JSON
        if (StringUtils.hasText(sapInfo.getUserConfig())) {
            try {
                UserConfigDTO userConfigDTO = objectMapper.readValue(
                        sapInfo.getUserConfig(), UserConfigDTO.class);
                responseDTO.setUserConfigDTO(new UserConfigResponseDTO());
            } catch (JsonProcessingException e) {
                log.warn("Failed to parse user config JSON: {}", sapInfo.getUserConfig(), e);
            }
        }

        return responseDTO;
    }

    @Override
    public SapInfo convertToEntity(SapInfoRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        SapInfo sapInfo = new SapInfo();
        BeanUtils.copyProperties(requestDTO, sapInfo);
        return sapInfo;
    }

    // Helper methods
    private Map<String, Object> buildQueryParams(SapInfoRequestDTO requestDTO) {
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasText(requestDTO.getUserSapId())) {
            params.put("userSapId", requestDTO.getUserSapId());
        }
        if (StringUtils.hasText(requestDTO.getUserName())) {
            params.put("userName", requestDTO.getUserName());
        }
        if (StringUtils.hasText(requestDTO.getUserEmail())) {
            params.put("userEmail", requestDTO.getUserEmail());
        }
        if (StringUtils.hasText(requestDTO.getUserMobile())) {
            params.put("userMobile", requestDTO.getUserMobile());
        }
        if (StringUtils.hasText(requestDTO.getIsLock())) {
            params.put("isLock", requestDTO.getIsLock());
        }
        if (StringUtils.hasText(requestDTO.getSapUserGroup())) {
            params.put("sapUserGroup", requestDTO.getSapUserGroup());
        }
        if (StringUtils.hasText(requestDTO.getRegionId())) {
            params.put("regionId", requestDTO.getRegionId());
        }
        if (StringUtils.hasText(requestDTO.getDistrictId())) {
            params.put("districtId", requestDTO.getDistrictId());
        }
        if (StringUtils.hasText(requestDTO.getUserGrade())) {
            params.put("userGrade", requestDTO.getUserGrade());
        }
        if (StringUtils.hasText(requestDTO.getPanNumber())) {
            params.put("panNumber", requestDTO.getPanNumber());
        }
        if (StringUtils.hasText(requestDTO.getGstNumber())) {
            params.put("gstNumber", requestDTO.getGstNumber());
        }
        if (StringUtils.hasText(requestDTO.getSearchKeyword())) {
            params.put("searchKeyword", requestDTO.getSearchKeyword());
        }
        if (requestDTO.getStartValidDate() != null) {
            params.put("startValidDate", requestDTO.getStartValidDate());
        }
        if (requestDTO.getEndValidDate() != null) {
            params.put("endValidDate", requestDTO.getEndValidDate());
        }

        return params;
    }

    private String generateRandomSalt() {
        // Generate 8-character random salt
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            salt.append(chars.charAt(random.nextInt(chars.length())));
        }
        return salt.toString();
    }

    private String generateDefaultPassword() {
        // Generate default password (you can customize this)
        return "Hikvision@123";
    }

    private String hashPassword(String password, String salt) {
        // Simple hash function (you should use a proper hashing algorithm like BCrypt)
        // This is just a placeholder
        //return org.apache.commons.codec.digest.DigestUtils.md5Hex(password + salt);
        return null;
    }
}