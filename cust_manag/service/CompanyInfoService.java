package com.pramaindia.cust_manag.service;


import com.pramaindia.cust_manag.dto.request.CompanyInfoRequestDTO;
import com.pramaindia.cust_manag.dto.response.CompanyInfoResponseDTO;

import java.util.List;

public interface CompanyInfoService {

    List<CompanyInfoResponseDTO> getAllCompanies();

    CompanyInfoResponseDTO getCompanyById(Long id);

    CompanyInfoResponseDTO getCompanyBySapId(String sapId);

    CompanyInfoResponseDTO createCompany(CompanyInfoRequestDTO request);

    CompanyInfoResponseDTO updateCompany(Long id, CompanyInfoRequestDTO request);

    void deleteCompany(Long id);

    List<CompanyInfoResponseDTO> searchCompaniesByName(String companyName);
}