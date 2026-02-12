package com.pramaindia.role_management.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CountryDAO {

    @Select("SELECT " +
            "  c.ID as countryId, " +
            "  c.COUNTRY_NAME as countryName, " +
            "  c.BU_ID as buId, " +
            "  c.SUB_BU_ID as subBuId, " +
            "  bu.name as buName, " +
            "  subBu.name as subBuName " +
            "FROM COUNTRY c " +
            "LEFT JOIN bu_info bu ON c.BU_ID = bu.id " +
            "LEFT JOIN bu_info subBu ON c.SUB_BU_ID = subBu.id " +
            "WHERE c.COUNTRY_NAME IS NOT NULL " +
            "ORDER BY c.COUNTRY_NAME")
    List<Map<String, Object>> selectAllCountries();

    @Select("SELECT COUNT(*) FROM COUNTRY")
    int countAllCountries();

    @Select("SELECT DISTINCT c.BU_ID, bu.name as buName " +
            "FROM COUNTRY c " +
            "JOIN bu_info bu ON c.BU_ID = bu.id " +
            "ORDER BY bu.name")
    List<Map<String, Object>> selectBusinessUnits();

    @Select("SELECT " +
            "  c.ID as countryId, " +
            "  c.COUNTRY_NAME as countryName, " +
            "  c.BU_ID as buId, " +
            "  c.SUB_BU_ID as subBuId " +
            "FROM COUNTRY c " +
            "WHERE c.BU_ID = #{buId} " +
            "ORDER BY c.COUNTRY_NAME")
    List<Map<String, Object>> selectCountriesByBuId(Long buId);

    @Select("SELECT " +
            "  c.ID as countryId, " +
            "  c.COUNTRY_NAME as countryName, " +
            "  c.BU_ID as buId, " +
            "  c.SUB_BU_ID as subBuId " +
            "FROM COUNTRY c " +
            "WHERE c.SUB_BU_ID = #{subBuId} " +
            "ORDER BY c.COUNTRY_NAME")
    List<Map<String, Object>> selectCountriesBySubBuId(Long subBuId);


    @Select("SELECT " +
            "  c.ID as countryId, " +
            "  c.COUNTRY_NAME as countryName, " +
            "  c.BU_ID as buId, " +
            "  c.SUB_BU_ID as subBuId " +
            "FROM COUNTRY c " +
            "WHERE c.ID = #{countryId}")
    Map<String, Object> selectCountryById(@Param("countryId") Long countryId);
}





