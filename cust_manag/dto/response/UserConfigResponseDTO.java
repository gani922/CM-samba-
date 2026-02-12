package com.pramaindia.cust_manag.dto.response;

import lombok.Data;

@Data
public class UserConfigResponseDTO {
    private Boolean openUserMaxOrder;
    private Integer maxOrderNum;
}