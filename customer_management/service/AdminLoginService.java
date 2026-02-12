package com.pramaindia.customer_management.service;

import com.pramaindia.customer_management.dto.request.LoginRequestDTO;
import com.pramaindia.customer_management.dto.response.LoginResponseDTO;
import com.pramaindia.customer_management.dto.response.RefreshTokenResponseDTO;

import java.util.Map;

public interface AdminLoginService {
    LoginResponseDTO authenticate(LoginRequestDTO request);
    RefreshTokenResponseDTO refreshToken(String refreshToken);
    LoginResponseDTO getCurrentAdminInfo(String adminId);
    Object getOrganizations(String adminId);
    boolean logout(String token);
    boolean validateCurrentSession(String adminId);
    Map<String, Object> getAdminProfile(String adminId);
    void changePassword(String adminId, String currentPassword, String newPassword);
    void processForgotPassword(String email, String phoneNumber);
    boolean validateResetToken(String resetToken);
    void resetPasswordWithToken(String resetToken, String newPassword);

}