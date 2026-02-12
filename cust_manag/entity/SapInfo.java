package com.pramaindia.cust_manag.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class SapInfo implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private String userTelphone;
    private String userTelphoneCipher;
    private String userMobile;
    private String userMobileCipher;
    private String userMobileCountryCode;
    private String userMobileCountryCodeCipher;
    private String userSaleSapId;
    private String currencyCode;
    private String languageCode;
    private String userSaleOrg;
    private String userSaleDept;
    private String userSaleGroup;
    private String userCountryCode;
    private String userPassword;
    private String userSalt;
    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationTime;

    private String modifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modificationTime;

    private String deleteStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date passwordModificationTime;

    private String authLaw;
    private String userParentId;
    private String userLevel;
    private String shortMessage;
    private String sapUserGroup;
    private String isResetPassword;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resetPasswordTime;

    private String userGrade;
    private String userArea;
    private String userPhoto;
    private String region;
    private String regionId;
    private String panNumber;
    private String gstNumber;
    private String saleEmail;
    private String saleEmailCipher;
    private String sapPriceGroup;
    private String branch;
    private String userAddressLock;
    private String district;
    private String paymentTerm;
    private String districtId;
    private String paymentTermId;
    private String userSaleName;
    private String branchManagerSapId;
    private String branchManagerName;
    private String reginalManagerSapId;
    private String reginalManagerName;
    private String userStockShow;
    private String downloadStockListShow;
    private String addCoupons;
    private String showCouponsWindows;
    private String saleChannel;
    private String addFlashSales;
    private String userConfig;
    private String resetDevicePassword;
    private String userSapId1;
    private String saleOrganizeId;
    private String salesName;
    private String countryName;
    private String userGroupName;
    private String reason;
    private String dt_RowId;
    private String financeEmail;
    private String purchaseEmail;
    private String userSapIds;
    private String b2bUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialSupportTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialSupportTimeEnd;

    private String groupIds;
    private String customerName;

    // Relationships
    private List<Address> addressList;
    private Address billingAddress;
    private String userGroup;
    private String priceGroup;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    private UserConfigDTO userConfigDTO;
}