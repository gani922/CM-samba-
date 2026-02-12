package com.pramaindia.customer_management.dao;

import com.pramaindia.customer_management.model.AdminLoginInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminLoginDAO {

    // Query methods
    Optional<AdminLoginInfo> findByUsername(@Param("username") String username);

    Optional<AdminLoginInfo> findById(@Param("adminId") String adminId);

    Optional<AdminLoginInfo> findByEmailAndPhone(
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber
    );

    Optional<AdminLoginInfo> findByResetToken(@Param("resetToken") String resetToken);

    List<AdminLoginInfo> selectAll();

    List<AdminLoginInfo> findByRole(@Param("role") String role);

    // Update methods
    int updateLastLogin(
            @Param("adminId") String adminId,
            @Param("lastLogin") LocalDateTime lastLogin
    );

    int updatePassword(
            @Param("adminId") String adminId,
            @Param("newHash") String newHash
    );

    int updateResetToken(
            @Param("adminId") String adminId,
            @Param("resetToken") String resetToken,
            @Param("tokenExpiry") LocalDateTime tokenExpiry
    );

    int updateProfile(AdminLoginInfo adminLoginInfo);

    int deactivateUser(@Param("adminId") String adminId);

    // Insert methods
    void insert(AdminLoginInfo adminLoginInfo);

    // JWT blacklist related methods
    void blacklistToken(
            @Param("token") String token,
            @Param("time") LocalDateTime time
    );

    int isTokenBlacklisted(@Param("token") String token);

    // Statistics methods
    int countActiveUsers();

    // Default method (for compatibility)
    default AdminLoginInfo getAdminById(String adminId) {
        return findById(adminId).orElse(null);
    }
}