package com.pramaindia.cust_manag.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyInfoRequestDTO {

    @NotNull(message = "ID is mandatory")
    private Long id;

    @NotNull(message = "BU ID is mandatory")
    private Long buId;

    @NotNull(message = "Country ID is mandatory")
    private Long countryId;

    @NotBlank(message = "Company name is mandatory")
    @Size(max = 800, message = "Company name must not exceed 800 characters")
    private String companyName;

    @Pattern(regexp = "[DA]", message = "Delete status must be D (Deleted) or A (Active)")
    private String deleteStatus;

    @Pattern(regexp = "[YN]", message = "Is active must be Y (Yes) or N (No)")
    private String isActive;

    @Size(max = 50, message = "Created by must not exceed 50 characters")
    private String createdBy;

    private LocalDateTime creationTime;

    @Size(max = 50, message = "Modified by must not exceed 50 characters")
    private String modifiedBy;

    private LocalDateTime modificationTime;

    @Size(max = 128, message = "Province must not exceed 128 characters")
    private String province;

    @Size(max = 256, message = "Phone must not exceed 256 characters")
    private String phone;

    @Size(max = 1500, message = "Address must not exceed 1500 characters")
    private String address;

    @Size(max = 1500, message = "Address2 must not exceed 1500 characters")
    private String address2;

    @Size(max = 32, message = "Zipcode must not exceed 32 characters")
    private String zipcode;

    @Size(max = 32, message = "Tax ID must not exceed 32 characters")
    private String taxId;

    @Size(max = 64, message = "Vertical industry type must not exceed 64 characters")
    private String verticalIndustryType;

    @Pattern(regexp = "[AIS]", message = "Status must be A (Active), I (Inactive), or S (Suspended)")
    private String status;

    @Size(max = 50, message = "SAP ID must not exceed 50 characters")
    private String sapId;

    @Size(max = 1000, message = "Lock reason must not exceed 1000 characters")
    private String lockReason;

    @Size(max = 256, message = "Email must not exceed 256 characters")
    private String email;

    @Pattern(regexp = "[UR]", message = "Location must be U (Urban) or R (Rural)")
    private String location;

    @Pattern(regexp = "[LMH]", message = "Monthly turnover must be L (Low), M (Medium), or H (High)")
    private String monthlyTurnover;

    @Pattern(regexp = "[IC]", message = "PANNO type must be I (Individual) or C (Company)")
    private String pannoType;

    @Size(max = 256, message = "PANNO must not exceed 256 characters")
    private String panno;

    @Size(max = 10, message = "Company type must not exceed 10 characters")
    private String companyType;

    @Size(max = 10, message = "Addition business type must not exceed 10 characters")
    private String additionBusinessType;

    private Long subBuId;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State note must not exceed 100 characters")
    private String stateNote;

    @Size(max = 100, message = "City note must not exceed 100 characters")
    private String cityNote;

    @Size(max = 50, message = "Sale group must not exceed 50 characters")
    private String saleGroup;

    @Size(max = 50, message = "User group must not exceed 50 characters")
    private String userGroup;

    @Size(max = 50, message = "User grade must not exceed 50 characters")
    private String userGrade;

    @Size(max = 50, message = "User area must not exceed 50 characters")
    private String userArea;

    @Size(max = 50, message = "Region ID must not exceed 50 characters")
    private String regionId;

    @Size(max = 200, message = "District must not exceed 200 characters")
    private String district;

    @Size(max = 50, message = "District ID must not exceed 50 characters")
    private String districtId;

    @Pattern(regexp = "[DIO]", message = "Sale channel must be D (Direct), I (Indirect), or O (Online)")
    private String saleChannel;

    @Size(max = 50, message = "HANA SAP ID must not exceed 50 characters")
    private String hanaSapId;

    private Integer specialSupportTimeStart;

    private Integer specialSupportTimeEnd;

    @Size(max = 20, message = "Branch company must not exceed 20 characters")
    private String branchCompany;

    private Integer forceKycPopup;

    @Pattern(regexp = "[YN]", message = "Account deactive must be Y (Yes) or N (No)")
    private String accountDeactive;

    private String accountDeactiveInfo;
}