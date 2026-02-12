package com.pramaindia.customer_management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pramaindia.customer_management.dto.BUInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // User Information
    private String adminId;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private String phoneNumber;
    private String department;
    private String profileImageUrl;
    private Boolean isActive;

    // Add lastLogin field
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String lastLogin;

    // Session Information
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String loginTime;

    // Token Information
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    @Builder.Default
    private String tokenType = "Bearer";

    @JsonProperty("expires_in")
    private Long expiresIn;  // Token expiry in seconds

    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;  // Refresh token expiry in seconds

    // Business Information
    private List<BUInfoDTO> buList;
    private String organizationId;
    private String organizationName;

    // Permissions & Roles
    private List<String> permissions;
    private List<String> roles;

    // Status Information
    private Integer statusCode;
    private String statusMessage;

    // Helper method to create successful login response
    public static LoginResponseDTO createSuccessResponse(
            String adminId,
            String username,
            String accessToken,
            String refreshToken,
            List<BUInfoDTO> buList) {

        return LoginResponseDTO.builder()
                .adminId(adminId)
                .username(username)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)  // 1 hour in seconds
                .refreshExpiresIn(86400L)  // 24 hours in seconds
                .loginTime(LocalDateTime.now().format(DATE_FORMATTER))
                .buList(buList)
                .isActive(true)
                .statusCode(200)
                .statusMessage("Login successful")
                .build();
    }

    // Helper method to create response with full user details
    public static LoginResponseDTO createFullResponse(
            String adminId,
            String username,
            String email,
            String fullName,
            String role,
            String phoneNumber,
            String department,
            String accessToken,
            String refreshToken,
            List<BUInfoDTO> buList) {

        return LoginResponseDTO.builder()
                .adminId(adminId)
                .username(username)
                .email(email)
                .fullName(fullName)
                .role(role)
                .phoneNumber(phoneNumber)
                .department(department)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .refreshExpiresIn(86400L)
                .loginTime(LocalDateTime.now().format(DATE_FORMATTER))
                .buList(buList)
                .isActive(true)
                .statusCode(200)
                .statusMessage("Login successful")
                .build();
    }

    // Helper method to create response with full user details including lastLogin
// In LoginResponseDTO.java
    public static LoginResponseDTO createFullResponseWithLastLogin(
            String adminId,
            String username,
            String email,
            String fullName,
            String role,
            String phoneNumber,
            String department,
            String lastLogin,
            Boolean isActive,
            String accessToken,
            String refreshToken,
            List<BUInfoDTO> buList) {

        return LoginResponseDTO.builder()
                .adminId(adminId)
                .username(username)
                .email(email)
                .fullName(fullName)
                .role(role)
                .phoneNumber(phoneNumber)
                .department(department)
                .lastLogin(lastLogin)
                .isActive(isActive)
                .accessToken(accessToken)      // Make sure this is set
                .refreshToken(refreshToken)    // Make sure this is set
                .tokenType("Bearer")
                .expiresIn(3600L)
                .refreshExpiresIn(86400L)
                .loginTime(LocalDateTime.now().format(DATE_FORMATTER))
                .buList(buList)
                .statusCode(200)
                .statusMessage("Login successful")
                .build();
    }

    // Helper method to create admin info response (without tokens)
    public static LoginResponseDTO createAdminInfoResponse(
            String adminId,
            String username,
            String email,
            String fullName,
            String role,
            String phoneNumber,
            String department,
            String lastLogin,
            Boolean isActive,
            List<BUInfoDTO> buList) {

        return LoginResponseDTO.builder()
                .adminId(adminId)
                .username(username)
                .email(email)
                .fullName(fullName)
                .role(role)
                .phoneNumber(phoneNumber)
                .department(department)
                .lastLogin(lastLogin)
                .isActive(isActive)
                .buList(buList)
                .statusCode(200)
                .statusMessage("Admin information retrieved successfully")
                .build();
    }

    // Helper method to create minimal response (for backward compatibility)
    public static LoginResponseDTO createMinimalResponse(
            String adminId,
            String username,
            String loginTime) {

        return LoginResponseDTO.builder()
                .adminId(adminId)
                .username(username)
                .loginTime(loginTime)
                .build();
    }

    // Helper method to create refresh token response
    public static LoginResponseDTO createRefreshResponse(
            String accessToken,
            String refreshToken,
            String username) {

        return LoginResponseDTO.builder()
                .username(username)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .refreshExpiresIn(86400L)
                .statusCode(200)
                .statusMessage("Token refreshed successfully")
                .build();
    }
}