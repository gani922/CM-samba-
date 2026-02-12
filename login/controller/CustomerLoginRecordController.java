package com.pramaindia.login.controller;

import com.pramaindia.login.model.dto.AddLoginRequestDto;
import com.pramaindia.login.model.dto.LoginRecordRequestDto;
import com.pramaindia.login.model.dto.LoginRecordResponseDto;
import com.pramaindia.login.model.dto.LogoutRequestDto;
import com.pramaindia.login.service.CustomerLoginRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/portal-user/ssoUser")
@RequiredArgsConstructor
@Tag(name = "Customer Login Records", description = "APIs for managing customer login and logout records")
public class CustomerLoginRecordController {

    private final CustomerLoginRecordService loginRecordService;

    @Operation(summary = "Test endpoint", description = "Check if login records API is working")
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Customer Login Records API is working!");
    }

    @Operation(summary = "Search login records", description = "Get paginated login records with filters")
    @PostMapping("/pageListSsoUserLoginRecord")
    public ResponseEntity<LoginRecordResponseDto> getLoginRecords(@RequestBody LoginRecordRequestDto request) {
        log.info("Search login records request: {}", request);
        try {
            LoginRecordResponseDto response = loginRecordService.getLoginRecords(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching login records", e);
            LoginRecordResponseDto errorResponse = new LoginRecordResponseDto();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error searching records: " + e.getMessage());
            errorResponse.setSuccess(false);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @Operation(summary = "Add login record", description = "Record a new customer login")
    @PostMapping("/addLoginRecord")
    public ResponseEntity<LoginRecordResponseDto> addLoginRecord(@RequestBody AddLoginRequestDto request) {
        log.info("Add login record request: {}", request);
        try {
            loginRecordService.addLoginRecord(request);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setMessage("Login recorded successfully");
            response.setData("Login ID: " + request.getSessionId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error adding login record", e);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setCode(500);
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Add logout record", description = "Record customer logout")
    @PostMapping("/logoutRecord")
    public ResponseEntity<LoginRecordResponseDto> logoutRecord(@RequestBody LogoutRequestDto request) {
        log.info("Logout record request: {}", request);
        try {
            loginRecordService.addLogoutRecord(request);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setMessage("Logout recorded successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error adding logout record", e);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setCode(500);
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Get active sessions", description = "Get all active login sessions")
    @GetMapping("/activeSessions")
    public ResponseEntity<LoginRecordResponseDto> getActiveSessions() {
        try {
            LoginRecordResponseDto response = loginRecordService.getActiveSessions();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting active sessions", e);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setCode(500);
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Get session by ID", description = "Get login session details by session ID")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<LoginRecordResponseDto> getSessionById(@PathVariable String sessionId) {
        try {
            LoginRecordResponseDto response = loginRecordService.getSessionById(sessionId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting session by ID: {}", sessionId, e);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setCode(404);
            response.setMessage("Session not found: " + sessionId);
            response.setSuccess(false);
            return ResponseEntity.status(404).body(response);
        }
    }
}