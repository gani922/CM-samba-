package com.pramaindia.login.mapper;

import com.pramaindia.login.model.entity.SsoUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SsoUserMapper {

//    @Select("SELECT * FROM sso_user WHERE email = #{email}")
    SsoUserEntity findByEmail(@Param("email") String email);
}
