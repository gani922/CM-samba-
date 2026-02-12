package com.pramaindia.customer_management.dao;

import com.pramaindia.customer_management.dto.BUInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BUInfoDAO {
    List<BUInfoDTO> getBUsByAdminId(@Param("adminId") String adminId);
    List<BUInfoDTO> getSubBUsWithParentId5(@Param("adminId") String adminId);
}