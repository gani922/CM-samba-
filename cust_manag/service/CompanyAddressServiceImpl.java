package com.pramaindia.cust_manag.service;

import com.pramaindia.cust_manag.dao.CompanyAddressDAO;
import com.pramaindia.cust_manag.dto.request.CompanyAddressRequestDTO;
import com.pramaindia.cust_manag.dto.response.CompanyAddressResponseDTO;
import com.pramaindia.cust_manag.entity.CompanyAddress;
import com.pramaindia.cust_manag.enums.AddressTypeEnum;
import com.pramaindia.cust_manag.enums.DefaultAddressEnum;
import com.pramaindia.cust_manag.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyAddressServiceImpl implements CompanyAddressService {

    private final CompanyAddressDAO companyAddressDAO;

    @Override
    public CompanyAddressResponseDTO createAddress(CompanyAddressRequestDTO request) {
        CompanyAddress address = mapToEntity(request);

        // Generate address ID if not provided
        if (address.getAddressId() == null) {
            Long maxId = companyAddressDAO.getMaxAddressId();
            address.setAddressId(maxId == null ? 1L : maxId + 1);
        }

        companyAddressDAO.insert(address);
        return mapToResponse(address);
    }

    @Override
    public List<CompanyAddressResponseDTO> getAllAddresses() {
        return companyAddressDAO.selectAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CompanyAddressResponseDTO getAddressById(Long addressId) {
        CompanyAddress address = companyAddressDAO.selectByPrimaryKey(addressId);
        if (address == null) {
            throw new RuntimeException("Address with ID " + addressId + " not found");
        }
        return mapToResponse(address);
    }

    @Override
    public CompanyAddressResponseDTO updateAddress(Long addressId, CompanyAddressRequestDTO request) {
        CompanyAddress existing = companyAddressDAO.selectByPrimaryKey(addressId);
        if (existing == null) {
            throw new RuntimeException("Address with ID " + addressId + " not found");
        }

        CompanyAddress updated = mapToEntity(request);
        updated.setAddressId(addressId);
        companyAddressDAO.updateByPrimaryKeySelective(updated);

        return mapToResponse(companyAddressDAO.selectByPrimaryKey(addressId));
    }

    @Override
    public void deleteAddress(Long addressId) {
        CompanyAddress address = companyAddressDAO.selectByPrimaryKey(addressId);
        if (address == null) {
            throw new RuntimeException("Address with ID " + addressId + " not found");
        }

        // Soft delete by updating status
        address.setDeleteStatus(StatusEnum.DELETED);
        companyAddressDAO.updateByPrimaryKeySelective(address);
    }

    @Override
    public CompanyAddressResponseDTO getDefaultAddressByUserSapId(String userSapId) {
        Long addressId = companyAddressDAO.findDefaultAddressIdByUserSapId(userSapId);
        if (addressId == null) {
            throw new RuntimeException("Default address not found for user SAP ID: " + userSapId);
        }
        return getAddressById(addressId);
    }

    private CompanyAddress mapToEntity(CompanyAddressRequestDTO r) {
        CompanyAddress a = new CompanyAddress();

        a.setAddressId(r.getAddressId());
        a.setUserSapId(r.getUserSapId());
        a.setAddressCountryCode(r.getAddressCountryCode());
        a.setAddressProvince(r.getAddressProvince());
        a.setAddressCity(r.getAddressCity());
        a.setAddressStreet(r.getAddressStreet());
        a.setAddressZipcode(r.getAddressZipcode());

        // Map directly from request (no conversion needed)
        if (r.getAddressType() != null && !r.getAddressType().isEmpty()) {
            switch (r.getAddressType().toUpperCase()) {
                case "B":
                    a.setAddressType(AddressTypeEnum.BILLING);
                    break;
                case "S":
                    a.setAddressType(AddressTypeEnum.SHIPPING);
                    break;
                case "O":
                    a.setAddressType(AddressTypeEnum.OFFICE);
                    break;
                default:
                    // If full enum name is provided
                    a.setAddressType(AddressTypeEnum.valueOf(r.getAddressType().toUpperCase()));
            }
        }

        a.setShiptoId(r.getShiptoId());

        // Map directly from request (no conversion needed)
        if (r.getDefaultAddress() != null && !r.getDefaultAddress().isEmpty()) {
            switch (r.getDefaultAddress().toUpperCase()) {
                case "Y":
                    a.setDefaultAddress(DefaultAddressEnum.YES);
                    break;
                case "N":
                    a.setDefaultAddress(DefaultAddressEnum.NO);
                    break;
                default:
                    // If full enum name is provided
                    a.setDefaultAddress(DefaultAddressEnum.valueOf(r.getDefaultAddress().toUpperCase()));
            }
        }

        a.setAddress(r.getAddress());
        a.setAddressContact(r.getAddressContact());
        a.setAddressEmail(r.getAddressEmail());
        a.setAddressTelephone(r.getAddressTelephone());
        a.setAddressName(r.getAddressName());
        a.setAddressProvinceCode(r.getAddressProvinceCode());
        a.setCreatedBy(r.getCreatedBy());
        a.setCreationTime(r.getCreationTime());
        a.setModifiedBy(r.getModifiedBy());
        a.setModificationTime(r.getModificationTime());

        // Map directly from request (no conversion needed)
        if (r.getDeleteStatus() != null && !r.getDeleteStatus().isEmpty()) {
            switch (r.getDeleteStatus().toUpperCase()) {
                case "A":
                    a.setDeleteStatus(StatusEnum.ACTIVE);
                    break;
                case "I":
                    a.setDeleteStatus(StatusEnum.INACTIVE);
                    break;
                case "D":
                    a.setDeleteStatus(StatusEnum.DELETED);
                    break;
                default:
                    // If full enum name is provided
                    a.setDeleteStatus(StatusEnum.valueOf(r.getDeleteStatus().toUpperCase()));
            }
        } else {
            a.setDeleteStatus(StatusEnum.ACTIVE);
        }

        return a;
    }

    private CompanyAddressResponseDTO mapToResponse(CompanyAddress a) {
        CompanyAddressResponseDTO r = new CompanyAddressResponseDTO();

        r.setAddressId(a.getAddressId());
        r.setUserSapId(a.getUserSapId());
        r.setAddressCountryCode(a.getAddressCountryCode());
        r.setAddressProvince(a.getAddressProvince());
        r.setAddressCity(a.getAddressCity());
        r.setAddressStreet(a.getAddressStreet());
        r.setAddressZipcode(a.getAddressZipcode());

        // Return full enum name
        r.setAddressType(a.getAddressType() != null ? a.getAddressType().name() : null);
        r.setShiptoId(a.getShiptoId());
        r.setDefaultAddress(a.getDefaultAddress() != null ? a.getDefaultAddress().name() : null);
        r.setAddress(a.getAddress());
        r.setAddressContact(a.getAddressContact());
        r.setAddressEmail(a.getAddressEmail());
        r.setAddressTelephone(a.getAddressTelephone());
        r.setAddressName(a.getAddressName());
        r.setAddressProvinceCode(a.getAddressProvinceCode());
        r.setCreatedBy(a.getCreatedBy());
        r.setCreationTime(a.getCreationTime());
        r.setModifiedBy(a.getModifiedBy());
        r.setModificationTime(a.getModificationTime());
        r.setDeleteStatus(a.getDeleteStatus() != null ? a.getDeleteStatus().name() : null);

        return r;
    }
}