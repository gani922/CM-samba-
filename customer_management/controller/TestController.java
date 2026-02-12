package com.pramaindia.customer_management.controller;

import com.pramaindia.customer_management.dao.AdminLoginDAO;
import com.pramaindia.customer_management.model.AdminLoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final AdminLoginDAO adminLoginDAO;

    @PostMapping("/setup-database")
    public String setupDatabase() {
        try {
            StringBuilder result = new StringBuilder();
            result.append("=== DATABASE SETUP ===\n\n");

            // Create encoder (matches SecurityConfig)
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            String passwordHash = encoder.encode("password");

            result.append("Generated hash for password 'password':\n");
            result.append(passwordHash).append("\n\n");

            // Check if admin already exists
            Optional<AdminLoginInfo> existingAdmin = adminLoginDAO.findByUsername("admin");

            if (existingAdmin.isPresent()) {
                // Update existing admin's password
                AdminLoginInfo admin = existingAdmin.get();
                String oldHash = admin.getPasswordHash();

                // Update password
                int updated = adminLoginDAO.updatePassword(admin.getAdminId(), passwordHash);

                result.append("Updated existing admin user:\n");
                result.append("Username: ").append(admin.getUsername()).append("\n");
                result.append("Admin ID: ").append(admin.getAdminId()).append("\n");
                result.append("Old Hash: ").append(oldHash.substring(0, 30)).append("...\n");
                result.append("New Hash: ").append(passwordHash.substring(0, 30)).append("...\n");
                result.append("Rows updated: ").append(updated).append("\n\n");

                // Also update manager and user if they exist
                updateUserPassword("manager", passwordHash, result);
                updateUserPassword("user", passwordHash, result);

            } else {
                // Create new admin user (you'll need to implement insert method in DAO)
                result.append("Admin user not found. You need to:\n");
                result.append("1. Stop the application\n");
                result.append("2. Update data.sql with this hash:\n");
                result.append(passwordHash).append("\n");
                result.append("3. Restart the application\n\n");
            }

            // Test the hash
            boolean matches = encoder.matches("password", passwordHash);
            result.append("Hash verification: password 'password' matches = ").append(matches).append("\n");

            result.append("\n=== SETUP COMPLETE ===\n");
            result.append("Now try login with:\n");
            result.append("Username: admin\n");
            result.append("Password: password\n");

            return result.toString();

        } catch (Exception e) {
            return "Setup failed: " + e.getMessage() + "\n\n" + e.getStackTrace()[0];
        }
    }

    private void updateUserPassword(String username, String passwordHash, StringBuilder result) {
        try {
            Optional<AdminLoginInfo> user = adminLoginDAO.findByUsername(username);
            if (user.isPresent()) {
                adminLoginDAO.updatePassword(user.get().getAdminId(), passwordHash);
                result.append("Updated ").append(username).append(" password\n");
            }
        } catch (Exception e) {
            result.append("Failed to update ").append(username).append(": ").append(e.getMessage()).append("\n");
        }
    }

    @GetMapping("/check-password")
    public String checkPassword() {
        try {
            // Get admin user from database
            Optional<AdminLoginInfo> adminOpt = adminLoginDAO.findByUsername("admin");

            if (adminOpt.isEmpty()) {
                return "❌ Admin user not found in database!\n" +
                        "Run POST /api/test/setup-database first.";
            }

            AdminLoginInfo admin = adminOpt.get();
            String storedHash = admin.getPasswordHash();

            // Create encoder (same as in SecurityConfig)
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

            // Test password "password"
            boolean matches = encoder.matches("password", storedHash);

            return String.format(
                    "🔍 DATABASE PASSWORD CHECK:\n" +
                            "===========================\n" +
                            "Username: %s\n" +
                            "Admin ID: %s\n" +
                            "Role: %s\n" +
                            "Active: %s\n\n" +
                            "📊 HASH INFORMATION:\n" +
                            "Stored Hash: %s...\n" +
                            "Hash Length: %d characters\n" +
                            "Hash Format: %s\n\n" +
                            "🔐 PASSWORD TEST:\n" +
                            "Test Password: 'password'\n" +
                            "Password Matches: %s\n\n" +
                            "💡 RECOMMENDATION:\n" +
                            "%s",
                    admin.getUsername(),
                    admin.getAdminId(),
                    admin.getRole(),
                    admin.getIsActive() ? "✅ Active" : "❌ Inactive",
                    storedHash.substring(0, Math.min(30, storedHash.length())),
                    storedHash.length(),
                    storedHash.startsWith("$2a$12$") ? "✅ Correct BCrypt-12 format" :
                            storedHash.startsWith("$2a$") ? "⚠️ BCrypt but wrong strength" : "❌ Not BCrypt format",
                    matches ? "✅ YES - Login should work!" : "❌ NO - Password mismatch",
                    matches ? "Try login with username: admin, password: password" :
                            "Run POST /api/test/setup-database to fix password hash"
            );

        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    @GetMapping("/list-users")
    public String listUsers() {
        try {
            StringBuilder result = new StringBuilder();
            result.append("👥 ALL USERS IN DATABASE:\n");
            result.append("========================\n\n");

            for (AdminLoginInfo user : adminLoginDAO.selectAll()) {
                result.append(String.format(
                        "ID: %s\n" +
                                "Username: %s\n" +
                                "Email: %s\n" +
                                "Role: %s\n" +
                                "Active: %s\n" +
                                "Hash: %s...\n" +
                                "Hash Length: %d\n" +
                                "------------------------\n",
                        user.getAdminId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getIsActive() ? "✅" : "❌",
                        user.getPasswordHash().substring(0, Math.min(30, user.getPasswordHash().length())),
                        user.getPasswordHash().length()
                ));
            }

            if (adminLoginDAO.selectAll().isEmpty()) {
                result.append("No users found in database!\n");
                result.append("Run POST /api/test/setup-database to create users.");
            }

            return result.toString();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/verify-login")
    public String verifyLogin() {
        try {
            StringBuilder result = new StringBuilder();
            result.append("🔐 LOGIN VERIFICATION TEST\n");
            result.append("=========================\n\n");

            // Test credentials
            String[] testUsers = {"admin", "manager", "user"};
            String testPassword = "password";

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

            for (String username : testUsers) {
                Optional<AdminLoginInfo> userOpt = adminLoginDAO.findByUsername(username);

                if (userOpt.isPresent()) {
                    AdminLoginInfo user = userOpt.get();
                    boolean matches = encoder.matches(testPassword, user.getPasswordHash());

                    result.append(String.format(
                            "User: %s\n" +
                                    "Status: %s\n" +
                                    "Password Match: %s\n" +
                                    "Hash Valid: %s\n" +
                                    "---\n",
                            username,
                            user.getIsActive() ? "✅ Active" : "❌ Inactive",
                            matches ? "✅ YES" : "❌ NO",
                            user.getPasswordHash().startsWith("$2a$12$") ? "✅ BCrypt-12" : "❌ Wrong format"
                    ));
                } else {
                    result.append(String.format("User: %s - ❌ NOT FOUND\n---\n", username));
                }
            }

            return result.toString();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}