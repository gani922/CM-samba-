package com.pramaindia.cust_manag.service;




import com.pramaindia.cust_manag.dto.request.SapInfoRequestDTO;
import com.pramaindia.cust_manag.dto.request.SapInfoUpdateDTO;
import com.pramaindia.cust_manag.dto.response.PageResponseDTO;
import com.pramaindia.cust_manag.dto.response.SapInfoResponseDTO;
import com.pramaindia.cust_manag.entity.SapInfo;

import java.util.List;

public interface SapInfoService {

    /**
     * Get SAP info by SAP ID
     */
    SapInfoResponseDTO getSapInfoBySapId(String userSapId);

    /**
     * Get all SAP info with pagination
     */
    PageResponseDTO<SapInfoResponseDTO> getAllSapInfo(SapInfoRequestDTO requestDTO);

    /**
     * Search SAP info with conditions
     */
    PageResponseDTO<SapInfoResponseDTO> searchSapInfo(SapInfoRequestDTO requestDTO);

    /**
     * Create new SAP info
     */
    SapInfoResponseDTO createSapInfo(SapInfo sapInfo);

    /**
     * Update SAP info
     */
    SapInfoResponseDTO updateSapInfo(SapInfoUpdateDTO updateDTO);

    /**
     * Delete SAP info (soft delete)
     */
    boolean deleteSapInfo(String userSapId, String modifiedBy);

    /**
     * Update lock status
     */
    boolean updateLockStatus(String userSapId, String isLock, String modifiedBy);

    /**
     * Get SAP info by email
     */
    SapInfoResponseDTO getSapInfoByEmail(String email);

    /**
     * Get SAP info by mobile
     */
    SapInfoResponseDTO getSapInfoByMobile(String mobile);

    /**
     * Get SAP info by PAN number
     */
    SapInfoResponseDTO getSapInfoByPanNumber(String panNumber);

    /**
     * Get SAP info by GST number
     */
    SapInfoResponseDTO getSapInfoByGstNumber(String gstNumber);

    /**
     * Get locked users
     */
    List<SapInfoResponseDTO> getLockedUsers();

    /**
     * Get users by user group
     */
    List<SapInfoResponseDTO> getUsersByUserGroup(String userGroup);

    /**
     * Get users by region
     */
    List<SapInfoResponseDTO> getUsersByRegion(String regionId);

    /**
     * Get users by district
     */
    List<SapInfoResponseDTO> getUsersByDistrict(String districtId);

    /**
     * Update last login time
     */
    void updateLastLoginTime(String userSapId);

    /**
     * Get users with active special support
     */
    List<SapInfoResponseDTO> getUsersWithActiveSpecialSupport();

    /**
     * Convert entity to response DTO
     */
    SapInfoResponseDTO convertToResponseDTO(SapInfo sapInfo);

    /**
     * Convert request DTO to entity
     */
    SapInfo convertToEntity(SapInfoRequestDTO requestDTO);
}