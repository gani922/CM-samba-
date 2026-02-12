package com.pramaindia.customer_management.controller;

import com.pramaindia.customer_management.dao.AdminLoginDAO;
import com.pramaindia.customer_management.dto.ForgotPasswordRequestDTO;
import com.pramaindia.customer_management.dto.request.ChangePasswordRequestDTO;
import com.pramaindia.customer_management.dto.request.LoginRequestDTO;
import com.pramaindia.customer_management.dto.request.RefreshTokenRequestDTO;
import com.pramaindia.customer_management.dto.request.ResetPasswordRequestDTO;
import com.pramaindia.customer_management.dto.response.LoginResponseDTO;
import com.pramaindia.customer_management.dto.response.RefreshTokenResponseDTO;
import com.pramaindia.customer_management.model.AdminLoginInfo;
import com.pramaindia.customer_management.security.JwtTokenUtil;
import com.pramaindia.customer_management.service.AdminLoginService;
import com.pramaindia.customer_management.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and User Management APIs")
public class AdminLoginController {

    private final AdminLoginService adminLoginService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private  final AdminLoginDAO adminLoginDAO;


    @Operation(summary = "User Login", description = "Authenticate user and return JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public Response<LoginResponseDTO> login(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody LoginRequestDTO loginRequest,
            HttpServletRequest request) {

        try {
            log.info("Login attempt for user: {}", loginRequest.getUsername());
            LoginResponseDTO loginResponse = adminLoginService.authenticate(loginRequest);
            return Response.success(loginResponse, "Login successful");
        } catch (RuntimeException e) {
            log.warn("Failed login attempt: {}", e.getMessage());
            return Response.failed(401, "Authentication Failed", e.getMessage(), null);
        } catch (Exception e) {
            log.error("Unexpected error during login", e);
            return Response.serverError("An unexpected error occurred during login");
        }
    }

    @Operation(summary = "Refresh Token", description = "Refresh expired access token using refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/refresh-token")
    public Response<RefreshTokenResponseDTO> refreshToken(
            @Parameter(description = "Refresh token request", required = true)
            @Valid @RequestBody RefreshTokenRequestDTO request) {
        try {
            RefreshTokenResponseDTO response = adminLoginService.refreshToken(request.getRefreshToken());
            return Response.success(response, "Token refreshed successfully");
        } catch (RuntimeException e) {
            log.warn("Token refresh failed: {}", e.getMessage());
            return Response.failed(401, "Token Refresh Failed", e.getMessage(), null);
        } catch (Exception e) {
            log.error("Unexpected error during token refresh", e);
            return Response.serverError("An unexpected error occurred during token refresh");
        }
    }

    @Operation(summary = "Get Admin Info", description = "Get information about the logged-in admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin information retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Admin not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/info")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('BU_ADMIN') or hasRole('COUNTRY_ADMIN')")
    public Response<LoginResponseDTO> getAdminInfo(
            @Parameter(description = "Admin ID", required = true)
            @RequestParam String adminId) {
        try {
            LoginResponseDTO data = adminLoginService.getCurrentAdminInfo(adminId);
            return Response.success(data, "Admin information retrieved successfully");
        } catch (RuntimeException e) {
            log.warn("Failed to get admin info: {}", e.getMessage());
            return Response.failed(404, "Admin Info Not Found", e.getMessage(), null);
        } catch (Exception e) {
            log.error("Unexpected error getting admin info", e);
            return Response.serverError("An unexpected error occurred while retrieving admin information");
        }
    }

