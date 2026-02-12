package com.pramaindia.cust_manag.controller;

import com.pramaindia.cust_manag.dto.request.SapInfoRequestDTO;
import com.pramaindia.cust_manag.dto.request.SapInfoUpdateDTO;
import com.pramaindia.cust_manag.dto.response.ApiResponse;
import com.pramaindia.cust_manag.dto.response.PageResponseDTO;
import com.pramaindia.cust_manag.dto.response.SapInfoResponseDTO;
import com.pramaindia.cust_manag.entity.SapInfo;
import com.pramaindia.cust_manag.service.SapInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/sap-info")
@Validated
@Tag(name = "SAP Information Management", description = "APIs for managing SAP user information")
public class SapInfoController {

    @Autowired
    private SapInfoService sapInfoService;

    @Operation(
            summary = "Get SAP user by SAP ID",
            description = "Retrieve SAP user information by user SAP ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "SAP user found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "SAP user not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/{userSapId}")
    public ResponseEntity<ApiResponse<SapInfoResponseDTO>> getSapInfoBySapId(
            @PathVariable @NotBlank(message = "SAP ID is required")
            @Parameter(description = "User SAP ID", required = true, example = "SAP001")
            String userSapId) {
        try {
            SapInfoResponseDTO responseDTO = sapInfoService.getSapInfoBySapId(userSapId);
            return ResponseEntity.ok(ApiResponse.success(responseDTO));
        } catch (Exception e) {
            log.error("Error getting SAP info by SAP ID: {}", userSapId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get all SAP users with pagination",
            description = "Retrieve all SAP users with pagination support"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "SAP users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PageResponseDTO<SapInfoResponseDTO>>> getAllSapInfo(
            @Valid
            @Parameter(description = "Pagination and filter parameters", required = true)
            SapInfoRequestDTO requestDTO) {
        try {
            PageResponseDTO<SapInfoResponseDTO> response = sapInfoService.getAllSapInfo(requestDTO);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("Error getting all SAP info", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Search SAP users",
            description = "Search SAP users with various filter criteria"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PageResponseDTO<SapInfoResponseDTO>>> searchSapInfo(
            @RequestBody @Valid
            @Parameter(description = "Search criteria", required = true)
            SapInfoRequestDTO requestDTO) {
        try {
            PageResponseDTO<SapInfoResponseDTO> response = sapInfoService.searchSapInfo(requestDTO);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("Error searching SAP info", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Create new SAP user",
            description = "Create a new SAP user with the provided information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "SAP user created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data or duplicate user",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SapInfoResponseDTO>> createSapInfo(
            @RequestBody @Valid
            @Parameter(description = "SAP user details", required = true)
            SapInfo sapInfo) {
        try {
            SapInfoResponseDTO responseDTO = sapInfoService.createSapInfo(sapInfo);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(responseDTO));
        } catch (Exception e) {
            log.error("Error creating SAP info", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Update SAP user",
            description = "Update existing SAP user information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "SAP user updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "SAP user not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<SapInfoResponseDTO>> updateSapInfo(
            @RequestBody @Valid
            @Parameter(description = "Updated SAP user details", required = true)
            SapInfoUpdateDTO updateDTO) {
        try {
            SapInfoResponseDTO responseDTO = sapInfoService.updateSapInfo(updateDTO);
            return ResponseEntity.ok(ApiResponse.success(responseDTO));
        } catch (Exception e) {
            log.error("Error updating SAP info", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Delete SAP user",
            description = "Soft delete a SAP user by SAP ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "SAP user deleted successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "SAP user not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @DeleteMapping("/{userSapId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSapInfo(
            @PathVariable @NotBlank(message = "SAP ID is required")
            @Parameter(description = "User SAP ID", required = true, example = "SAP001")
            String userSapId,
            @RequestParam @NotBlank(message = "Modified by is required")
            @Parameter(description = "User who is performing the delete", required = true, example = "admin")
            String modifiedBy) {
        try {
            boolean result = sapInfoService.deleteSapInfo(userSapId, modifiedBy);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("Error deleting SAP info: {}", userSapId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Update user lock status",
            description = "Lock or unlock a SAP user account"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lock status updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "SAP user not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PutMapping("/lock-status/{userSapId}")
    public ResponseEntity<ApiResponse<Boolean>> updateLockStatus(
            @PathVariable @NotBlank(message = "SAP ID is required")
            @Parameter(description = "User SAP ID", required = true, example = "SAP001")
            String userSapId,
            @RequestParam @NotBlank(message = "Lock status is required")
            @Parameter(description = "Lock status (Y/N)", required = true, example = "Y", schema = @Schema(allowableValues = {"Y", "N"}))
            String isLock,
            @RequestParam @NotBlank(message = "Modified by is required")
            @Parameter(description = "User who is performing the update", required = true, example = "admin")
            String modifiedBy) {
        try {
            boolean result = sapInfoService.updateLockStatus(userSapId, isLock, modifiedBy);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("Error updating lock status for SAP info: {}", userSapId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get SAP user by email",
            description = "Retrieve SAP user information by email address"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "SAP user found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "SAP user not found with this email",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<SapInfoResponseDTO>> getSapInfoByEmail(
            @PathVariable @NotBlank(message = "Email is required")
            @Parameter(description = "Email address", required = true, example = "user@example.com")
            String email) {
        try {
            SapInfoResponseDTO responseDTO = sapInfoService.getSapInfoByEmail(email);
            return ResponseEntity.ok(ApiResponse.success(responseDTO));
        } catch (Exception e) {
            log.error("Error getting SAP info by email: {}", email, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get SAP user by mobile number",
            description = "Retrieve SAP user information by mobile number"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "SAP user found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "SAP user not found with this mobile number",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/mobile/{mobile}")
    public ResponseEntity<ApiResponse<SapInfoResponseDTO>> getSapInfoByMobile(
            @PathVariable @NotBlank(message = "Mobile number is required")
            @Parameter(description = "Mobile number", required = true, example = "9876543210")
            String mobile) {
        try {
            SapInfoResponseDTO responseDTO = sapInfoService.getSapInfoByMobile(mobile);
            return ResponseEntity.ok(ApiResponse.success(responseDTO));
        } catch (Exception e) {
            log.error("Error getting SAP info by mobile: {}", mobile, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get SAP user by PAN number",
            description = "Retrieve SAP user information by PAN number"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "SAP user found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "SAP user not found with this PAN number",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/pan/{panNumber}")
    public ResponseEntity<ApiResponse<SapInfoResponseDTO>> getSapInfoByPanNumber(
            @PathVariable @NotBlank(message = "PAN number is required")
            @Parameter(description = "PAN number", required = true, example = "ABCDE1234F")
            String panNumber) {
        try {
            SapInfoResponseDTO responseDTO = sapInfoService.getSapInfoByPanNumber(panNumber);
            return ResponseEntity.ok(ApiResponse.success(responseDTO));
        } catch (Exception e) {
            log.error("Error getting SAP info by PAN number: {}", panNumber, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get SAP user by GST number",
            description = "Retrieve SAP user information by GST number"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "SAP user found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "SAP user not found with this GST number",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/gst/{gstNumber}")
    public ResponseEntity<ApiResponse<SapInfoResponseDTO>> getSapInfoByGstNumber(
            @PathVariable @NotBlank(message = "GST number is required")
            @Parameter(description = "GST number", required = true, example = "27ABCDE1234F1Z5")
            String gstNumber) {
        try {
            SapInfoResponseDTO responseDTO = sapInfoService.getSapInfoByGstNumber(gstNumber);
            return ResponseEntity.ok(ApiResponse.success(responseDTO));
        } catch (Exception e) {
            log.error("Error getting SAP info by GST number: {}", gstNumber, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get all locked users",
            description = "Retrieve a list of all locked SAP users"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Locked users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/locked-users")
    public ResponseEntity<ApiResponse<List<SapInfoResponseDTO>>> getLockedUsers() {
        try {
            List<SapInfoResponseDTO> responseList = sapInfoService.getLockedUsers();
            return ResponseEntity.ok(ApiResponse.success(responseList));
        } catch (Exception e) {
            log.error("Error getting locked users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get users by user group",
            description = "Retrieve SAP users by user group"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/user-group/{userGroup}")
    public ResponseEntity<ApiResponse<List<SapInfoResponseDTO>>> getUsersByUserGroup(
            @PathVariable @NotBlank(message = "User group is required")
            @Parameter(description = "User group code", required = true, example = "003")
            String userGroup) {
        try {
            List<SapInfoResponseDTO> responseList = sapInfoService.getUsersByUserGroup(userGroup);
            return ResponseEntity.ok(ApiResponse.success(responseList));
        } catch (Exception e) {
            log.error("Error getting users by user group: {}", userGroup, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get users by region",
            description = "Retrieve SAP users by region ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/region/{regionId}")
    public ResponseEntity<ApiResponse<List<SapInfoResponseDTO>>> getUsersByRegion(
            @PathVariable @NotBlank(message = "Region ID is required")
            @Parameter(description = "Region ID", required = true, example = "REG001")
            String regionId) {
        try {
            List<SapInfoResponseDTO> responseList = sapInfoService.getUsersByRegion(regionId);
            return ResponseEntity.ok(ApiResponse.success(responseList));
        } catch (Exception e) {
            log.error("Error getting users by region: {}", regionId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get users by district",
            description = "Retrieve SAP users by district ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/district/{districtId}")
    public ResponseEntity<ApiResponse<List<SapInfoResponseDTO>>> getUsersByDistrict(
            @PathVariable @NotBlank(message = "District ID is required")
            @Parameter(description = "District ID", required = true, example = "DIST001")
            String districtId) {
        try {
            List<SapInfoResponseDTO> responseList = sapInfoService.getUsersByDistrict(districtId);
            return ResponseEntity.ok(ApiResponse.success(responseList));
        } catch (Exception e) {
            log.error("Error getting users by district: {}", districtId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Update last login time",
            description = "Update the last login timestamp for a SAP user"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Last login time updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "SAP user not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PutMapping("/last-login/{userSapId}")
    public ResponseEntity<ApiResponse<Void>> updateLastLoginTime(
            @PathVariable @NotBlank(message = "SAP ID is required")
            @Parameter(description = "User SAP ID", required = true, example = "SAP001")
            String userSapId) {
        try {
            sapInfoService.updateLastLoginTime(userSapId);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            log.error("Error updating last login time: {}", userSapId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(
            summary = "Get users with active special support",
            description = "Retrieve SAP users who have active special support time"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/special-support/active")
    public ResponseEntity<ApiResponse<List<SapInfoResponseDTO>>> getUsersWithActiveSpecialSupport() {
        try {
            List<SapInfoResponseDTO> responseList = sapInfoService.getUsersWithActiveSpecialSupport();
            return ResponseEntity.ok(ApiResponse.success(responseList));
        } catch (Exception e) {
            log.error("Error getting users with active special support", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}