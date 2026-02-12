package com.pramaindia.cust_manag.dao;

import com.pramaindia.cust_manag.entity.CompanyAddress;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CompanyAddressDAO extends Mapper<CompanyAddress> {

    @Select("SELECT MAX(ADDRESS_ID) FROM COMPANY_ADDRESS")
    Long getMaxAddressId();

    @Select("SELECT ADDRESS_ID FROM COMPANY_ADDRESS WHERE USER_SAP_ID = #{userSapId} AND DEFAULT_ADDRESS = 'Y'")
    Long findDefaultAddressIdByUserSapId(String userSapId);
}