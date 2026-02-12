package com.pramaindia.customer_management.service.impl;

import com.pramaindia.customer_management.dao.AdminLoginDAO;
import com.pramaindia.customer_management.dao.BUInfoDAO;
import com.pramaindia.customer_management.dao.SaleOrganizeDAO;
import com.pramaindia.customer_management.dto.BUInfoDTO;
import com.pramaindia.customer_management.dto.request.LoginRequestDTO;
import com.pramaindia.customer_management.dto.response.LoginResponseDTO;
import com.pramaindia.customer_management.dto.response.RefreshTokenResponseDTO;
import com.pramaindia.customer_management.model.AdminLoginInfo;
import com.pramaindia.customer_management.security.JwtTokenUtil;
import com.pramaindia.customer_management.service.AdminLoginService;
import com.pramaindia.customer_management.service.CaptchaService;
import com.pramaindia.customer_management.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminLoginServiceImpl implements AdminLoginService {

    private final AdminLoginDAO adminLoginDAO;
    private final BUInfoDAO buInfoDAO;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final CaptchaService captchaService;
    private final EmailService emailService;
    private final SaleOrganizeDAO saleOrganizeDAO;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final int RESET_TOKEN_EXPIRY_MINUTES = 15;

    @Override
    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        log.info("Login attempt for user: {}", request.getUsername());

        try {
            // Find user
            AdminLoginInfo adminInfo = adminLoginDAO.findByUsername(request.getUsername())
                    .orElseThrow(() -> {
                        log.warn("User not found: {}", request.getUsername());
                        return new BadCredentialsException("Invalid username or password");
                    });

            log.info("User found: {}", adminInfo.getUsername());

            // Check if user is active using helper method
            if (!adminInfo.isAccountActive()) {
                log.warn("User account is inactive: {}", request.getUsername());
                throw new BadCredentialsException("Account is inactive");
            }

            // Check password
            if (!passwordEncoder.matches(request.getPassword(), adminInfo.getPasswordHash())) {
                log.warn("Password mismatch for user: {}", request.getUsername());
                throw new BadCredentialsException("Invalid username or password");
            }

            // Spring Security authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Update last login using helper method
            adminInfo.updateLastLogin();
            adminLoginDAO.updateLastLogin(adminInfo.getAdminId(), adminInfo.getLastLogin());

            // Generate tokens
            String accessToken = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            // Get BU list
            List<BUInfoDTO> buList = buInfoDAO.getBUsByAdminId(adminInfo.getAdminId());

            log.info("Login successful for user: {}", request.getUsername());

            return LoginResponseDTO.createFullResponseWithLastLogin(
                    adminInfo.getAdminId(),
                    adminInfo.getUsername(),
                    adminInfo.getEmail(),
                    adminInfo.getFullName(),
                    adminInfo.getRole(),
                    adminInfo.getPhoneNumber(),
                    adminInfo.getDepartment(),
                    adminInfo.getLastLogin() != null ?
                            adminInfo.getLastLogin().format(DATE_FORMATTER) : null,
                    adminInfo.isAccountActive(),
                    accessToken,
                    refreshToken,
                    buList
            );

        } catch (BadCredentialsException e) {
            log.warn("Authentication failed: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.error("Authentication error", e);
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(String refreshToken) {
        log.info("Refresh token request");

        try {
            // Validate refresh token
            if (!jwtTokenUtil.validateToken(refreshToken)) {
                throw new RuntimeException("Invalid or expired refresh token");
            }

            // Get username from token
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

            // Check if token is blacklisted
            if (adminLoginDAO.isTokenBlacklisted(refreshToken) > 0) {
                throw new RuntimeException("Token has been revoked");
            }

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Generate new tokens
            String newAccessToken = jwtTokenUtil.generateToken(userDetails);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            // Blacklist old refresh token
            adminLoginDAO.blacklistToken(refreshToken, LocalDateTime.now());

            return RefreshTokenResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(3600L)
                    .refreshExpiresIn(86400L)
                    .build();

        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new RuntimeException("Failed to refresh token: " + e.getMessage());
        }
    }

    @Override
    public LoginResponseDTO getCurrentAdminInfo(String adminId) {
        log.info("Getting admin info for ID: {}", adminId);

        AdminLoginInfo adminInfo = adminLoginDAO.findById(adminId)
                .orElseThrow(() -> {
                    log.warn("Admin not found: {}", adminId);
                    return new RuntimeException("Admin not found");
                });

        // Check if user is active using helper method
        if (!adminInfo.isAccountActive()) {
            throw new RuntimeException("Admin account is inactive");
        }

        List<BUInfoDTO> buList = buInfoDAO.getBUsByAdminId(adminId);

        return LoginResponseDTO.createAdminInfoResponse(
                adminInfo.getAdminId(),
                adminInfo.getUsername(),
                adminInfo.getEmail(),
                adminInfo.getFullName(),
                adminInfo.getRole(),
                adminInfo.getPhoneNumber(),
                adminInfo.getDepartment(),
                adminInfo.getLastLogin() != null ?
                        adminInfo.getLastLogin().format(DATE_FORMATTER) : null,
                adminInfo.isAccountActive(),
                buList
        );
    }

    @Override
    public boolean logout(String token) {
        log.info("Logout request");

        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            // Check if token is already blacklisted
            if (adminLoginDAO.isTokenBlacklisted(token) == 0) {
                adminLoginDAO.blacklistToken(token, LocalDateTime.now());
            }
            SecurityContextHolder.clearContext();
            log.info("Logout successful for token: {}", token.substring(0, Math.min(20, token.length())) + "...");
            return true;
        } catch (Exception e) {
            log.error("Logout error", e);
            return false;
        }
    }

    @Override
    public Object getOrganizations(String adminId) {
        try {
            // Try to get data from sale_organize table
            List<Map<String, Object>> organizations = saleOrganizeDAO.getSaleOrganizationsByAdminId(adminId);

            // If empty, try from sap_info table
            if (organizations.isEmpty()) {
                organizations = saleOrganizeDAO.getSaleOrganizationsFromSapInfo(adminId);
            }

            // If still empty, return default structure
            if (organizations.isEmpty()) {
                log.info("No organizations found for admin {}, returning default structure", adminId);
                return createDefaultOrganizationStructure();
            }

            log.info("Found {} organizations for admin {}", organizations.size(), adminId);
            return organizations;

        } catch (Exception e) {
            log.error("Error getting organizations for admin: {}", adminId, e);
            return createDefaultOrganizationStructure();
        }
    }

    private List<Map<String, Object>> createDefaultOrganizationStructure() {
        List<Map<String, Object>> organizations = new ArrayList<>();
        Map<String, Object> org = new HashMap<>();
        org.put("saleOrganizeId", "8304");
        org.put("saleOrganizeShort", "IN");
        org.put("saleOrganizeName", "India Branch 1");
        org.put("channel", null);
        organizations.add(org);
        return organizations;
    }

    @Override
    public boolean validateCurrentSession(String adminId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("Authentication is null or not authenticated for adminId: {}", adminId);
                return false;
            }

            log.info("Authentication details - Name: {}, Authorities: {}, Authenticated: {}",
                    authentication.getName(),
                    authentication.getAuthorities(),
                    authentication.isAuthenticated());

            // Get admin from database
            AdminLoginInfo admin = adminLoginDAO.findById(adminId).orElse(null);
            if (admin == null) {
                log.warn("Admin not found in database for adminId: {}", adminId);
                return false;
            }

            if (!admin.isAccountActive()) {
                log.warn("Admin account is inactive for adminId: {}", adminId);
                return false;
            }

            // Debug: Print both usernames
            log.info("Comparing - Authentication name: {}, Admin username: {}",
                    authentication.getName(), admin.getUsername());

            // Check if the authentication name matches admin username
            boolean usernameMatches = authentication.getName().equals(admin.getUsername());

            if (!usernameMatches) {
                log.warn("Username mismatch. Authentication name: {}, Admin username: {}",
                        authentication.getName(), admin.getUsername());
            }

            return usernameMatches;

        } catch (Exception e) {
            log.error("Session validation error for admin: {}", adminId, e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getAdminProfile(String adminId) {
        AdminLoginInfo admin = adminLoginDAO.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Check if user is active using helper method
        if (!admin.isAccountActive()) {
            throw new RuntimeException("Admin account is inactive");
        }

        List<BUInfoDTO> buList = buInfoDAO.getBUsByAdminId(adminId);

        Map<String, Object> profile = new HashMap<>();
        profile.put("adminId", admin.getAdminId());
        profile.put("username", admin.getUsername());
        profile.put("email", admin.getEmail());
        profile.put("fullName", admin.getFullName());
        profile.put("role", admin.getRole());
        profile.put("phoneNumber", admin.getPhoneNumber());
        profile.put("department", admin.getDepartment());
        profile.put("isActive", admin.isAccountActive());
        profile.put("createdAt", admin.getCreatedAt() != null ? admin.getCreatedAt().format(DATE_FORMATTER) : null);
        profile.put("lastLogin", admin.getLastLogin() != null ? admin.getLastLogin().format(DATE_FORMATTER) : null);
        profile.put("businessUnits", buList);
        profile.put("businessUnitCount", buList != null ? buList.size() : 0);
        profile.put("displayName", admin.getDisplayName());
        profile.put("maskedEmail", admin.getMaskedEmail());
        profile.put("maskedPhone", admin.getMaskedPhoneNumber());
        profile.put("isNewUser", admin.isNewUser());

        return profile;
    }

    @Override
    public void changePassword(String adminId, String currentPassword, String newPassword) {
        log.info("Changing password for admin: {}", adminId);

        try {
            AdminLoginInfo adminInfo = adminLoginDAO.findById(adminId)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            // Check if current password matches
            if (!passwordEncoder.matches(currentPassword, adminInfo.getPasswordHash())) {
                throw new RuntimeException("Current password is incorrect");
            }

            // Validate new password
            if (newPassword == null || newPassword.length() < 8) {
                throw new RuntimeException("New password must be at least 8 characters long");
            }

            // Optional: Check if new password is same as old password
            if (passwordEncoder.matches(newPassword, adminInfo.getPasswordHash())) {
                throw new RuntimeException("New password cannot be the same as current password");
            }

            // Hash new password
            String newPasswordHash = passwordEncoder.encode(newPassword);

            // Update password in database
            int updated = adminLoginDAO.updatePassword(adminId, newPasswordHash);

            if (updated == 0) {
                throw new RuntimeException("Failed to update password");
            }

            log.info("Password changed successfully for admin: {}", adminId);

        } catch (RuntimeException e) {
            log.warn("Password change failed for admin {}: {}", adminId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error changing password for admin: {}", adminId, e);
            throw new RuntimeException("Failed to change password");
        }
    }



    @Override
    public void processForgotPassword(String email, String phoneNumber) {
        log.info("Processing forgot password for email: {}, phone: {}", email, phoneNumber);
        try {
            // Validate inputs
            if (email == null || email.trim().isEmpty()) {
                throw new RuntimeException("Email is required");
            }

            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                throw new RuntimeException("Phone number is required");
            }

            String cleanEmail = email.trim();
            String cleanPhone = phoneNumber.trim();

            // Find user by email and phone
            AdminLoginInfo adminInfo = adminLoginDAO.findByEmailAndPhone(cleanEmail, cleanPhone)
                    .orElse(null);

            // For security, always return success even if user not found
            if (adminInfo == null) {
                log.info("Forgot password request processed for: {}", cleanEmail);
                return;
            }

            // Check if user is active
            if (!Boolean.TRUE.equals(adminInfo.getIsActive())) {
                log.info("Forgot password request for inactive account: {}", cleanEmail);
                return;
            }

            // Generate reset token
            String resetToken = UUID.randomUUID().toString();

            // Set expiry to 15 minutes from now
            LocalDateTime tokenExpiry = LocalDateTime.now().plusMinutes(RESET_TOKEN_EXPIRY_MINUTES);

            // Update user with reset token
            int updated = adminLoginDAO.updateResetToken(adminInfo.getAdminId(), resetToken, tokenExpiry);

            if (updated == 0) {
                log.error("Failed to update reset token for user: {}", adminInfo.getUsername());
                return;
            }

            // Create reset link
            String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;

            // Send email (For now, just log it. In production, implement email service)
            log.info("Password Reset Email Details:");
            log.info("To: {}", adminInfo.getEmail());
            log.info("Subject: Password Reset Request");
            log.info("Reset Link: {}", resetLink);
            log.info("Token: {}", resetToken);
            log.info("Token Expiry: {}", tokenExpiry);

            // Also log to console for easy access during development
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🔐 PASSWORD RESET TOKEN (FOR DEVELOPMENT)");
            System.out.println("=".repeat(60));
            System.out.println("User: " + adminInfo.getUsername());
            System.out.println("Email: " + adminInfo.getEmail());
            System.out.println("Reset Token: " + resetToken);
            System.out.println("Reset Link: " + resetLink);
            System.out.println("Token Expiry: " + tokenExpiry);
            System.out.println("=".repeat(60) + "\n");

            // TODO: Implement actual email sending in production
            emailService.sendResetEmail(adminInfo.getEmail(), resetToken);

        } catch (RuntimeException e) {
            log.warn("Forgot password failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error processing forgot password", e);
            throw new RuntimeException("Failed to process forgot password request");
        }
    }



    // New method to get user by reset token
    public AdminLoginInfo getUserByResetToken(String resetToken) {
        return adminLoginDAO.findByResetToken(resetToken)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));
    }
    @Override
    public boolean validateResetToken(String resetToken) {
        try {
            AdminLoginInfo adminInfo = adminLoginDAO.findByResetToken(resetToken)
                    .orElseThrow(() -> new RuntimeException("Invalid reset token"));

            // Check if token is expired
            if (adminInfo.getTokenExpiry() != null &&
                    adminInfo.getTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Reset token has expired");
            }

            return true;
        } catch (Exception e) {
            log.error("Reset token validation failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void resetPasswordWithToken(String resetToken, String newPassword) {
        log.info("Resetting password with token");

        try {
            AdminLoginInfo adminInfo = adminLoginDAO.findByResetToken(resetToken)
                    .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

            // Check if token is expired
            if (adminInfo.getTokenExpiry() != null &&
                    adminInfo.getTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Reset token has expired");
            }

            // Validate new password
            if (newPassword == null || newPassword.length() < 8) {
                throw new RuntimeException("New password must be at least 8 characters long");
            }

            // Hash new password
            String newPasswordHash = passwordEncoder.encode(newPassword);

            // Update password in database
            int updated = adminLoginDAO.updatePassword(adminInfo.getAdminId(), newPasswordHash);

            if (updated == 0) {
                throw new RuntimeException("Failed to reset password");
            }

            // Clear reset token
            adminLoginDAO.updateResetToken(adminInfo.getAdminId(), null, null);

            log.info("Password reset successfully for user: {}", adminInfo.getUsername());

        } catch (RuntimeException e) {
            log.warn("Password reset failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error resetting password", e);
            throw new RuntimeException("Failed to reset password");
        }
    }

}