    @Operation(summary = "Validate Token", description = "Validate JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Token is invalid"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/validate-token")
    public Response<Map<String, Object>> validateToken(
            @Parameter(description = "JWT token to validate", required = true)
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(value = "token", required = false) String tokenParam) {

        try {
            String token = extractToken(authHeader);

            // If token not in header, check query parameter
            if (token == null && tokenParam != null && !tokenParam.isEmpty()) {
                token = tokenParam;
            }

            if (token == null || token.isEmpty()) {
                return Response.failed(400, "Validation Failed",
                        "Token is required. Provide either Authorization header or token parameter", null);
            }

            // First, validate the token structure and expiration
            boolean isValid = jwtTokenUtil.validateToken(token);

            if (!isValid) {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("username", null);
                response.put("expired", true);
                response.put("message", "Token is invalid or expired");
                return Response.success(response, "Token is invalid");
            }

            // Get username from token
            String username = jwtTokenUtil.getUsernameFromToken(token);
            String role = jwtTokenUtil.getRoleFromToken(token);
            Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
            boolean nearingExpiration = jwtTokenUtil.isTokenNearingExpiration(token);

            // Try to load user details to validate against database
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                boolean isUserValid = jwtTokenUtil.validateToken(token);

                Map<String, Object> response = new HashMap<>();
                response.put("valid", isUserValid);
                response.put("username", username);
                response.put("role", role);
                response.put("expired", !isUserValid);
                response.put("nearingExpiration", nearingExpiration);
                response.put("expiration", expiration);

                if (expiration != null) {
                    long remainingMillis = expiration.getTime() - System.currentTimeMillis();
                    long remainingMinutes = remainingMillis / (60 * 1000);
                    response.put("remainingMinutes", Math.max(0, remainingMinutes));
                }

                return Response.success(response, isUserValid ? "Token is valid" : "Token is invalid");

            } catch (UsernameNotFoundException e) {
                // User not found in database
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                response.put("username", username);
                response.put("expired", true);
                response.put("message", "User not found in system");
                return Response.success(response, "User not found");
            }

        } catch (Exception e) {
            log.error("Token validation error", e);
            return Response.serverError("Token validation failed: " + e.getMessage());
        }
    }

    @Operation(summary = "User Logout", description = "Logout user and invalidate token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "400", description = "Invalid token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('BU_ADMIN') or hasRole('COUNTRY_ADMIN')")
    public Response<String> logout(
            @Parameter(description = "Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);

            if (token == null || token.isEmpty()) {
                return Response.failed(400, "Logout Failed", "Authorization token is required", null);
            }

            boolean isLoggedOut = adminLoginService.logout(token);
            if (isLoggedOut) {
                return Response.success("Logout successful");
            } else {
                return Response.failed(400, "Logout Failed", "Invalid token or session already ended", null);
            }
        } catch (Exception e) {
            log.error("Error during logout", e);
            return Response.serverError("An unexpected error occurred during logout");
        }
    }

    @Operation(summary = "Validate Current Session", description = "Check if current session is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session validation result"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/validate-session")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('BU_ADMIN') or hasRole('COUNTRY_ADMIN')")
    public Response<Map<String, Object>> validateSession(
            @Parameter(description = "Admin ID", required = true)
            @RequestParam String adminId) {
        try {
            boolean isValid = adminLoginService.validateCurrentSession(adminId);
            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);
            response.put("adminId", adminId);
            response.put("message", isValid ? "Session is valid" : "Session is invalid");
            return Response.success(response, "Session validation completed");
        } catch (Exception e) {
            log.error("Session validation error", e);
            return Response.serverError("Session validation failed");
        }
    }

    @Operation(summary = "Get Organizations", description = "Get organizations for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organizations retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/organizations")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('BU_ADMIN') or hasRole('COUNTRY_ADMIN')")
    public Response<Object> getOrganizations(
            @Parameter(description = "Admin ID", required = true)
            @RequestParam String adminId) {
        try {
            Object organizations = adminLoginService.getOrganizations(adminId);
            return Response.success(organizations, "Organizations retrieved successfully");
        } catch (Exception e) {
            log.error("Error getting organizations", e);
            return Response.serverError("Failed to retrieve organizations");
        }
    }

