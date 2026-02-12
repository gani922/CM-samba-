package com.pramaindia.cust_manag.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyAddressResponseDTO {

    private Long addressId;
    private String userSapId;
    private String addressCountryCode;
    private String addressProvince;
    private String addressCity;
    private String addressStreet;
    private String addressZipcode;
    private String addressType;
    private String shiptoId;
    private String defaultAddress;
    private String address;
    private String addressContact;
    private String addressEmail;
    private String addressTelephone;
    private String addressName;
    private String addressProvinceCode;
    private String createdBy;
    private LocalDateTime creationTime;
    private String modifiedBy;
    private LocalDateTime modificationTime;
    private String deleteStatus;
}