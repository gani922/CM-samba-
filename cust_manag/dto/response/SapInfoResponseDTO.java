package com.pramaindia.cust_manag.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SapInfoResponseDTO {
    private String userId;
    private String userSapId;
    private String isLock;
    private String userName;
    private String userNameCipher;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startValidDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endValidDate;

    private String userEmail;
    private String userEmailCipher;
    private String userMobile;
    private String userMobileCountryCode;
    private String userSaleSapId;
    private String currencyCode;
    private String userSaleOrg;
    private String userSaleGroup;
    private String userCountryCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modificationTime;

    private String userLevel;
    private String sapUserGroup;
    private String userGrade;
    private String userArea;
    private String region;
    private String regionId;
    private String panNumber;
    private String gstNumber;
    private String saleEmail;
    private String branch;
    private String userAddressLock;
    private String district;
    private String paymentTerm;
    private String districtId;
    private String paymentTermId;
    private String userSaleName;
    private String userStockShow;
    private String downloadStockListShow;
    private String addCoupons;
    private String showCouponsWindows;
    private String saleChannel;
    private String addFlashSales;
    private String userConfig;
    private String resetDevicePassword;
    private String customerName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    private String financeEmail;
    private String purchaseEmail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialSupportTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialSupportTimeEnd;

    private List<AddressResponseDTO> addressList;
    private AddressResponseDTO billingAddress;
    private UserConfigResponseDTO userConfigDTO;

    // Additional fields for UI
    private String lockStatusDesc;
    private String userLevelDesc;
    private String sapUserGroupDesc;
    private String saleChannelDesc;
    private String statusDesc;
}