    @Operation(summary = "Get Admin Profile", description = "Get detailed admin profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Admin not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('BU_ADMIN') or hasRole('COUNTRY_ADMIN')")
    public Response<Map<String, Object>> getAdminProfile(
            @Parameter(description = "Admin ID", required = true)
            @RequestParam String adminId) {
        try {
            Map<String, Object> profile = adminLoginService.getAdminProfile(adminId);
            return Response.success(profile, "Profile retrieved successfully");
        } catch (RuntimeException e) {
            log.warn("Failed to get admin profile: {}", e.getMessage());
            return Response.failed(404, "Profile Not Found", e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error getting admin profile", e);
            return Response.serverError("Failed to retrieve profile");
        }
    }

    @Operation(summary = "Forgot Password", description = "Send password reset link via email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset link sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/     ")
    public Response<String> forgotPassword(
            @Parameter(description = "Forgot password request", required = true)
            @Valid @RequestBody ForgotPasswordRequestDTO request) {

        try {
            log.info("Forgot password request for email: {}", request.getEmail());

            // Process forgot password through the service with captcha validation
            adminLoginService.processForgotPassword(
                    request.getEmail(),
                    request.getPhoneNumber()

            );

            log.info("Reset link processed for email: {}", request.getEmail());

            // Generic success message to prevent user enumeration
            return Response.success("If the email exists in our system, a reset link has been sent to your email");

        } catch (RuntimeException e) {
            log.warn("Forgot password failed: {}", e.getMessage());
            return Response.failed(400, "Forgot Password Failed", e.getMessage(), null);
        } catch (Exception e) {
            log.error("Unexpected error during forgot password", e);
            return Response.serverError("An unexpected error occurred while processing forgot password request");
        }
    }

    private boolean validateSystemToken(String token, UserDetails userDetails) {
        try {
            // System tokens should have "system" claim
            String tokenType = jwtTokenUtil.getClaimFromToken(token, claims -> claims.get("type", String.class));
            return "system".equals(tokenType) && jwtTokenUtil.validateToken(token);
        } catch (Exception e) {
            log.warn("Invalid system token: {}", e.getMessage());
            return false;
        }
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
    @Operation(summary = "Change Password", description = "Change user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/change-password")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('BU_ADMIN') or hasRole('COUNTRY_ADMIN')")
    public Response<String> changePassword(
            @Parameter(description = "Change password request", required = true)
            @Valid @RequestBody ChangePasswordRequestDTO request,
            @Parameter(hidden = true) Authentication authentication) {

        try {
            log.info("Change password request for user: {}", authentication.getName());

            // Get adminId from the authenticated user
            String username = authentication.getName();
            AdminLoginInfo adminInfo = adminLoginDAO.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Call service to change password
            adminLoginService.changePassword(
                    adminInfo.getAdminId(),
                    request.getCurrentPassword(),
                    request.getNewPassword()
            );

            return Response.success("Password changed successfully");

        } catch (RuntimeException e) {
            log.warn("Password change failed: {}", e.getMessage());
            return Response.failed(400, "Password Change Failed", e.getMessage(), null);
        } catch (Exception e) {
            log.error("Unexpected error during password change", e);
            return Response.serverError("An unexpected error occurred while changing password");
        }
    }

    @Operation(summary = "Reset Password with Token", description = "Reset password using valid reset token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "400", description = "Invalid token or password"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/reset-password")
    public Response<String> resetPassword(
            @Parameter(description = "Reset password request", required = true)
            @Valid @RequestBody ResetPasswordRequestDTO request) {

        try {
            log.info("Reset password request with token");

            // Call service to reset password
            adminLoginService.resetPasswordWithToken(request.getToken(), request.getNewPassword());

            return Response.success("Password has been reset successfully");

        } catch (RuntimeException e) {
            log.warn("Password reset failed: {}", e.getMessage());
            return Response.failed(400, "Password Reset Failed", e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error resetting password", e);
            return Response.serverError("Failed to reset password");
        }
    }
    // In AdminLoginController.java - Add this method
    @GetMapping("/debug/roles")
    @PreAuthorize("permitAll()")
    public Response<Map<String, Object>> debugRoles(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            String role = jwtTokenUtil.getRoleFromToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("role", role);
            response.put("roles_in_token", jwtTokenUtil.getClaimFromToken(token, claims -> claims.get("roles")));

            return Response.success(response, "Debug information");
        } catch (Exception e) {
            return Response.failed(400, "Debug Failed", e.getMessage(), null);
        }
    }
}