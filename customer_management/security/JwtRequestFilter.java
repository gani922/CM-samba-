package com.pramaindia.customer_management.security;

import com.pramaindia.customer_management.dao.AdminLoginDAO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final AdminLoginDAO adminLoginDAO;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Debug logging
        log.debug("Checking filter for: {} {}", method, path);

        // Skip JWT filter for:
        // 1. Public API endpoints
        // 2. Static resources
        // 3. Frontend pages
        // 4. Documentation

        // Check if path matches any public pattern
        boolean shouldSkip =
                // Public API endpoints
                path.equals("/api/admin/login") ||
                        path.equals("/api/admin/refresh-token") ||
                        path.equals("/api/admin/forgot-password") ||
                        path.equals("/api/admin/reset-password") ||
                        path.equals("/api/admin/validate-token") ||

                        // Debug and test endpoints
                        path.startsWith("/api/debug/") ||
                        path.startsWith("/api/test/") ||
                        path.startsWith("/api/simple/") ||

                        // Generate token endpoints
                        path.startsWith("/api/generate-token") ||
                        path.startsWith("/api/generate-all-tokens") ||
                        path.startsWith("/api/validate-token") ||

                        // H2 Console
                        path.startsWith("/h2-console") ||

                        // Documentation
                        path.startsWith("/swagger-ui") ||
                        path.startsWith("/v3/api-docs") ||
                        path.startsWith("/webjars") ||
                        path.startsWith("/swagger-resources") ||
                        path.equals("/swagger-ui.html") ||

                        // Actuator
                        path.startsWith("/actuator") ||

                        // Frontend paths
                        path.equals("/") ||                      // Root
                        path.equals("/login") ||                 // /login endpoint
                        path.equals("/login.html") ||            // login.html file
                        path.equals("/index.html") ||            // index.html file
                        path.equals("/dashboard") ||             // /dashboard endpoint
                        path.equals("/dashboard.html") ||        // dashboard.html file
                        path.equals("/reset-password") ||        // reset password page
                        path.equals("/com/pramaindia/login") ||  // old login path

                        // Static resource patterns
                        path.startsWith("/css/") ||
                        path.startsWith("/js/") ||
                        path.startsWith("/assets/") ||
                        path.startsWith("/images/") ||
                        path.startsWith("/static/") ||

                        // File extensions
                        path.endsWith(".html") ||
                        path.endsWith(".css") ||
                        path.endsWith(".js") ||
                        path.endsWith(".png") ||
                        path.endsWith(".jpg") ||
                        path.endsWith(".jpeg") ||
                        path.endsWith(".gif") ||
                        path.endsWith(".ico") ||
                        path.endsWith(".svg") ||

                        // Favicon
                        path.equals("/favicon.ico") ||

                        // Error page
                        path.equals("/error");

        log.debug("Should skip filter for {}: {}", path, shouldSkip);
        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String requestPath = request.getRequestURI();
        log.debug("Processing JWT for: {}", requestPath);

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // Skip if no Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Bearer token found, proceeding with filter chain");
            chain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);

        try {
            // Check token blacklist
            if (adminLoginDAO.isTokenBlacklisted(jwtToken) > 0) {
                log.warn("Blacklisted token attempt");
                sendErrorResponse(response, 401, "Token has been revoked");
                return;
            }

            username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            // Add warning header if token is nearing expiration
            if (jwtTokenUtil.isTokenNearingExpiration(jwtToken)) {
                response.setHeader("X-Token-Expiring-Soon", "true");
            }

        } catch (ExpiredJwtException e) {
            log.warn("JWT Token expired");
            sendErrorResponse(response, 401, "Token has expired");
            return;
        } catch (Exception e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            sendErrorResponse(response, 401, "Invalid token");
            return;
        }

        // Validate username and token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(jwtToken)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authenticated user: {}", username);
                } else {
                    log.warn("Token validation failed for user: {}", username);
                    sendErrorResponse(response, 401, "Invalid token");
                    return;
                }
            } catch (Exception e) {
                log.error("Cannot set user authentication: {}", e.getMessage());
                sendErrorResponse(response, 401, "User not found");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format(
                "{\"success\": false, \"code\": %d, \"message\": \"%s\", \"timestamp\": \"%s\"}",
                status, message, new java.util.Date().toString()
        );
        response.getWriter().write(jsonResponse);
    }
}