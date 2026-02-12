package com.pramaindia.login.model.entity;

import lombok.Data;

@Data
public class SsoUserEntity {
    private String email;
    private String firstName;
    private String lastName;
    private String companySapId;
}