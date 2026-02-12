package com.pramaindia.login.mapper;

import com.pramaindia.login.model.dto.LoginRecordRequestDto;
import com.pramaindia.login.model.entity.LoginRecordEntity;
import com.pramaindia.login.model.vo.LoginRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface CustomerLoginRecordMapper {
    List<LoginRecordVo> getLoginRecords(LoginRecordRequestDto condition);
    Long countLoginRecords(LoginRecordRequestDto condition);

    int insertLoginRecord(LoginRecordEntity entity);
    int updateLogoutTime(@Param("sessionId") String sessionId,
                         @Param("logoutTime") Date logoutTime);
    LoginRecordEntity findActiveSession(@Param("sessionId") String sessionId);

    List<LoginRecordVo> getActiveSessions();

    List<LoginRecordVo> findBySessionId(String sessionId);

    List<Map<String, Object>> getLoginStats();

    List<Map<String, Object>> getTopCompaniesByLoginCount();

    List<Map<String, Object>> getHourlyLoginStats();

    List<Map<String, Object>> getRegionWiseStats();

    int markStaleSessionsAsLogout();
}