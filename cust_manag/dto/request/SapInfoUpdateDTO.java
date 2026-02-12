package com.pramaindia.cust_manag.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class SapInfoUpdateDTO {

    @NotBlank(message = "User SAP ID is required")
    private String userSapId;

    @Size(max = 100, message = "User name must not exceed 100 characters")
    private String userName;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String userEmail;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Mobile number must be 10-15 digits")
    private String userMobile;

    @Size(max = 10, message = "SAP user group must not exceed 10 characters")
    private String sapUserGroup;

    @Size(max = 50, message = "Region must not exceed 50 characters")
    private String region;

    @Size(max = 20, message = "Region ID must not exceed 20 characters")
    private String regionId;

    @Size(max = 20, message = "PAN number must not exceed 20 characters")
    private String panNumber;

    @Size(max = 50, message = "GST number must not exceed 50 characters")
    private String gstNumber;

    private String isLock;
    private String userStatus;

    @Size(max = 50, message = "Modified by must not exceed 50 characters")
    private String modifiedBy;

    private Date startValidDate;
    private Date endValidDate;
    private Date specialSupportTimeStart;
    private Date specialSupportTimeEnd;

    private String userConfig;
}