package com.pramaindia.cust_manag.service;


import com.pramaindia.cust_manag.dto.request.CompanyAddressRequestDTO;
import com.pramaindia.cust_manag.dto.response.CompanyAddressResponseDTO;

import java.util.List;

public interface CompanyAddressService {

    List<CompanyAddressResponseDTO> getAllAddresses();

    CompanyAddressResponseDTO getAddressById(Long addressId);

    CompanyAddressResponseDTO createAddress(CompanyAddressRequestDTO request);

    CompanyAddressResponseDTO updateAddress(Long addressId, CompanyAddressRequestDTO request);

    void deleteAddress(Long addressId);

    CompanyAddressResponseDTO getDefaultAddressByUserSapId(String userSapId);
}