package com.pramaindia.cust_manag.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean openUserMaxOrder;
    private Integer maxOrderNum;
}