package com.pramaindia.role_management.controller;

import com.pramaindia.role_management.dto.AssignRolesRequestDTO;
import com.pramaindia.role_management.query.DataResult;
import com.pramaindia.role_management.service.PortalUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portal-user-role")
@CrossOrigin(origins = "*")
@Tag(name = "Portal User Role Management", description = "APIs for managing user-role assignments")
public class PortalUserRoleController {

    @Autowired
    private PortalUserRoleService portalUserRoleService;

    /**
     * Get all roles for a user
     */
    @GetMapping("/user/{userId}/roles")
    @Operation(
            summary = "Get roles by user ID",
            description = "Retrieve all role IDs assigned to a specific user"
    )
    @Parameters({
            @Parameter(
                    name = "userId",
                    description = "User ID",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved roles",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class),
                            examples = @ExampleObject(
                                    value = "{\"success\": true, \"code\": 200, \"message\": \"Success\", \"data\": [1, 2, 3], \"dtoClass\": \"List<Long>\", \"genericsClass\": \"Long\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class),
                            examples = @ExampleObject(
                                    value = "{\"success\": false, \"code\": 400, \"message\": \"Invalid user ID\", \"dtoClass\": \"List<Long>\", \"genericsClass\": \"Long\"}"
                            )
                    )
            )
    })
    public DataResult<List<Long>> getRolesByUserId(@PathVariable Long userId) {
        try {
            if (userId == null || userId <= 0) {
                DataResult<List<Long>> result = DataResult.BAD_REQUEST("Invalid user ID");
                result.setDtoClass("List<Long>");
                result.setGenericsClass("Long");
                return result;
            }

            List<Long> roleIds = portalUserRoleService.getRoleIdsByUserId(userId);
            DataResult<List<Long>> result = DataResult.SUCCESS(roleIds);
            result.setDtoClass("List<Long>");
            result.setGenericsClass("Long");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            DataResult<List<Long>> result = DataResult.FAILED(e.getMessage(), e.toString());
            result.setDtoClass("List<Long>");
            result.setGenericsClass("Long");
            return result;
        }
    }

    /**
     * Get all users for a role
     */
    @GetMapping("/role/{roleId}/users")
    @Operation(
            summary = "Get users by role ID",
            description = "Retrieve all user IDs assigned to a specific role"
    )
    @Parameters({
            @Parameter(
                    name = "roleId",
                    description = "Role ID",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved users",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class),
                            examples = @ExampleObject(
                                    value = "{\"success\": true, \"code\": 200, \"message\": \"Success\", \"data\": [101, 102, 103], \"dtoClass\": \"List<Long>\", \"genericsClass\": \"Long\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid role ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class),
                            examples = @ExampleObject(
                                    value = "{\"success\": false, \"code\": 400, \"message\": \"Invalid role ID\", \"dtoClass\": \"List<Long>\", \"genericsClass\": \"Long\"}"
                            )
                    )
            )
    })
    public Object getUsersByRoleId(@PathVariable Long roleId) {
        try {
            if (roleId == null || roleId <= 0) {
                DataResult<List<Long>> result = DataResult.BAD_REQUEST("Invalid role ID");
                result.setDtoClass("List<Long>");
                result.setGenericsClass("Long");
                return result;
            }

            List<String> userIds = portalUserRoleService.getUserIdsByRoleId(roleId);
            DataResult<List<String>> result = DataResult.SUCCESS(userIds);
            result.setDtoClass("String");
            result.setGenericsClass("Long");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            DataResult<List<Long>> result = DataResult.FAILED(e.getMessage(), e.toString());
            result.setDtoClass("List<Long>");
            result.setGenericsClass("Long");
            return result;
        }
    }

