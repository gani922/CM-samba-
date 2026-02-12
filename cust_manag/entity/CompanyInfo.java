package com.pramaindia.cust_manag.entity;

import com.pramaindia.cust_manag.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMPANY_INFO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfo {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "BU_ID", nullable = false)
    private Long buId;

    @Column(name = "COUNTRY_ID", nullable = false)
    private Long countryId;

    @Column(name = "COMPANY_NAME", nullable = false, length = 800)
    private String companyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "DELETE_STATUS", nullable = false, length = 1)
    private DeleteStatusEnum deleteStatus = DeleteStatusEnum.A;

    @Enumerated(EnumType.STRING)
    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private IsActiveEnum isActive = IsActiveEnum.Y;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "CREATION_TIME", nullable = false)
    private LocalDateTime creationTime = LocalDateTime.now();

    @Column(name = "MODIFIED_BY", length = 50)
    private String modifiedBy;

    @Column(name = "MODIFICATION_TIME")
    private LocalDateTime modificationTime;

    @Column(name = "PROVINCE", length = 128)
    private String province;

    @Column(name = "PHONE", length = 256)
    private String phone;

    @Column(name = "ADDRESS", length = 1500)
    private String address;

    @Column(name = "ADDRESS2", length = 1500)
    private String address2;

    @Column(name = "ZIPCODE", length = 32)
    private String zipcode;

    @Column(name = "TAX_ID", length = 32)
    private String taxId;

    @Column(name = "VERTICAL_INDUSTRY_TYPE", length = 64)
    private String verticalIndustryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 1)
    private StatusEnum status;

    @Column(name = "SAP_ID", length = 50)
    private String sapId;

    @Column(name = "LOCK_REASON", length = 1000)
    private String lockReason;

    @Column(name = "EMAIL", length = 256)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "LOCATION", length = 2)
    private LocationEnum location;

    @Enumerated(EnumType.STRING)
    @Column(name = "MOUNTHLY_TURNOVER", length = 2)
    private MonthlyTurnoverEnum monthlyTurnover;

    @Enumerated(EnumType.STRING)
    @Column(name = "PANNO_TYPE", length = 2)
    private PannoTypeEnum pannoType;

    @Column(name = "PANNO", length = 256)
    private String panno;

    @Column(name = "COMPANY_TYPE", length = 10)
    private String companyType;

    @Column(name = "ADDITION_BUSINESS_TYPE", length = 10)
    private String additionBusinessType;

    @Column(name = "SUB_BU_ID")
    private Long subBuId;

    @Column(name = "CITY", length = 100)
    private String city;

    @Column(name = "STATE_NOTE", length = 100)
    private String stateNote;

    @Column(name = "CITY_NOTE", length = 100)
    private String cityNote;

    @Column(name = "SALE_GROUP", length = 50)
    private String saleGroup;

    @Column(name = "USER_GROUP", length = 50)
    private String userGroup;

    @Column(name = "USER_GRADE", length = 50)
    private String userGrade;

    @Column(name = "USER_AREA", length = 50)
    private String userArea;

    @Column(name = "REGION_ID", length = 50)
    private String regionId;

    @Column(name = "DISTRICT", length = 200)
    private String district;

    @Column(name = "DISTRICT_ID", length = 50)
    private String districtId;

    @Enumerated(EnumType.STRING)
    @Column(name = "SALE_CHANNEL", length = 1)
    private SaleChannelEnum saleChannel;

    @Column(name = "HANA_SAP_ID", length = 50)
    private String hanaSapId;

    @Column(name = "SPECIAL_SUPPORT_TIME_START")
    private Integer specialSupportTimeStart;

    @Column(name = "SPECIAL_SUPPORT_TIME_END")
    private Integer specialSupportTimeEnd;

    @Column(name = "BRANCH_COMPANY", length = 20)
    private String branchCompany;

    @Column(name = "FORCE_KYC_POPUP")
    private Integer forceKycPopup;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_DEACTIVE", length = 1)
    private AccountDeactiveEnum accountDeactive = AccountDeactiveEnum.N;

    @Lob
    @Column(name = "ACCOUNT_DEACTIVE_INFO")
    private String accountDeactiveInfo;
}