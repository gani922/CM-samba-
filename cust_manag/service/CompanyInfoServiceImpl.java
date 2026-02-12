package com.pramaindia.cust_manag.service;

import com.pramaindia.cust_manag.dao.CompanyInfoDAO;
import com.pramaindia.cust_manag.dto.request.CompanyInfoRequestDTO;
import com.pramaindia.cust_manag.dto.response.CompanyInfoResponseDTO;
import com.pramaindia.cust_manag.entity.CompanyInfo;
import com.pramaindia.cust_manag.enums.*;
import com.pramaindia.customer_management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyInfoServiceImpl implements CompanyInfoService {

    private final CompanyInfoDAO companyInfoDAO;

    @Override
    public CompanyInfoResponseDTO createCompany(CompanyInfoRequestDTO request) {
        // Check if company with same name already exists
        Integer count = companyInfoDAO.countByCompanyName(request.getCompanyName());
        if (count > 0) {
            throw new ResourceNotFoundException("Company with name '" + request.getCompanyName() + "' already exists");
        }

        CompanyInfo company = mapToEntity(request);

        // Generate company ID if not provided
        if (company.getId() == null) {
            Long maxId = companyInfoDAO.getMaxCompanyId();
            company.setId(maxId == null ? 1L : maxId + 1);
        }

        companyInfoDAO.insert(company);
        return mapToResponse(company);
    }

    @Override
    public List<CompanyInfoResponseDTO> getAllCompanies() {
        return companyInfoDAO.selectAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CompanyInfoResponseDTO getCompanyById(Long id) {
        CompanyInfo company = companyInfoDAO.selectByPrimaryKey(id);
        if (company == null) {
            throw new ResourceNotFoundException("Company with ID " + id + " not found");
        }
        return mapToResponse(company);
    }

    @Override
    public CompanyInfoResponseDTO getCompanyBySapId(String sapId) {
        CompanyInfo company = companyInfoDAO.findBySapId(sapId);
        if (company == null) {
            throw new ResourceNotFoundException("Company with SAP ID " + sapId + " not found");
        }
        return mapToResponse(company);
    }

    @Override
    public CompanyInfoResponseDTO updateCompany(Long id, CompanyInfoRequestDTO request) {
        CompanyInfo existing = companyInfoDAO.selectByPrimaryKey(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Company with ID " + id + " not found");
        }

        CompanyInfo updated = mapToEntity(request);
        updated.setId(id);
        companyInfoDAO.updateByPrimaryKeySelective(updated);

        return mapToResponse(companyInfoDAO.selectByPrimaryKey(id));
    }

    @Override
    public void deleteCompany(Long id) {
        CompanyInfo company = companyInfoDAO.selectByPrimaryKey(id);
        if (company == null) {
            throw new ResourceNotFoundException("Company with ID " + id + " not found");
        }

        // Soft delete by updating status
        company.setDeleteStatus(DeleteStatusEnum.D);
        companyInfoDAO.updateByPrimaryKeySelective(company);
    }

    @Override
    public List<CompanyInfoResponseDTO> searchCompaniesByName(String companyName) {
        // This would need a custom mapper method
        // For now, filter from all companies
        return companyInfoDAO.selectAll()
                .stream()
                .filter(company -> company.getCompanyName().toLowerCase().contains(companyName.toLowerCase()))
                .map(this::mapToResponse)
                .toList();
    }

    private CompanyInfo mapToEntity(CompanyInfoRequestDTO r) {
        CompanyInfo c = new CompanyInfo();

        c.setId(r.getId());
        c.setBuId(r.getBuId());
        c.setCountryId(r.getCountryId());
        c.setCompanyName(r.getCompanyName());

        if (r.getDeleteStatus() != null && !r.getDeleteStatus().isEmpty()) {
            c.setDeleteStatus(DeleteStatusEnum.valueOf(r.getDeleteStatus()));
        }

        if (r.getIsActive() != null && !r.getIsActive().isEmpty()) {
            c.setIsActive(IsActiveEnum.valueOf(r.getIsActive()));
        }

        c.setCreatedBy(r.getCreatedBy());
        c.setCreationTime(r.getCreationTime());
        c.setModifiedBy(r.getModifiedBy());
        c.setModificationTime(r.getModificationTime());
        c.setProvince(r.getProvince());
        c.setPhone(r.getPhone());
        c.setAddress(r.getAddress());
        c.setAddress2(r.getAddress2());
        c.setZipcode(r.getZipcode());
        c.setTaxId(r.getTaxId());
        c.setVerticalIndustryType(r.getVerticalIndustryType());

        if (r.getStatus() != null && !r.getStatus().isEmpty()) {
            c.setStatus(StatusEnum.valueOf(r.getStatus()));
        }

        c.setSapId(r.getSapId());
        c.setLockReason(r.getLockReason());
        c.setEmail(r.getEmail());

        if (r.getLocation() != null && !r.getLocation().isEmpty()) {
            c.setLocation(LocationEnum.valueOf(r.getLocation()));
        }

        if (r.getMonthlyTurnover() != null && !r.getMonthlyTurnover().isEmpty()) {
            c.setMonthlyTurnover(MonthlyTurnoverEnum.valueOf(r.getMonthlyTurnover()));
        }

        if (r.getPannoType() != null && !r.getPannoType().isEmpty()) {
            c.setPannoType(PannoTypeEnum.valueOf(r.getPannoType()));
        }

        c.setPanno(r.getPanno());
        c.setCompanyType(r.getCompanyType());
        c.setAdditionBusinessType(r.getAdditionBusinessType());
        c.setSubBuId(r.getSubBuId());
        c.setCity(r.getCity());
        c.setStateNote(r.getStateNote());
        c.setCityNote(r.getCityNote());
        c.setSaleGroup(r.getSaleGroup());
        c.setUserGroup(r.getUserGroup());
        c.setUserGrade(r.getUserGrade());
        c.setUserArea(r.getUserArea());
        c.setRegionId(r.getRegionId());
        c.setDistrict(r.getDistrict());
        c.setDistrictId(r.getDistrictId());

        if (r.getSaleChannel() != null && !r.getSaleChannel().isEmpty()) {
            c.setSaleChannel(SaleChannelEnum.valueOf(r.getSaleChannel()));
        }

        c.setHanaSapId(r.getHanaSapId());
        c.setSpecialSupportTimeStart(r.getSpecialSupportTimeStart());
        c.setSpecialSupportTimeEnd(r.getSpecialSupportTimeEnd());
        c.setBranchCompany(r.getBranchCompany());
        c.setForceKycPopup(r.getForceKycPopup());

        if (r.getAccountDeactive() != null && !r.getAccountDeactive().isEmpty()) {
            c.setAccountDeactive(AccountDeactiveEnum.valueOf(r.getAccountDeactive()));
        }

        c.setAccountDeactiveInfo(r.getAccountDeactiveInfo());

        return c;
    }

    private CompanyInfoResponseDTO mapToResponse(CompanyInfo c) {
        CompanyInfoResponseDTO r = new CompanyInfoResponseDTO();

        r.setId(c.getId());
        r.setBuId(c.getBuId());
        r.setCountryId(c.getCountryId());
        r.setCompanyName(c.getCompanyName());
        r.setDeleteStatus(c.getDeleteStatus() != null ? c.getDeleteStatus().name() : null);
        r.setIsActive(c.getIsActive() != null ? c.getIsActive().name() : null);
        r.setCreatedBy(c.getCreatedBy());
        r.setCreationTime(c.getCreationTime());
        r.setModifiedBy(c.getModifiedBy());
        r.setModificationTime(c.getModificationTime());
        r.setProvince(c.getProvince());
        r.setPhone(c.getPhone());
        r.setAddress(c.getAddress());
        r.setAddress2(c.getAddress2());
        r.setZipcode(c.getZipcode());
        r.setTaxId(c.getTaxId());
        r.setVerticalIndustryType(c.getVerticalIndustryType());
        r.setStatus(c.getStatus() != null ? c.getStatus().name() : null);
        r.setSapId(c.getSapId());
        r.setLockReason(c.getLockReason());
        r.setEmail(c.getEmail());
        r.setLocation(c.getLocation() != null ? c.getLocation().name() : null);
        r.setMonthlyTurnover(c.getMonthlyTurnover() != null ? c.getMonthlyTurnover().name() : null);
        r.setPannoType(c.getPannoType() != null ? c.getPannoType().name() : null);
        r.setPanno(c.getPanno());
        r.setCompanyType(c.getCompanyType());
        r.setAdditionBusinessType(c.getAdditionBusinessType());
        r.setSubBuId(c.getSubBuId());
        r.setCity(c.getCity());
        r.setStateNote(c.getStateNote());
        r.setCityNote(c.getCityNote());
        r.setSaleGroup(c.getSaleGroup());
        r.setUserGroup(c.getUserGroup());
        r.setUserGrade(c.getUserGrade());
        r.setUserArea(c.getUserArea());
        r.setRegionId(c.getRegionId());
        r.setDistrict(c.getDistrict());
        r.setDistrictId(c.getDistrictId());
        r.setSaleChannel(c.getSaleChannel() != null ? c.getSaleChannel().name() : null);
        r.setHanaSapId(c.getHanaSapId());
        r.setSpecialSupportTimeStart(c.getSpecialSupportTimeStart());
        r.setSpecialSupportTimeEnd(c.getSpecialSupportTimeEnd());
        r.setBranchCompany(c.getBranchCompany());
        r.setForceKycPopup(c.getForceKycPopup());
        r.setAccountDeactive(c.getAccountDeactive() != null ? c.getAccountDeactive().name() : null);
        r.setAccountDeactiveInfo(c.getAccountDeactiveInfo());

        return r;
    }
}