    /**
     * Assign role to user
     */
    @PostMapping("/assign")
    @Operation(
            summary = "Assign role to user",
            description = "Assign a specific role to a user"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User and role information",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\"userId\": 1, \"roleId\": 2}"
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Role assigned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class),
                            examples = @ExampleObject(
                                    value = "{\"success\": true, \"code\": 200, \"message\": \"Role assigned successfully\", \"data\": true, \"dtoClass\": \"Boolean\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class),
                            examples = @ExampleObject(
                                    value = "{\"success\": false, \"code\": 400, \"message\": \"Invalid user ID\", \"dtoClass\": \"Boolean\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already has this role",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class)
                    )
            )
    })
    public DataResult<Boolean> assignRoleToUser(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Long roleId = request.get("roleId");

            if (userId == null || userId <= 0) {
                DataResult<Boolean> result = DataResult.BAD_REQUEST("Invalid user ID");
                result.setDtoClass("Boolean");
                return result;
            }
            if (roleId == null || roleId <= 0) {
                DataResult<Boolean> result = DataResult.BAD_REQUEST("Invalid role ID");
                result.setDtoClass("Boolean");
                return result;
            }

            boolean result = portalUserRoleService.assignRoleToUser(userId, roleId);
            if (result) {
                DataResult<Boolean> dataResult = DataResult.SUCCESS(true, "Role assigned successfully");
                dataResult.setDtoClass("Boolean");
                return dataResult;
            } else {
                DataResult<Boolean> dataResult = DataResult.FAILED("Failed to assign role");
                dataResult.setDtoClass("Boolean");
                return dataResult;
            }
        } catch (IllegalArgumentException e) {
            DataResult<Boolean> result = DataResult.BAD_REQUEST(e.getMessage());
            result.setDtoClass("Boolean");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            DataResult<Boolean> result = DataResult.FAILED(e.getMessage(), e.toString());
            result.setDtoClass("Boolean");
            return result;
        }
    }

    /**
     * Remove role from user
     */
    @PostMapping("/remove")
    @Operation(
            summary = "Remove role from user",
            description = "Remove a specific role from a user"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User and role information for removal",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            value = "{\"userId\": 1, \"roleId\": 2}"
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Role removed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class),
                            examples = @ExampleObject(
                                    value = "{\"success\": true, \"code\": 200, \"message\": \"Role removed successfully\", \"data\": true, \"dtoClass\": \"Boolean\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User doesn't have this role",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class)
                    )
            )
    })
    public DataResult<Boolean> removeRoleFromUser(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Long roleId = request.get("roleId");

            if (userId == null || userId <= 0) {
                DataResult<Boolean> result = DataResult.BAD_REQUEST("Invalid user ID");
                result.setDtoClass("Boolean");
                return result;
            }
            if (roleId == null || roleId <= 0) {
                DataResult<Boolean> result = DataResult.BAD_REQUEST("Invalid role ID");
                result.setDtoClass("Boolean");
                return result;
            }

            boolean result = portalUserRoleService.removeRoleFromUser(userId, roleId);
            if (result) {
                DataResult<Boolean> dataResult = DataResult.SUCCESS(true, "Role removed successfully");
                dataResult.setDtoClass("Boolean");
                return dataResult;
            } else {
                DataResult<Boolean> dataResult = DataResult.FAILED("Failed to remove role");
                dataResult.setDtoClass("Boolean");
                return dataResult;
            }
        } catch (IllegalArgumentException e) {
            DataResult<Boolean> result = DataResult.BAD_REQUEST(e.getMessage());
            result.setDtoClass("Boolean");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            DataResult<Boolean> result = DataResult.FAILED(e.getMessage(), e.toString());
            result.setDtoClass("Boolean");
            return result;
        }
    }

    /**
     * Remove all roles from user
     */
    @PostMapping("/user/{userId}/remove-all")
    @Operation(
            summary = "Remove all roles from user",
            description = "Remove all role assignments from a specific user"
    )
    @Parameters({
            @Parameter(
                    name = "userId",
                    description = "User ID",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "All roles removed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class),
                            examples = @ExampleObject(
                                    value = "{\"success\": true, \"code\": 200, \"message\": \"All roles removed successfully\", \"data\": true, \"dtoClass\": \"Boolean\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class)
                    )
            )
    })
    public DataResult<Boolean> removeAllRolesFromUser(@PathVariable Long userId) {
        try {
            if (userId == null || userId <= 0) {
                DataResult<Boolean> result = DataResult.BAD_REQUEST("Invalid user ID");
                result.setDtoClass("Boolean");
                return result;
            }

            boolean result = portalUserRoleService.removeAllRolesFromUser(userId);
            if (result) {
                DataResult<Boolean> dataResult = DataResult.SUCCESS(true, "All roles removed successfully");
                dataResult.setDtoClass("Boolean");
                return dataResult;
            } else {
                DataResult<Boolean> dataResult = DataResult.FAILED("Failed to remove all roles");
                dataResult.setDtoClass("Boolean");
                return dataResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            DataResult<Boolean> result = DataResult.FAILED(e.getMessage(), e.toString());
            result.setDtoClass("Boolean");
            return result;
        }
    }

    /**
     * Check if user has role
     */
    @GetMapping("/check")
    @Operation(
            summary = "Check user role assignment",
            description = "Check if a specific user has a particular role"
    )
    @Parameters({
            @Parameter(
                    name = "userId",
                    description = "User ID",
                    required = true,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            ),
            @Parameter(
                    name = "roleId",
                    description = "Role ID",
                    required = true,
                    example = "2",
                    schema = @Schema(type = "integer", format = "int64")
            )
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Check completed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class),
                            examples = @ExampleObject(
                                    value = "{\"success\": true, \"code\": 200, \"message\": \"Success\", \"data\": true, \"dtoClass\": \"Boolean\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DataResult.class)
                    )
            )
    })
    public DataResult<Boolean> hasRole(@RequestParam Long userId,
                                       @RequestParam Long roleId) {
        try {
            if (userId == null || userId <= 0) {
                DataResult<Boolean> result = DataResult.BAD_REQUEST("Invalid user ID");
                result.setDtoClass("Boolean");
                return result;
            }
            if (roleId == null || roleId <= 0) {
                DataResult<Boolean> result = DataResult.BAD_REQUEST("Invalid role ID");
                result.setDtoClass("Boolean");
                return result;
            }

            boolean hasRole = portalUserRoleService.hasRole(userId, roleId);
            DataResult<Boolean> result = DataResult.SUCCESS(hasRole);
            result.setDtoClass("Boolean");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            DataResult<Boolean> result = DataResult.FAILED(e.getMessage(), e.toString());
            result.setDtoClass("Boolean");
            return result;
        }
    }

    /**
     * Assign multiple roles to user
     */
    @PostMapping("/assign-multiple")
    public DataResult<Boolean> assignRolesToUser(@RequestBody AssignRolesRequestDTO request) {
        try {
            Long userId = request.getUserId();
            List<Long> roleIds = request.getRoleIds();

            if (userId == null || userId <= 0) {
                DataResult<Boolean> result = DataResult.BAD_REQUEST("Invalid user ID");
                result.setDtoClass("Boolean");
                return result;
            }
            if (roleIds == null || roleIds.isEmpty()) {
                DataResult<Boolean> result = DataResult.PARAMEMPTY();
                result.setDtoClass("Boolean");
                return result;
            }

            boolean result = portalUserRoleService.assignRolesToUser(userId, roleIds);
            if (result) {
                DataResult<Boolean> dataResult = DataResult.SUCCESS(true, "Roles assigned successfully");
                dataResult.setDtoClass("Boolean");
                dataResult.setGenericsClass("List<Long>");
                return dataResult;
            } else {
                DataResult<Boolean> dataResult = DataResult.FAILED("Failed to assign roles");
                dataResult.setDtoClass("Boolean");
                dataResult.setGenericsClass("List<Long>");
                return dataResult;
            }
        } catch (IllegalArgumentException e) {
            DataResult<Boolean> result = DataResult.BAD_REQUEST(e.getMessage());
            result.setDtoClass("Boolean");
            result.setGenericsClass("List<Long>");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            DataResult<Boolean> result = DataResult.FAILED("Failed to assign roles", e.toString());
            result.setDtoClass("Boolean");
            result.setGenericsClass("List<Long>");
            return result;
        }
    }
}