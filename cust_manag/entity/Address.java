package com.pramaindia.cust_manag.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long addressId;
    private String userSapId;
    private String shiptoId;
    private String addressCountryCode;
    private String addressProvinceCode;
    private String addressProvince;
    private String addressStreet;
    private String address;
    private String addressCipher;
    private String addressContact;
    private String addressContactCipher;
    private String addressEmail;
    private String addressEmailCipher;
    private String addressTelphone;
    private String addressTelphoneCipher;
    private String addressName;
    private String addressNameCipher;
    private String addressZipcode;
    private String addressCity;
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    private String modifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modificationTime;

    private String deleteStatus;
    private String addressType;
    private String saleOrganizeId;
    private String isDefault;
    private String countryName;
    private String userName;
}