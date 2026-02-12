package com.pramaindia.login.mapper;

import com.pramaindia.login.model.entity.CompanyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface CompanyMapper {
    CompanyEntity findBySapId(@Param("sapId") String sapId);
    List<CompanyEntity> findByCompanyName(@Param("companyName") String companyName);
    List<CompanyEntity> findAllCompanies();
    List<CompanyEntity> findByRegion(@Param("region") String region);
    List<CompanyEntity> searchCompanies(@Param("sapId") String sapId,
                                        @Param("companyName") String companyName,
                                        @Param("region") String region,
                                        @Param("branch") String branch,
                                        @Param("district") String district,
                                        @Param("city") String city,
                                        @Param("state") String state,
                                        @Param("country") String country,
                                        @Param("limit") Integer limit);
    int insertCompany(CompanyEntity entity);
    int updateCompany(CompanyEntity entity);
    int updateStatus(@Param("sapId") String sapId, @Param("status") String status);
    int deleteBySapId(@Param("sapId") String sapId);
    Long countCompanies(@Param("region") String region, @Param("branch") String branch);
    List<Map<String, Object>> getCompanyStats();
    List<CompanyEntity> findCompaniesWithNoRecentLogins();
}