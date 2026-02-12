package com.pramaindia.cust_manag.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AddressResponseDTO {
    private Long addressId;
    private String userSapId;
    private String shiptoId;
    private String addressCountryCode;
    private String addressProvinceCode;
    private String addressProvince;
    private String address;
    private String addressCipher;
    private String addressContact;
    private String addressEmail;
    private String addressTelphone;
    private String addressName;
    private String addressZipcode;
    private String addressCity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modificationTime;

    private String addressType;
    private String isDefault;
    private String countryName;

    // Additional fields for UI
    private String addressTypeDesc;
    private String isDefaultDesc;
}