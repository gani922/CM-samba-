package com.pramaindia.role_management.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum PortalUserRoleType {

    USER_DEFINED(0, "User Defined"),
    DISTRIBUTOR_COMPANY_ADMIN(1, "Distributor Company Admin"),
    DISTRIBUTOR_COMPANY_USER(2, "Distributor Company User"),
    DPP_COMPANY_ADMIN(3, "DPP Company Admin"),
    DPP_COMPANY_USER(4, "DPP Company User"),
    NO_DPP_COMPANY_ADMIN(5, "NonPartnerAcct Admin"),
    NO_DPP_COMPANY_USER(6, "NonPartnerAcct User"),
    CONSULTANT_COMPANY_ADMIN(7, "Consultant Company Admin"),
    CONSULTANT_COMPANY_USER(8, "Consultant Company User"),
    B2B_ADMIN(9, "B2B Admin"),
    B2B_PROCUREMENT(10, "B2B Procurement"),
    B2B_INVENTORY(11, "B2B Inventory"),
    B2B_FINANCE(12, "B2B Finance"),
    UNAUTHORIZED_COMPANY_USER(13, "Unauthorized Company Admin/User"),
    OEM_COMPANY_ADMIN(14, "OEM Company Admin"),
    OEM_COMPANY_USER(15, "OEM Company User"),
    ANONYMOUS(99, "Anonymous User");

    private final int code;
    private final String value;

    PortalUserRoleType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static PortalUserRoleType getByCode(Integer code) {
        if (code == null) {
            return USER_DEFINED;
        }
        for (PortalUserRoleType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return USER_DEFINED;
    }

    public static String getValueByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PortalUserRoleType item : values()) {
            if (item.getCode() == code) {
                return item.getValue();
            }
        }
        return null;
    }

    /**
     * list of all Portal role types (automatically set)
     */
    public static int[] getPortalUserRoleTypeArray() {
        return new int[]{
                DISTRIBUTOR_COMPANY_ADMIN.getCode(), DISTRIBUTOR_COMPANY_USER.getCode(),
                DPP_COMPANY_ADMIN.getCode(), DPP_COMPANY_USER.getCode(), NO_DPP_COMPANY_ADMIN.getCode(),
                NO_DPP_COMPANY_USER.getCode(), CONSULTANT_COMPANY_ADMIN.getCode(), CONSULTANT_COMPANY_USER.getCode(),
                UNAUTHORIZED_COMPANY_USER.getCode(), ANONYMOUS.getCode(),
                OEM_COMPANY_ADMIN.getCode(), OEM_COMPANY_USER.getCode()
        };
    }

    /**
     * Get all role types for dropdown/select options
     */
    public static List<Map<String, Object>> getAllRoleTypes() {
        List<Map<String, Object>> roleTypes = new ArrayList<>();
        for (PortalUserRoleType type : values()) {
            Map<String, Object> roleType = new HashMap<>();
            roleType.put("code", type.getCode());
            roleType.put("name", type.getValue());
            roleType.put("description", type.getValue());
            roleTypes.add(roleType);
        }
        return roleTypes;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
