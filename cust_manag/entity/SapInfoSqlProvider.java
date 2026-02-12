package com.pramaindia.cust_manag.entity;


import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class SapInfoSqlProvider {

    public String insert(SapInfo sapInfo) {
        return new SQL() {{
            INSERT_INTO("sap_info");

            if (sapInfo.getUserSapId() != null) {
                VALUES("user_sap_id", "#{userSapId}");
            }
            if (sapInfo.getIsLock() != null) {
                VALUES("is_lock", "#{isLock}");
            }
            if (sapInfo.getUserName() != null) {
                VALUES("user_name", "#{userName}");
            }
            if (sapInfo.getUserNameCipher() != null) {
                VALUES("user_name_cipher", "#{userNameCipher}");
            }
            if (sapInfo.getStartValidDate() != null) {
                VALUES("start_valid_date", "#{startValidDate}");
            }
            if (sapInfo.getEndValidDate() != null) {
                VALUES("end_valid_date", "#{endValidDate}");
            }
            if (sapInfo.getUserEmail() != null) {
                VALUES("user_email", "#{userEmail}");
            }
            if (sapInfo.getUserEmailCipher() != null) {
                VALUES("user_email_cipher", "#{userEmailCipher}");
            }
            if (sapInfo.getUserTelphone() != null) {
                VALUES("user_telphone", "#{userTelphone}");
            }
            if (sapInfo.getUserTelphoneCipher() != null) {
                VALUES("user_telphone_cipher", "#{userTelphoneCipher}");
            }
            if (sapInfo.getUserMobile() != null) {
                VALUES("user_mobile", "#{userMobile}");
            }
            if (sapInfo.getUserMobileCipher() != null) {
                VALUES("user_mobile_cipher", "#{userMobileCipher}");
            }
            if (sapInfo.getUserMobileCountryCode() != null) {
                VALUES("user_mobile_country_code", "#{userMobileCountryCode}");
            }
            if (sapInfo.getUserMobileCountryCodeCipher() != null) {
                VALUES("user_mobile_country_code_cipher", "#{userMobileCountryCodeCipher}");
            }
            if (sapInfo.getUserSaleSapId() != null) {
                VALUES("user_sale_sap_id", "#{userSaleSapId}");
            }
            if (sapInfo.getCurrencyCode() != null) {
                VALUES("currency_code", "#{currencyCode}");
            }
            if (sapInfo.getLanguageCode() != null) {
                VALUES("language_code", "#{languageCode}");
            }
            if (sapInfo.getUserSaleOrg() != null) {
                VALUES("user_sale_org", "#{userSaleOrg}");
            }
            if (sapInfo.getUserSaleDept() != null) {
                VALUES("user_sale_dept", "#{userSaleDept}");
            }
            if (sapInfo.getUserSaleGroup() != null) {
                VALUES("user_sale_group", "#{userSaleGroup}");
            }
            if (sapInfo.getUserCountryCode() != null) {
                VALUES("user_country_code", "#{userCountryCode}");
            }
            if (sapInfo.getUserPassword() != null) {
                VALUES("user_password", "#{userPassword}");
            }
            if (sapInfo.getUserSalt() != null) {
                VALUES("user_salt", "#{userSalt}");
            }
            if (sapInfo.getCreatedBy() != null) {
                VALUES("created_by", "#{createdBy}");
            }
            VALUES("creation_time", "NOW()");
            if (sapInfo.getModifiedBy() != null) {
                VALUES("modified_by", "#{modifiedBy}");
            }
            VALUES("modification_time", "NOW()");
            if (sapInfo.getDeleteStatus() != null) {
                VALUES("delete_status", "#{deleteStatus}");
            } else {
                VALUES("delete_status", "'0'");
            }
            if (sapInfo.getPasswordModificationTime() != null) {
                VALUES("password_modification_time", "#{passwordModificationTime}");
            }
            if (sapInfo.getAuthLaw() != null) {
                VALUES("auth_law", "#{authLaw}");
            }
            if (sapInfo.getUserParentId() != null) {
                VALUES("user_parent_id", "#{userParentId}");
            }
            if (sapInfo.getUserLevel() != null) {
                VALUES("user_level", "#{userLevel}");
            }
            if (sapInfo.getShortMessage() != null) {
                VALUES("short_message", "#{shortMessage}");
            }
            if (sapInfo.getSapUserGroup() != null) {
                VALUES("sap_user_group", "#{sapUserGroup}");
            }
            if (sapInfo.getIsResetPassword() != null) {
                VALUES("is_reset_password", "#{isResetPassword}");
            }
            if (sapInfo.getResetPasswordTime() != null) {
                VALUES("reset_password_time", "#{resetPasswordTime}");
            }
            if (sapInfo.getUserGrade() != null) {
                VALUES("user_grade", "#{userGrade}");
            }
            if (sapInfo.getUserArea() != null) {
                VALUES("user_area", "#{userArea}");
            }
            if (sapInfo.getUserPhoto() != null) {
                VALUES("user_photo", "#{userPhoto}");
            }
            if (sapInfo.getRegion() != null) {
                VALUES("region", "#{region}");
            }
            if (sapInfo.getRegionId() != null) {
                VALUES("region_id", "#{regionId}");
            }
            if (sapInfo.getPanNumber() != null) {
                VALUES("pan_number", "#{panNumber}");
            }
            if (sapInfo.getGstNumber() != null) {
                VALUES("gst_number", "#{gstNumber}");
            }
            if (sapInfo.getSaleEmail() != null) {
                VALUES("sale_email", "#{saleEmail}");
            }
            if (sapInfo.getSaleEmailCipher() != null) {
                VALUES("sale_email_cipher", "#{saleEmailCipher}");
            }
            if (sapInfo.getSapPriceGroup() != null) {
                VALUES("sap_price_group", "#{sapPriceGroup}");
            }
            if (sapInfo.getBranch() != null) {
                VALUES("branch", "#{branch}");
            }
            if (sapInfo.getUserAddressLock() != null) {
                VALUES("user_address_lock", "#{userAddressLock}");
            }
            if (sapInfo.getDistrict() != null) {
                VALUES("district", "#{district}");
            }
            if (sapInfo.getPaymentTerm() != null) {
                VALUES("payment_term", "#{paymentTerm}");
            }
            if (sapInfo.getDistrictId() != null) {
                VALUES("district_id", "#{districtId}");
            }
            if (sapInfo.getPaymentTermId() != null) {
                VALUES("payment_term_id", "#{paymentTermId}");
            }
            if (sapInfo.getUserSaleName() != null) {
                VALUES("user_sale_name", "#{userSaleName}");
            }
            if (sapInfo.getBranchManagerSapId() != null) {
                VALUES("branch_manager_sap_id", "#{branchManagerSapId}");
            }
            if (sapInfo.getBranchManagerName() != null) {
                VALUES("branch_manager_name", "#{branchManagerName}");
            }
            if (sapInfo.getReginalManagerSapId() != null) {
                VALUES("reginal_manager_sap_id", "#{reginalManagerSapId}");
            }
            if (sapInfo.getReginalManagerName() != null) {
                VALUES("reginal_manager_name", "#{reginalManagerName}");
            }
            if (sapInfo.getUserStockShow() != null) {
                VALUES("user_stock_show", "#{userStockShow}");
            }
            if (sapInfo.getDownloadStockListShow() != null) {
                VALUES("download_stock_list_show", "#{downloadStockListShow}");
            }
            if (sapInfo.getAddCoupons() != null) {
                VALUES("add_coupons", "#{addCoupons}");
            }
            if (sapInfo.getShowCouponsWindows() != null) {
                VALUES("show_coupons_windows", "#{showCouponsWindows}");
            }
            if (sapInfo.getSaleChannel() != null) {
                VALUES("sale_channel", "#{saleChannel}");
            }
            if (sapInfo.getAddFlashSales() != null) {
                VALUES("add_flash_sales", "#{addFlashSales}");
            }
            if (sapInfo.getUserConfig() != null) {
                VALUES("user_config", "#{userConfig}");
            }
            if (sapInfo.getResetDevicePassword() != null) {
                VALUES("reset_device_password", "#{resetDevicePassword}");
            }
            if (sapInfo.getUserSapId1() != null) {
                VALUES("user_sap_id1", "#{userSapId1}");
            }
            if (sapInfo.getSaleOrganizeId() != null) {
                VALUES("sale_organize_id", "#{saleOrganizeId}");
            }
            if (sapInfo.getSalesName() != null) {
                VALUES("sales_name", "#{salesName}");
            }
            if (sapInfo.getCountryName() != null) {
                VALUES("country_name", "#{countryName}");
            }
            if (sapInfo.getUserGroupName() != null) {
                VALUES("user_group_name", "#{userGroupName}");
            }
            if (sapInfo.getReason() != null) {
                VALUES("reason", "#{reason}");
            }
            if (sapInfo.getDt_RowId() != null) {
                VALUES("dt_row_id", "#{dt_RowId}");
            }
            if (sapInfo.getFinanceEmail() != null) {
                VALUES("finance_email", "#{financeEmail}");
            }
            if (sapInfo.getPurchaseEmail() != null) {
                VALUES("purchase_email", "#{purchaseEmail}");
            }
            if (sapInfo.getUserSapIds() != null) {
                VALUES("user_sap_ids", "#{userSapIds}");
            }
            if (sapInfo.getB2bUser() != null) {
                VALUES("b2b_user", "#{b2bUser}");
            }
            if (sapInfo.getSpecialSupportTimeStart() != null) {
                VALUES("special_support_time_start", "#{specialSupportTimeStart}");
            }
            if (sapInfo.getSpecialSupportTimeEnd() != null) {
                VALUES("special_support_time_end", "#{specialSupportTimeEnd}");
            }
            if (sapInfo.getGroupIds() != null) {
                VALUES("group_ids", "#{groupIds}");
            }
            if (sapInfo.getCustomerName() != null) {
                VALUES("customer_name", "#{customerName}");
            }
            if (sapInfo.getLastLoginTime() != null) {
                VALUES("last_login_time", "#{lastLoginTime}");
            }
        }}.toString();
    }

    public String update(SapInfo sapInfo) {
        return new SQL() {{
            UPDATE("sap_info");

            if (sapInfo.getIsLock() != null) {
                SET("is_lock = #{isLock}");
            }
            if (sapInfo.getUserName() != null) {
                SET("user_name = #{userName}");
            }
            if (sapInfo.getUserNameCipher() != null) {
                SET("user_name_cipher = #{userNameCipher}");
            }
            if (sapInfo.getStartValidDate() != null) {
                SET("start_valid_date = #{startValidDate}");
            }
            if (sapInfo.getEndValidDate() != null) {
                SET("end_valid_date = #{endValidDate}");
            }
            if (sapInfo.getUserEmail() != null) {
                SET("user_email = #{userEmail}");
            }
            if (sapInfo.getUserEmailCipher() != null) {
                SET("user_email_cipher = #{userEmailCipher}");
            }
            if (sapInfo.getUserTelphone() != null) {
                SET("user_telphone = #{userTelphone}");
            }
            if (sapInfo.getUserTelphoneCipher() != null) {
                SET("user_telphone_cipher = #{userTelphoneCipher}");
            }
            if (sapInfo.getUserMobile() != null) {
                SET("user_mobile = #{userMobile}");
            }
            if (sapInfo.getUserMobileCipher() != null) {
                SET("user_mobile_cipher = #{userMobileCipher}");
            }
            if (sapInfo.getUserMobileCountryCode() != null) {
                SET("user_mobile_country_code = #{userMobileCountryCode}");
            }
            if (sapInfo.getUserMobileCountryCodeCipher() != null) {
                SET("user_mobile_country_code_cipher = #{userMobileCountryCodeCipher}");
            }
            if (sapInfo.getUserSaleSapId() != null) {
                SET("user_sale_sap_id = #{userSaleSapId}");
            }
            if (sapInfo.getCurrencyCode() != null) {
                SET("currency_code = #{currencyCode}");
            }
            if (sapInfo.getLanguageCode() != null) {
                SET("language_code = #{languageCode}");
            }
            if (sapInfo.getUserSaleGroup() != null) {
                SET("user_sale_group = #{userSaleGroup}");
            }
            if (sapInfo.getUserPassword() != null) {
                SET("user_password = #{userPassword}");
            }
            if (sapInfo.getUserSalt() != null) {
                SET("user_salt = #{userSalt}");
            }
            if (sapInfo.getModifiedBy() != null) {
                SET("modified_by = #{modifiedBy}");
            }
            SET("modification_time = NOW()");
            if (sapInfo.getPasswordModificationTime() != null) {
                SET("password_modification_time = #{passwordModificationTime}");
            }
            if (sapInfo.getUserLevel() != null) {
                SET("user_level = #{userLevel}");
            }
            if (sapInfo.getSapUserGroup() != null) {
                SET("sap_user_group = #{sapUserGroup}");
            }
            if (sapInfo.getUserGrade() != null) {
                SET("user_grade = #{userGrade}");
            }
            if (sapInfo.getUserArea() != null) {
                SET("user_area = #{userArea}");
            }
            if (sapInfo.getRegion() != null) {
                SET("region = #{region}");
            }
            if (sapInfo.getRegionId() != null) {
                SET("region_id = #{regionId}");
            }
            if (sapInfo.getPanNumber() != null) {
                SET("pan_number = #{panNumber}");
            }
            if (sapInfo.getGstNumber() != null) {
                SET("gst_number = #{gstNumber}");
            }
            if (sapInfo.getSaleEmail() != null) {
                SET("sale_email = #{saleEmail}");
            }
            if (sapInfo.getSapPriceGroup() != null) {
                SET("sap_price_group = #{sapPriceGroup}");
            }
            if (sapInfo.getBranch() != null) {
                SET("branch = #{branch}");
            }
            if (sapInfo.getUserAddressLock() != null) {
                SET("user_address_lock = #{userAddressLock}");
            }
            if (sapInfo.getDistrict() != null) {
                SET("district = #{district}");
            }
            if (sapInfo.getPaymentTerm() != null) {
                SET("payment_term = #{paymentTerm}");
            }
            if (sapInfo.getDistrictId() != null) {
                SET("district_id = #{districtId}");
            }
            if (sapInfo.getPaymentTermId() != null) {
                SET("payment_term_id = #{paymentTermId}");
            }
            if (sapInfo.getUserSaleName() != null) {
                SET("user_sale_name = #{userSaleName}");
            }
            if (sapInfo.getUserStockShow() != null) {
                SET("user_stock_show = #{userStockShow}");
            }
            if (sapInfo.getDownloadStockListShow() != null) {
                SET("download_stock_list_show = #{downloadStockListShow}");
            }
            if (sapInfo.getAddCoupons() != null) {
                SET("add_coupons = #{addCoupons}");
            }
            if (sapInfo.getShowCouponsWindows() != null) {
                SET("show_coupons_windows = #{showCouponsWindows}");
            }
            if (sapInfo.getSaleChannel() != null) {
                SET("sale_channel = #{saleChannel}");
            }
            if (sapInfo.getAddFlashSales() != null) {
                SET("add_flash_sales = #{addFlashSales}");
            }
            if (sapInfo.getUserConfig() != null) {
                SET("user_config = #{userConfig}");
            }
            if (sapInfo.getResetDevicePassword() != null) {
                SET("reset_device_password = #{resetDevicePassword}");
            }
            if (sapInfo.getCustomerName() != null) {
                SET("customer_name = #{customerName}");
            }
            if (sapInfo.getFinanceEmail() != null) {
                SET("finance_email = #{financeEmail}");
            }
            if (sapInfo.getPurchaseEmail() != null) {
                SET("purchase_email = #{purchaseEmail}");
            }
            if (sapInfo.getSpecialSupportTimeStart() != null) {
                SET("special_support_time_start = #{specialSupportTimeStart}");
            }
            if (sapInfo.getSpecialSupportTimeEnd() != null) {
                SET("special_support_time_end = #{specialSupportTimeEnd}");
            }
            if (sapInfo.getLastLoginTime() != null) {
                SET("last_login_time = #{lastLoginTime}");
            }

            WHERE("user_sap_id = #{userSapId}");
            WHERE("delete_status = '0'");
        }}.toString();
    }

    public String countByCondition(Map<String, Object> params) {
        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("sap_info");
            WHERE("delete_status = '0'");

            if (params.get("userSapId") != null) {
                WHERE("user_sap_id LIKE CONCAT('%', #{params.userSapId}, '%')");
            }
            if (params.get("userName") != null) {
                WHERE("user_name LIKE CONCAT('%', #{params.userName}, '%')");
            }
            if (params.get("userEmail") != null) {
                WHERE("user_email LIKE CONCAT('%', #{params.userEmail}, '%')");
            }
            if (params.get("userMobile") != null) {
                WHERE("user_mobile LIKE CONCAT('%', #{params.userMobile}, '%')");
            }
            if (params.get("isLock") != null) {
                WHERE("is_lock = #{params.isLock}");
            }
            if (params.get("sapUserGroup") != null) {
                WHERE("sap_user_group = #{params.sapUserGroup}");
            }
            if (params.get("regionId") != null) {
                WHERE("region_id = #{params.regionId}");
            }
            if (params.get("districtId") != null) {
                WHERE("district_id = #{params.districtId}");
            }
            if (params.get("searchKeyword") != null) {
                WHERE("(user_sap_id LIKE CONCAT('%', #{params.searchKeyword}, '%') " +
                        "OR user_name LIKE CONCAT('%', #{params.searchKeyword}, '%') " +
                        "OR user_email LIKE CONCAT('%', #{params.searchKeyword}, '%') " +
                        "OR user_mobile LIKE CONCAT('%', #{params.searchKeyword}, '%') " +
                        "OR customer_name LIKE CONCAT('%', #{params.searchKeyword}, '%'))");
            }
        }}.toString();
    }
}