package com.pramaindia.cust_manag.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyInfoResponseDTO {

    private Long id;
    private Long buId;
    private Long countryId;
    private String companyName;
    private String deleteStatus;
    private String isActive;
    private String createdBy;
    private LocalDateTime creationTime;
    private String modifiedBy;
    private LocalDateTime modificationTime;
    private String province;
    private String phone;
    private String address;
    private String address2;
    private String zipcode;
    private String taxId;
    private String verticalIndustryType;
    private String status;
    private String sapId;
    private String lockReason;
    private String email;
    private String location;
    private String monthlyTurnover;
    private String pannoType;
    private String panno;
    private String companyType;
    private String additionBusinessType;
    private Long subBuId;
    private String city;
    private String stateNote;
    private String cityNote;
    private String saleGroup;
    private String userGroup;
    private String userGrade;
    private String userArea;
    private String regionId;
    private String district;
    private String districtId;
    private String saleChannel;
    private String hanaSapId;
    private Integer specialSupportTimeStart;
    private Integer specialSupportTimeEnd;
    private String branchCompany;
    private Integer forceKycPopup;
    private String accountDeactive;
    private String accountDeactiveInfo;
}