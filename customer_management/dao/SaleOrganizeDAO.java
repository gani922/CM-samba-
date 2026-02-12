package com.pramaindia.customer_management.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SaleOrganizeDAO {

    @Select("SELECT " +
            "sale_organize_id as saleOrganizeId, " +
            "sale_organize_short as saleOrganizeShort, " +
            "sale_organize_name as saleOrganizeName, " +
            "channel " +
            "FROM sale_organize " +
            "WHERE admin_id = #{adminId} " +
            "AND delete_status = '0' " +
            "ORDER BY sale_organize_id")
    List<Map<String, Object>> getSaleOrganizationsByAdminId(@Param("adminId") String adminId);

    // If you don't have a sale_organize table, you might need to create it
    @Select("SELECT DISTINCT " +
            "user_sale_sap_id as saleOrganizeId, " +
            "user_country_code as saleOrganizeShort, " +
            "user_sale_name as saleOrganizeName, " +
            "sale_channel as channel " +
            "FROM sap_info " +
            "WHERE created_by = #{adminId} " +
            "AND delete_status = '0' " +
            "AND user_sale_sap_id IS NOT NULL " +
            "ORDER BY user_sale_sap_id")
    List<Map<String, Object>> getSaleOrganizationsFromSapInfo(@Param("adminId") String adminId);
}