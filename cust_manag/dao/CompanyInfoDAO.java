package com.pramaindia.cust_manag.dao;

import com.pramaindia.cust_manag.entity.CompanyInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CompanyInfoDAO extends Mapper<CompanyInfo> {

    @Select("SELECT MAX(ID) FROM COMPANY_INFO")
    Long getMaxCompanyId();

    @Select("SELECT * FROM COMPANY_INFO WHERE SAP_ID = #{sapId} AND DELETE_STATUS != 'D' LIMIT 1")
    CompanyInfo findBySapId(String sapId);

    @Select("SELECT COUNT(*) FROM COMPANY_INFO WHERE COMPANY_NAME = #{companyName}")
    Integer countByCompanyName(String companyName);
}