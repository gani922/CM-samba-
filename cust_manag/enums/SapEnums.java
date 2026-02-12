package com.pramaindia.cust_manag.enums;

public enum SapEnums {

    // User Status
    USER_STATUS_ACTIVE("1", "Active"),
    USER_STATUS_INACTIVE("0", "Inactive"),
    USER_STATUS_LOCKED("Y", "Locked"),
    USER_STATUS_UNLOCKED("N", "Unlocked"),

    // Delete Status
    DELETE_STATUS_ACTIVE("0", "Not Deleted"),
    DELETE_STATUS_DELETED("1", "Deleted"),

    // Address Type
    ADDRESS_TYPE_BILLING("0", "Billing Address"),
    ADDRESS_TYPE_SHIPPING("1", "Shipping Address"),

    // SAP User Group
    USER_GROUP_DISTRIBUTOR("003", "Distributor"),
    USER_GROUP_RESELLER("004", "Reseller"),

    // Sale Channel
    SALE_CHANNEL_DIRECT("0", "Direct"),
    SALE_CHANNEL_INDIRECT("1", "Indirect"),

    // Auth Law
    AUTH_LAW_NO("0", "No Authorization Required"),
    AUTH_LAW_YES("1", "Authorization Required"),

    // User Level
    USER_LEVEL_BASIC("1", "Basic User"),
    USER_LEVEL_ADVANCED("2", "Advanced User"),
    USER_LEVEL_ADMIN("3", "Administrator"),

    // Short Message
    SHORT_MESSAGE_DISABLED("0", "SMS Disabled"),
    SHORT_MESSAGE_ENABLED("1", "SMS Enabled"),

    // User Stock Show
    STOCK_SHOW_DISABLED("0", "Stock Hidden"),
    STOCK_SHOW_ENABLED("1", "Stock Visible"),

    // Download Stock List
    DOWNLOAD_STOCK_DISABLED("0", "Download Disabled"),
    DOWNLOAD_STOCK_ENABLED("1", "Download Enabled"),

    // Add Coupons
    ADD_COUPONS_DISABLED("0", "Add Coupons Disabled"),
    ADD_COUPONS_ENABLED("1", "Add Coupons Enabled"),

    // Show Coupons Windows
    SHOW_COUPONS_DISABLED("0", "Coupons Window Hidden"),
    SHOW_COUPONS_ENABLED("1", "Coupons Window Visible"),

    // Add Flash Sales
    ADD_FLASH_SALES_DISABLED("0", "Flash Sales Disabled"),
    ADD_FLASH_SALES_ENABLED("1", "Flash Sales Enabled"),

    // Reset Device Password
    RESET_DEVICE_PASSWORD_DISABLED("0", "Reset Disabled"),
    RESET_DEVICE_PASSWORD_ENABLED("1", "Reset Enabled"),

    // User Address Lock
    ADDRESS_LOCK_DISABLED("0", "Address Editable"),
    ADDRESS_LOCK_ENABLED("1", "Address Locked"),

    // Is Default Address
    DEFAULT_ADDRESS_NO("0", "Not Default"),
    DEFAULT_ADDRESS_YES("1", "Default");

    private final String code;
    private final String description;

    SapEnums(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionByCode(String code) {
        for (SapEnums value : values()) {
            if (value.getCode().equals(code)) {
                return value.getDescription();
            }
        }
        return "Unknown";
    }
}