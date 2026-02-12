package com.pramaindia.login.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class LoginRecordRequestDto {

    // Search filters
    private String sapId;
    private String companyName;
    private String loginEmail;
    private String region;
    private String branch;
    private String district;
    private String clientIp;

    // Date range filters (epoch milliseconds)
    private Long startTime;
    private Long endTime;

    // Status filter
    private Integer status; // 1 = active, 0 = logged out

    // Pagination
    @NotNull(message = "Page number cannot be null")
    @Min(value = 1, message = "Page number must be at least 1")
    private Integer pageNum = 1;

    @NotNull(message = "Page size cannot be null")
    @Min(value = 1, message = "Page size must be at least 1")
    @Min(value = 200, message = "Page size cannot exceed 200")
    private Integer pageSize = 10;

    // Sorting
    private String sortField = "login_time"; // login_time, logout_time, company_name, etc.
    private String sortOrder = "DESC"; // ASC or DESC

    // Additional filters
    private Boolean showOnlyActive = false;
    private Boolean showOnlyToday = false;
    private String searchKeyword; // Search across multiple fields

    // Session filters
    private String sessionId;
    private List<Integer> statuses; // Multiple status values
    private Boolean hasLogoutTime; // true = has logout time, false = no logout time

    // Duration filters (in minutes)
    private Integer minSessionDuration;
    private Integer maxSessionDuration;

    // Constructor for default values
    public LoginRecordRequestDto() {
        this.pageNum = 1;
        this.pageSize = 10;
        this.sortField = "login_time";
        this.sortOrder = "DESC";
        this.showOnlyActive = false;
        this.showOnlyToday = false;
    }

    // Builder pattern methods for fluent API
    public static LoginRecordRequestDto builder() {
        return new LoginRecordRequestDto();
    }

    public LoginRecordRequestDto withSapId(String sapId) {
        this.sapId = sapId;
        return this;
    }

    public LoginRecordRequestDto withLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
        return this;
    }

    public LoginRecordRequestDto withPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public LoginRecordRequestDto withPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public LoginRecordRequestDto build() {
        return this;
    }

    // Validation methods
    public boolean hasDateRange() {
        return startTime != null && endTime != null;
    }

    public boolean isValidDateRange() {
        if (!hasDateRange()) return false;
        return endTime > startTime;
    }

    public boolean hasSapIdFilter() {
        return sapId != null && !sapId.trim().isEmpty();
    }

    public boolean hasEmailFilter() {
        return loginEmail != null && !loginEmail.trim().isEmpty();
    }

    public boolean hasCompanyFilter() {
        return companyName != null && !companyName.trim().isEmpty();
    }

    public boolean hasRegionFilter() {
        return region != null && !region.trim().isEmpty();
    }

    public boolean hasSearchKeyword() {
        return searchKeyword != null && !searchKeyword.trim().isEmpty();
    }

    // Getters with defaults
    public Integer getPageNum() {
        return pageNum != null ? pageNum : 1;
    }

    public Integer getPageSize() {
        if (pageSize == null) return 10;
        return Math.min(pageSize, 200); // Cap at 200 for safety
    }

    public String getSortField() {
        if (sortField == null) return "login_time";
        // Validate allowed sort fields
        List<String> allowedFields = List.of(
                "login_time", "logout_time", "company_name",
                "login_email", "region", "branch", "district", "client_ip"
        );
        return allowedFields.contains(sortField.toLowerCase()) ? sortField : "login_time";
    }

    public String getSortOrder() {
        if (sortOrder == null) return "DESC";
        return sortOrder.equalsIgnoreCase("ASC") ? "ASC" : "DESC";
    }

    public Boolean getShowOnlyActive() {
        return showOnlyActive != null ? showOnlyActive : false;
    }

    public Boolean getShowOnlyToday() {
        return showOnlyToday != null ? showOnlyToday : false;
    }

    // Calculate offset for SQL query
    public Integer getOffset() {
        return (getPageNum() - 1) * getPageSize();
    }

    // Convert epoch milliseconds to Date if needed
    public java.util.Date getStartDate() {
        if (startTime == null) return null;
        return new java.util.Date(startTime);
    }

    public java.util.Date getEndDate() {
        if (endTime == null) return null;
        return new java.util.Date(endTime);
    }

    // Helper to create copy
    public LoginRecordRequestDto copy() {
        LoginRecordRequestDto copy = new LoginRecordRequestDto();
        copy.sapId = this.sapId;
        copy.companyName = this.companyName;
        copy.loginEmail = this.loginEmail;
        copy.region = this.region;
        copy.branch = this.branch;
        copy.district = this.district;
        copy.clientIp = this.clientIp;
        copy.startTime = this.startTime;
        copy.endTime = this.endTime;
        copy.status = this.status;
        copy.pageNum = this.pageNum;
        copy.pageSize = this.pageSize;
        copy.sortField = this.sortField;
        copy.sortOrder = this.sortOrder;
        copy.showOnlyActive = this.showOnlyActive;
        copy.showOnlyToday = this.showOnlyToday;
        copy.searchKeyword = this.searchKeyword;
        copy.sessionId = this.sessionId;
        copy.statuses = this.statuses;
        copy.hasLogoutTime = this.hasLogoutTime;
        copy.minSessionDuration = this.minSessionDuration;
        copy.maxSessionDuration = this.maxSessionDuration;
        return copy;
    }

    // Create request for active sessions
    public static LoginRecordRequestDto createActiveSessionsRequest() {
        LoginRecordRequestDto request = new LoginRecordRequestDto();
        request.setStatus(1);
        request.setSortField("login_time");
        request.setSortOrder("DESC");
        return request;
    }

    // Create request for today's logins
    public static LoginRecordRequestDto createTodayLoginsRequest() {
        LoginRecordRequestDto request = new LoginRecordRequestDto();
        request.setShowOnlyToday(true);
        request.setSortField("login_time");
        request.setSortOrder("DESC");
        return request;
    }

    // Create request for specific session
    public static LoginRecordRequestDto createSessionRequest(String sessionId) {
        LoginRecordRequestDto request = new LoginRecordRequestDto();
        request.setSessionId(sessionId);
        request.setPageSize(1);
        return request;
    }

    @Override
    public String toString() {
        return String.format(
                "LoginRecordRequestDto{" +
                        "sapId='%s', " +
                        "loginEmail='%s', " +
                        "companyName='%s', " +
                        "pageNum=%d, " +
                        "pageSize=%d, " +
                        "startTime=%d, " +
                        "endTime=%d" +
                        "}",
                sapId != null ? sapId : "null",
                loginEmail != null ? loginEmail : "null",
                companyName != null ? companyName : "null",
                getPageNum(),
                getPageSize(),
                startTime != null ? startTime : 0,
                endTime != null ? endTime : 0
        );
    }
}