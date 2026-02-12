package com.pramaindia.cust_manag.entity;

import com.pramaindia.cust_manag.enums.AddressTypeEnum;
import com.pramaindia.cust_manag.enums.DefaultAddressEnum;
import com.pramaindia.cust_manag.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMPANY_ADDRESS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAddress {

    @Id
    @Column(name = "ADDRESS_ID")
    private Long addressId;

    @Column(name = "USER_SAP_ID", length = 50)
    private String userSapId;

    @Column(name = "ADDRESS_COUNTRY_CODE", length = 10)
    private String addressCountryCode;

    @Column(name = "ADDRESS_PROVINCE", length = 50)
    private String addressProvince;

    @Column(name = "ADDRESS_CITY", length = 100)
    private String addressCity;

    @Column(name = "ADDRESS_STREET", length = 100)
    private String addressStreet;

    @Column(name = "ADDRESS_ZIPCODE", length = 10)
    private String addressZipcode;

    @Enumerated(EnumType.STRING)
    @Column(name = "ADDRESS_TYPE", length = 1)
    private AddressTypeEnum addressType;

    @Column(name = "SHIPTO_ID", length = 50)
    private String shiptoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEFAULT_ADDRESS", length = 1)
    private DefaultAddressEnum defaultAddress;

    @Column(name = "ADDRESS", length = 2000)
    private String address;

    @Column(name = "ADDRESS_CONTACT", length = 500)
    private String addressContact;

    @Column(name = "ADDRESS_EMAIL", length = 1000)
    private String addressEmail;

    @Column(name = "ADDRESS_TELPHONE", length = 1000)
    private String addressTelephone;

    @Column(name = "ADDRESS_NAME", length = 1000)
    private String addressName;

    @Column(name = "ADDRESS_PROVINCE_CODE", length = 50)
    private String addressProvinceCode;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "CREATION_TIME")
    private LocalDateTime creationTime = LocalDateTime.now();

    @Column(name = "MODIFIED_BY", length = 50)
    private String modifiedBy;

    @Column(name = "MODIFICATION_TIME")
    private LocalDateTime modificationTime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "DELETE_STATUS", length = 1)
    private StatusEnum deleteStatus = StatusEnum.ACTIVE;
}