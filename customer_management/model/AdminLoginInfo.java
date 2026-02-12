package com.pramaindia.customer_management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginInfo {

    // Primary identifier
    private String adminId;

    // Authentication fields
    private String username;
    private String passwordHash;

    // Personal information
    private String email;
    private String fullName;
    private String phoneNumber;
    private String department;

    // Role and status
    private String role;

    @Builder.Default
    private Boolean isActive = true;

    // Timestamps
    private LocalDateTime lastLogin;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    // Reset password fields (if needed for forgot password)
    private String resetToken;
    private LocalDateTime tokenExpiry;

    // Helper method to check if user is active
    public boolean isAccountActive() {
        return Boolean.TRUE.equals(isActive);
    }

    // Helper method to update last login
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    // Helper method to set reset token
    public void setResetToken(String token, int expiryMinutes) {
        this.resetToken = token;
        this.tokenExpiry = LocalDateTime.now().plusMinutes(expiryMinutes);
    }

    // Helper method to check if reset token is valid
    public boolean isResetTokenValid() {
        if (resetToken == null || tokenExpiry == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(tokenExpiry);
    }

    // Helper method to clear reset token
    public void clearResetToken() {
        this.resetToken = null;
        this.tokenExpiry = null;
    }

    // Helper method to get formatted last login time
    public String getFormattedLastLogin() {
        return lastLogin != null ? lastLogin.toString() : "Never";
    }

    // Helper method to check if this is a new user
    public boolean isNewUser() {
        return lastLogin == null;
    }

    // Factory method to create a new admin user
    public static AdminLoginInfo createNewAdmin(
            String adminId,
            String username,
            String passwordHash,
            String email,
            String fullName,
            String role,
            String phoneNumber,
            String department) {

        return AdminLoginInfo.builder()
                .adminId(adminId)
                .username(username)
                .passwordHash(passwordHash)
                .email(email)
                .fullName(fullName)
                .role(role)
                .phoneNumber(phoneNumber)
                .department(department)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Factory method to create a test/admin user
    public static AdminLoginInfo createTestAdmin(
            String username,
            String passwordHash,
            String role) {

        String adminId = "ADM" + System.currentTimeMillis();

        return AdminLoginInfo.builder()
                .adminId(adminId)
                .username(username)
                .passwordHash(passwordHash)
                .email(username + "@pramaindia.com")
                .fullName(username.toUpperCase() + " User")
                .role(role)
                .phoneNumber("+911234567890")
                .department("IT")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Method to get role with prefix (for Spring Security)
    public String getRoleWithPrefix() {
        return "ROLE_" + role;
    }

    // Method to validate email format (basic validation)
    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    // Method to validate phone number (basic validation)
    public boolean isValidPhoneNumber() {
        return phoneNumber != null &&
                phoneNumber.matches("^\\+?[0-9. ()-]{10,25}$");
    }

    // Method to get user display name
    public String getDisplayName() {
        return fullName != null && !fullName.trim().isEmpty() ?
                fullName : username;
    }

    // Method to mask email for display
    public String getMaskedEmail() {
        if (email == null) return null;

        String[] parts = email.split("@");
        if (parts.length != 2) return email;

        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 2) {
            return "***@" + domain;
        }

        String maskedUsername = username.charAt(0) +
                "*".repeat(username.length() - 2) +
                username.charAt(username.length() - 1);
        return maskedUsername + "@" + domain;
    }

    // Method to mask phone number for display
    public String getMaskedPhoneNumber() {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return phoneNumber;
        }

        String lastFour = phoneNumber.substring(phoneNumber.length() - 4);
        return "******" + lastFour;
    }

    // Override toString to exclude sensitive information
    @Override
    public String toString() {
        return "AdminLoginInfo{" +
                "adminId='" + adminId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + getMaskedEmail() + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                ", lastLogin=" + lastLogin +
                ", createdAt=" + createdAt +
                ", phoneNumber='" + getMaskedPhoneNumber() + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}