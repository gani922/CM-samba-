package com.pramaindia.cust_manag.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class SapInfoRequestDTO {

   // @NotBlank(message = "User SAP ID is required")
    @Size(max = 50, message = "User SAP ID must not exceed 50 characters")
    private String userSapId;

    @Size(max = 100, message = "User name must not exceed 100 characters")
    private String userName;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String userEmail;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Mobile number must be 10-15 digits")
    private String userMobile;

    @Size(max = 10, message = "Country code must not exceed 10 characters")
    private String userMobileCountryCode;

    @Size(max = 10, message = "User level must not exceed 10 characters")
    private String userLevel;

    @Size(max = 10, message = "SAP user group must not exceed 10 characters")
    private String sapUserGroup;

    @Size(max = 10, message = "User grade must not exceed 10 characters")
    private String userGrade;

    @Size(max = 50, message = "Region must not exceed 50 characters")
    private String region;

    @Size(max = 20, message = "Region ID must not exceed 20 characters")
    private String regionId;

    @Size(max = 20, message = "PAN number must not exceed 20 characters")
    private String panNumber;

    @Size(max = 50, message = "GST number must not exceed 50 characters")
    private String gstNumber;

    @Size(max = 50, message = "District must not exceed 50 characters")
    private String district;

    @Size(max = 20, message = "District ID must not exceed 20 characters")
    private String districtId;

    @Size(max = 100, message = "User sale name must not exceed 100 characters")
    private String userSaleName;

    private String saleChannel;
    private String addCoupons;
    private String showCouponsWindows;
    private String addFlashSales;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startValidDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endValidDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialSupportTimeStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date specialSupportTimeEnd;

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String orderBy = "creation_time desc";

    // Search parameters
    private String searchKeyword;
    private String userStatus;
    private String isLock;
    private String companyType;
    private String sapIdFlag;
}