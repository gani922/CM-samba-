package com.pramaindia.cust_manag.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyAddressRequestDTO {

    @NotNull(message = "Address ID is mandatory")
    private Long addressId;

    @Size(max = 50, message = "User SAP ID must not exceed 50 characters")
    private String userSapId;

    @Size(max = 10, message = "Country code must not exceed 10 characters")
    private String addressCountryCode;

    @Size(max = 50, message = "Province must not exceed 50 characters")
    private String addressProvince;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String addressCity;

    @Size(max = 100, message = "Street must not exceed 100 characters")
    private String addressStreet;

    @Size(max = 10, message = "Zipcode must not exceed 10 characters")
    private String addressZipcode;

    @Pattern(regexp = "B|S|O|BILLING|SHIPPING|OFFICE",
            message = "Address type must be B, S, O or BILLING, SHIPPING, OFFICE")
    private String addressType;

    @Size(max = 50, message = "ShipTo ID must not exceed 50 characters")
    private String shiptoId;

    @Pattern(regexp = "Y|N|YES|NO",
            message = "Default address must be Y, N or YES, NO")
    private String defaultAddress;

    @Size(max = 2000, message = "Address must not exceed 2000 characters")
    private String address;

    @Size(max = 500, message = "Contact must not exceed 500 characters")
    private String addressContact;

    @Size(max = 1000, message = "Email must not exceed 1000 characters")
    private String addressEmail;

    @Size(max = 1000, message = "Telephone must not exceed 1000 characters")
    private String addressTelephone;

    @Size(max = 1000, message = "Address name must not exceed 1000 characters")
    private String addressName;

    @Size(max = 50, message = "Province code must not exceed 50 characters")
    private String addressProvinceCode;

    @NotBlank(message = "Created by is mandatory")
    @Size(max = 50, message = "Created by must not exceed 50 characters")
    private String createdBy;

    private LocalDateTime creationTime;

    @Size(max = 50, message = "Modified by must not exceed 50 characters")
    private String modifiedBy;

    private LocalDateTime modificationTime;

    @Pattern(regexp = "A|I|D|ACTIVE|INACTIVE|DELETED",
            message = "Delete status must be A, I, D or ACTIVE, INACTIVE, DELETED")
    private String deleteStatus;
}