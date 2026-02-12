package com.pramaindia.cust_manag.dao;

import com.pramaindia.cust_manag.entity.SapInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SapInfoMapper {

    @Select("SELECT COUNT(*) FROM sap_info WHERE delete_status = '0'")
    Long countAll();

    @Select("SELECT * FROM sap_info WHERE user_sap_id = #{userSapId} AND delete_status = '0'")
    @Results({
            @Result(property = "userSapId", column = "user_sap_id"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "userEmail", column = "user_email"),
            @Result(property = "userTelphone", column = "user_telphone"),
            @Result(property = "userMobile", column = "user_mobile"),
            @Result(property = "userMobileCountryCode", column = "user_mobile_country_code"),
            @Result(property = "userSaleSapId", column = "user_sale_sap_id"),
            @Result(property = "userSaleOrg", column = "user_sale_org"),
            @Result(property = "userSaleDept", column = "user_sale_dept"),
            @Result(property = "userSaleGroup", column = "user_sale_group"),
            @Result(property = "userCountryCode", column = "user_country_code"),
            @Result(property = "userPassword", column = "user_password"),
            @Result(property = "userSalt", column = "user_salt"),
            @Result(property = "createdBy", column = "created_by"),
            @Result(property = "creationTime", column = "creation_time"),
            @Result(property = "modifiedBy", column = "modified_by"),
            @Result(property = "modificationTime", column = "modification_time"),
            @Result(property = "deleteStatus", column = "delete_status"),
            @Result(property = "passwordModificationTime", column = "password_modification_time"),
            @Result(property = "userParentId", column = "user_parent_id"),
            @Result(property = "userLevel", column = "user_level"),
            @Result(property = "sapUserGroup", column = "sap_user_group"),
            @Result(property = "isResetPassword", column = "is_reset_password"),
            @Result(property = "resetPasswordTime", column = "reset_password_time"),
            @Result(property = "userGrade", column = "user_grade"),
            @Result(property = "userArea", column = "user_area"),
            @Result(property = "userPhoto", column = "user_photo"),
            @Result(property = "panNumber", column = "pan_number"),
            @Result(property = "gstNumber", column = "gst_number"),
            @Result(property = "saleEmail", column = "sale_email"),
            @Result(property = "sapPriceGroup", column = "sap_price_group"),
            @Result(property = "userAddressLock", column = "user_address_lock"),
            @Result(property = "paymentTerm", column = "payment_term"),
            @Result(property = "paymentTermId", column = "payment_term_id"),
            @Result(property = "userSaleName", column = "user_sale_name"),
            @Result(property = "branchManagerSapId", column = "branch_manager_sap_id"),
            @Result(property = "branchManagerName", column = "branch_manager_name"),
            @Result(property = "reginalManagerSapId", column = "reginal_manager_sap_id"),
            @Result(property = "reginalManagerName", column = "reginal_manager_name"),
            @Result(property = "userStockShow", column = "user_stock_show"),
            @Result(property = "downloadStockListShow", column = "download_stock_list_show"),
            @Result(property = "addCoupons", column = "add_coupons"),
            @Result(property = "showCouponsWindows", column = "show_coupons_windows"),
            @Result(property = "saleChannel", column = "sale_channel"),
            @Result(property = "addFlashSales", column = "add_flash_sales"),
            @Result(property = "userConfig", column = "user_config"),
            @Result(property = "resetDevicePassword", column = "reset_device_password"),
            @Result(property = "userSapId1", column = "user_sap_id1"),
            @Result(property = "saleOrganizeId", column = "sale_organize_id"),
            @Result(property = "userGroupName", column = "user_group_name"),
            @Result(property = "dt_RowId", column = "dt_row_id"),
            @Result(property = "financeEmail", column = "finance_email"),
            @Result(property = "purchaseEmail", column = "purchase_email"),
            @Result(property = "userSapIds", column = "user_sap_ids"),
            @Result(property = "b2bUser", column = "b2b_user"),
            @Result(property = "specialSupportTimeStart", column = "special_support_time_start"),
            @Result(property = "specialSupportTimeEnd", column = "special_support_time_end"),
            @Result(property = "groupIds", column = "group_ids"),
            @Result(property = "customerName", column = "customer_name"),
            @Result(property = "lastLoginTime", column = "last_login_time")
    })
    SapInfo findBySapId(@Param("userSapId") String userSapId);

    // Add this method declaration
    List<SapInfo> findAll(@Param("params") Map<String, Object> params);

    // Add this method declaration
    Long countByCondition(@Param("params") Map<String, Object> params);

    @Insert("INSERT INTO sap_info (" +
            "user_id, user_sap_id, is_lock, user_name, user_name_cipher, start_valid_date, " +
            "end_valid_date, user_email, user_email_cipher, user_telphone, user_telphone_cipher, " +
            "user_mobile, user_mobile_cipher, user_mobile_country_code, user_mobile_country_code_cipher, " +
            "user_sale_sap_id, currency_code, language_code, user_sale_org, user_sale_dept, " +
            "user_sale_group, user_country_code, user_password, user_salt, created_by, " +
            "creation_time, modified_by, modification_time, delete_status, password_modification_time, " +
            "auth_law, user_parent_id, user_level, short_message, sap_user_group, is_reset_password, " +
            "reset_password_time, user_grade, user_area, user_photo, region, region_id, pan_number, " +
            "gst_number, sale_email, sale_email_cipher, sap_price_group, branch, user_address_lock, " +
            "district, payment_term, district_id, payment_term_id, user_sale_name, " +
            "branch_manager_sap_id, branch_manager_name, reginal_manager_sap_id, reginal_manager_name, " +
            "user_stock_show, download_stock_list_show, add_coupons, show_coupons_windows, " +
            "sale_channel, add_flash_sales, user_config, reset_device_password, user_sap_id1, " +
            "sale_organize_id, sales_name, country_name, user_group_name, reason, dt_row_id, " +
            "finance_email, purchase_email, user_sap_ids, b2b_user, special_support_time_start, " +
            "special_support_time_end, group_ids, customer_name, last_login_time" +
            ") VALUES (" +
            "#{userId}, #{userSapId}, #{isLock}, #{userName}, #{userNameCipher}, #{startValidDate}, " +
            "#{endValidDate}, #{userEmail}, #{userEmailCipher}, #{userTelphone}, #{userTelphoneCipher}, " +
            "#{userMobile}, #{userMobileCipher}, #{userMobileCountryCode}, #{userMobileCountryCodeCipher}, " +
            "#{userSaleSapId}, #{currencyCode}, #{languageCode}, #{userSaleOrg}, #{userSaleDept}, " +
            "#{userSaleGroup}, #{userCountryCode}, #{userPassword}, #{userSalt}, #{createdBy}, " +
            "#{creationTime}, #{modifiedBy}, #{modificationTime}, #{deleteStatus}, #{passwordModificationTime}, " +
            "#{authLaw}, #{userParentId}, #{userLevel}, #{shortMessage}, #{sapUserGroup}, #{isResetPassword}, " +
            "#{resetPasswordTime}, #{userGrade}, #{userArea}, #{userPhoto}, #{region}, #{regionId}, #{panNumber}, " +
            "#{gstNumber}, #{saleEmail}, #{saleEmailCipher}, #{sapPriceGroup}, #{branch}, #{userAddressLock}, " +
            "#{district}, #{paymentTerm}, #{districtId}, #{paymentTermId}, #{userSaleName}, " +
            "#{branchManagerSapId}, #{branchManagerName}, #{reginalManagerSapId}, #{reginalManagerName}, " +
            "#{userStockShow}, #{downloadStockListShow}, #{addCoupons}, #{showCouponsWindows}, " +
            "#{saleChannel}, #{addFlashSales}, #{userConfig}, #{resetDevicePassword}, #{userSapId1}, " +
            "#{saleOrganizeId}, #{salesName}, #{countryName}, #{userGroupName}, #{reason}, #{dt_RowId}, " +
            "#{financeEmail}, #{purchaseEmail}, #{userSapIds}, #{b2bUser}, #{specialSupportTimeStart}, " +
            "#{specialSupportTimeEnd}, #{groupIds}, #{customerName}, #{lastLoginTime}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(SapInfo sapInfo);

    @Update("UPDATE sap_info SET " +
            "is_lock = #{isLock}, " +
            "user_name = #{userName}, " +
            "user_name_cipher = #{userNameCipher}, " +
            "start_valid_date = #{startValidDate}, " +
            "end_valid_date = #{endValidDate}, " +
            "user_email = #{userEmail}, " +
            "user_email_cipher = #{userEmailCipher}, " +
            "user_telphone = #{userTelphone}, " +
            "user_telphone_cipher = #{userTelphoneCipher}, " +
            "user_mobile = #{userMobile}, " +
            "user_mobile_cipher = #{userMobileCipher}, " +
            "user_mobile_country_code = #{userMobileCountryCode}, " +
            "user_mobile_country_code_cipher = #{userMobileCountryCodeCipher}, " +
            "user_sale_sap_id = #{userSaleSapId}, " +
            "currency_code = #{currencyCode}, " +
            "language_code = #{languageCode}, " +
            "user_sale_org = #{userSaleOrg}, " +
            "user_sale_dept = #{userSaleDept}, " +
            "user_sale_group = #{userSaleGroup}, " +
            "user_country_code = #{userCountryCode}, " +
            "user_password = #{userPassword}, " +
            "user_salt = #{userSalt}, " +
            "modified_by = #{modifiedBy}, " +
            "modification_time = NOW(), " +
            "password_modification_time = #{passwordModificationTime}, " +
            "auth_law = #{authLaw}, " +
            "user_parent_id = #{userParentId}, " +
            "user_level = #{userLevel}, " +
            "short_message = #{shortMessage}, " +
            "sap_user_group = #{sapUserGroup}, " +
            "is_reset_password = #{isResetPassword}, " +
            "reset_password_time = #{resetPasswordTime}, " +
            "user_grade = #{userGrade}, " +
            "user_area = #{userArea}, " +
            "user_photo = #{userPhoto}, " +
            "region = #{region}, " +
            "region_id = #{regionId}, " +
            "pan_number = #{panNumber}, " +
            "gst_number = #{gstNumber}, " +
            "sale_email = #{saleEmail}, " +
            "sale_email_cipher = #{saleEmailCipher}, " +
            "sap_price_group = #{sapPriceGroup}, " +
            "branch = #{branch}, " +
            "user_address_lock = #{userAddressLock}, " +
            "district = #{district}, " +
            "payment_term = #{paymentTerm}, " +
            "district_id = #{districtId}, " +
            "payment_term_id = #{paymentTermId}, " +
            "user_sale_name = #{userSaleName}, " +
            "branch_manager_sap_id = #{branchManagerSapId}, " +
            "branch_manager_name = #{branchManagerName}, " +
            "reginal_manager_sap_id = #{reginalManagerSapId}, " +
            "reginal_manager_name = #{reginalManagerName}, " +
            "user_stock_show = #{userStockShow}, " +
            "download_stock_list_show = #{downloadStockListShow}, " +
            "add_coupons = #{addCoupons}, " +
            "show_coupons_windows = #{showCouponsWindows}, " +
            "sale_channel = #{saleChannel}, " +
            "add_flash_sales = #{addFlashSales}, " +
            "user_config = #{userConfig}, " +
            "reset_device_password = #{resetDevicePassword}, " +
            "user_sap_id1 = #{userSapId1}, " +
            "sale_organize_id = #{saleOrganizeId}, " +
            "sales_name = #{salesName}, " +
            "country_name = #{countryName}, " +
            "user_group_name = #{userGroupName}, " +
            "reason = #{reason}, " +
            "dt_row_id = #{dt_RowId}, " +
            "finance_email = #{financeEmail}, " +
            "purchase_email = #{purchaseEmail}, " +
            "user_sap_ids = #{userSapIds}, " +
            "b2b_user = #{b2bUser}, " +
            "special_support_time_start = #{specialSupportTimeStart}, " +
            "special_support_time_end = #{specialSupportTimeEnd}, " +
            "group_ids = #{groupIds}, " +
            "customer_name = #{customerName}, " +
            "last_login_time = #{lastLoginTime} " +
            "WHERE user_sap_id = #{userSapId} AND delete_status = '0'")
    int update(SapInfo sapInfo);

    @Update("UPDATE sap_info SET delete_status = '1', modification_time = NOW(), modified_by = #{modifiedBy} WHERE user_sap_id = #{userSapId}")
    int deleteBySapId(@Param("userSapId") String userSapId, @Param("modifiedBy") String modifiedBy);

    @Update("UPDATE sap_info SET is_lock = #{isLock}, modification_time = NOW(), modified_by = #{modifiedBy} WHERE user_sap_id = #{userSapId}")
    int updateLockStatus(@Param("userSapId") String userSapId, @Param("isLock") String isLock, @Param("modifiedBy") String modifiedBy);

    @Select("SELECT * FROM sap_info WHERE user_email = #{email} AND delete_status = '0'")
    SapInfo findByEmail(@Param("email") String email);

    @Select("SELECT * FROM sap_info WHERE user_mobile = #{mobile} AND delete_status = '0'")
    SapInfo findByMobile(@Param("mobile") String mobile);

    @Select("SELECT * FROM sap_info WHERE pan_number = #{panNumber} AND delete_status = '0'")
    SapInfo findByPanNumber(@Param("panNumber") String panNumber);

    @Select("SELECT * FROM sap_info WHERE gst_number = #{gstNumber} AND delete_status = '0'")
    SapInfo findByGstNumber(@Param("gstNumber") String gstNumber);

    @Select("SELECT * FROM sap_info WHERE is_lock = 'Y' AND delete_status = '0'")
    List<SapInfo> findLockedUsers();

    @Select("SELECT * FROM sap_info WHERE sap_user_group = #{userGroup} AND delete_status = '0'")
    List<SapInfo> findByUserGroup(@Param("userGroup") String userGroup);

    @Select("SELECT * FROM sap_info WHERE region_id = #{regionId} AND delete_status = '0'")
    List<SapInfo> findByRegion(@Param("regionId") String regionId);

    @Select("SELECT * FROM sap_info WHERE district_id = #{districtId} AND delete_status = '0'")
    List<SapInfo> findByDistrict(@Param("districtId") String districtId);

    // Add this method declaration
    void updateLastLoginTime(@Param("userSapId") String userSapId);

    // Add this method declaration
    List<SapInfo> findBySpecialSupportTimeRange();
}