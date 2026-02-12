package com.pramaindia.login.controller;

import com.pramaindia.login.model.dto.ExportRequestDto;
import com.pramaindia.login.model.dto.LoginRecordResponseDto;
import com.pramaindia.login.model.entity.ExportJobEntity;
import com.pramaindia.login.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/portal-user/ssoUserLoginRecord")
@RequiredArgsConstructor
@Tag(name = "Export Management", description = "APIs for exporting login records")
public class ExportController {

    private final ExportService exportService;

    @Operation(summary = "Export records immediately", description = "Export login records as CSV immediately")
    @GetMapping("/export")
    public ResponseEntity<LoginRecordResponseDto> exportRecords(
            @RequestParam(value = "ssoUserLoginRecordSearchJson", required = false) String searchJson) {
        log.info("Export records request: {}", searchJson);

        try {
            String csvData = exportService.exportRecords(searchJson);

            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setData(csvData);
            response.setMessage("Export completed successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error exporting records", e);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setCode(500);
            response.setMessage("Export failed: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Request async export", description = "Request export and get notified via email")
    @PostMapping("/exportRequest")
    public ResponseEntity<LoginRecordResponseDto> requestExport(@RequestBody ExportRequestDto request) {
        log.info("Export request for: {}", request.getUserEmail());

        try {
            String jobId = exportService.requestExport(request.getSearchJson(), request.getUserEmail());

            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setData(jobId);
            response.setMessage("Export requested successfully. Job ID: " + jobId + ". You will receive an email when ready.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error requesting export", e);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setCode(500);
            response.setMessage("Export request failed: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Check export status", description = "Check the status of an export job")
    @GetMapping("/status/{jobId}")
    public ResponseEntity<LoginRecordResponseDto> getExportStatus(@PathVariable String jobId) {
        log.info("Check export status for job: {}", jobId);

        try {
            ExportJobEntity job = exportService.getExportStatus(jobId);

            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setData(job);
            response.setMessage("Job status retrieved successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting export status", e);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setCode(404);
            response.setMessage("Job not found: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(404).body(response);
        }
    }

    @Operation(summary = "Download export file", description = "Download exported CSV file")
    @GetMapping("/download/{jobId}")
    public ResponseEntity<byte[]> downloadExportFile(@PathVariable String jobId) {
        log.info("Download request for job: {}", jobId);

        try {
            byte[] fileBytes = exportService.downloadExportFile(jobId);
            ExportJobEntity job = exportService.getExportStatus(jobId);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + job.getFileName() + "\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(fileBytes);

        } catch (Exception e) {
            log.error("Error downloading export file", e);
            return ResponseEntity.badRequest()
                    .body(("Error: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

    @Operation(summary = "Stream export file", description = "Stream exported CSV file directly")
    @GetMapping("/stream/{jobId}")
    public void streamExportFile(@PathVariable String jobId, HttpServletResponse response) {
        log.info("Stream request for job: {}", jobId);

        try {
            byte[] fileBytes = exportService.downloadExportFile(jobId);
            ExportJobEntity job = exportService.getExportStatus(jobId);

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + job.getFileName() + "\"");
            response.setContentLength(fileBytes.length);

            try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
                response.getOutputStream().flush();
            }

        } catch (Exception e) {
            log.error("Error streaming export file", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                response.getWriter().write("Error: " + e.getMessage());
            } catch (Exception ex) {
                // Ignore
            }
        }
    }

    @Operation(summary = "List export jobs", description = "List all export jobs for a user")
    @GetMapping("/jobs")
    public ResponseEntity<LoginRecordResponseDto> listExportJobs(
            @RequestParam(required = false) String userEmail) {
        log.info("List export jobs for user: {}", userEmail);

        try {
            LoginRecordResponseDto response = exportService.listExportJobs(userEmail);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error listing export jobs", e);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setCode(500);
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Cancel export job", description = "Cancel a pending export job")
    @PostMapping("/cancel/{jobId}")
    public ResponseEntity<LoginRecordResponseDto> cancelExportJob(@PathVariable String jobId) {
        log.info("Cancel export job: {}", jobId);

        try {
            boolean cancelled = exportService.cancelExportJob(jobId);

            LoginRecordResponseDto response = new LoginRecordResponseDto();
            if (cancelled) {
                response.setMessage("Job cancelled successfully");
            } else {
                response.setCode(400);
                response.setMessage("Cannot cancel job. It may already be processing or completed.");
                response.setSuccess(false);
            }
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error cancelling export job", e);
            LoginRecordResponseDto response = new LoginRecordResponseDto();
            response.setCode(500);
            response.setMessage("Error: " + e.getMessage());
            response.setSuccess(false);
            return ResponseEntity.status(500).body(response);
        }
    }


    @Operation(summary = "List export jobs with filters", description = "List export jobs with advanced filters")
    @GetMapping("/jobs/filtered")
    public ResponseEntity<LoginRecordResponseDto> listExportJobsWithFilters(
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {

        LoginRecordResponseDto response = exportService.listExportJobsWithFilters(
                userEmail, status, startDate, endDate, pageNum, pageSize);
        return ResponseEntity.status(response.getCode() == 200 ? 200 : 400).body(response);
    }

    @Operation(summary = "Get export job details", description = "Get detailed information about an export job")
    @GetMapping("/jobs/{jobId}/details")
    public ResponseEntity<LoginRecordResponseDto> getExportJobDetails(@PathVariable String jobId) {

        LoginRecordResponseDto response = exportService.getExportJobDetails(jobId);
        return ResponseEntity.status(response.getCode() == 200 ? 200 : 404).body(response);
    }

    @Operation(summary = "Get user export statistics", description = "Get export statistics for a specific user")
    @GetMapping("/stats/user")
    public ResponseEntity<LoginRecordResponseDto> getUserExportStats(
            @RequestParam String userEmail) {

        LoginRecordResponseDto response = exportService.getUserExportStats(userEmail);
        return ResponseEntity.status(response.getCode() == 200 ? 200 : 400).body(response);
    }
}