package com.pramaindia.role_management.controller;

import com.pramaindia.customer_management.exception.BusinessException;
import com.pramaindia.role_management.dto.RoleDTO;
import com.pramaindia.role_management.enums.PortalUserRoleType;
import com.pramaindia.role_management.query.DataResult;
import com.pramaindia.role_management.query.PageResult;
import com.pramaindia.role_management.query.RoleQuery;
import com.pramaindia.role_management.service.ResourceServiceImpl;
import com.pramaindia.role_management.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 1. Query roles with pagination and search
     */
    @PostMapping("/queryPage")
    public DataResult<PageResult<List<RoleDTO>>> queryPage(@RequestBody RoleQuery query) {
        try {
            if (query == null) {
                return DataResult.PARAMEMPTY();
            }

            if (query.getPageNum() == null || query.getPageNum() <= 0) {
                query.setPageNum(1);
            }
            if (query.getPageSize() == null || query.getPageSize() <= 0) {
                query.setPageSize(10);
            }

            PageResult<List<RoleDTO>> result = roleService.queryPage(query);
            DataResult<PageResult<List<RoleDTO>>> dataResult = DataResult.SUCCESS(result);
            dataResult.setDtoClass("PageResult<List<RoleDTO>>");
            dataResult.setGenericsClass("RoleDTO");
            return dataResult;
        } catch (IllegalArgumentException e) {
            return DataResult.BAD_REQUEST(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.FAILED(e.getMessage(), e.toString());
        }
    }

    @Autowired
    private ResourceServiceImpl resourceService;

//    @GetMapping("/getAvailableCountries")
//    public ResponseEntity<?> getAvailableCountries() {
//        try {
//            List<Map<String, Object>> countries = roleService.getAvailableCountries();
//            return ResponseEntity.ok()
//                    .header("X-Total-Count", String.valueOf(countries.size()))
//                    .body(countries);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(Map.of(
//                            "error", true,
//                            "message", e.getMessage(),
//                            "timestamp", new Date()
//                    ));
//        }
//    }

    // Optional: Add endpoint to get specific country
    @GetMapping("/countries/{countryId}")
    public ResponseEntity<?> getCountryById(@PathVariable Long countryId) {
        try {
            Map<String, Object> country = roleService.selectCountryById(countryId);
            return ResponseEntity.ok(country);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", true,
                            "message", e.getMessage(),
                            "timestamp", new Date()
                    ));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", true,
                            "message", e.getMessage(),
                            "timestamp", new Date()
                    ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", true,
                            "message", "Internal server error",
                            "details", e.getMessage(),
                            "timestamp", new Date()
                    ));
        }
    }

    // Optional: Add endpoint to get business units
    @GetMapping("/business-units")
    public ResponseEntity<?> getBusinessUnits() {
        try {
            List<Map<String, Object>> businessUnits = roleService.getBusinessUnits();
            return ResponseEntity.ok(businessUnits);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", true,
                            "message", e.getMessage(),
                            "timestamp", new Date()
                    ));
        }
    }


    /**
     * 2. Get available countries
     */
    @GetMapping("/getAvailableCountries")
    public DataResult<List<Map<String, Object>>> getAvailableCountries() {
        try {
            List<Map<String, Object>> countries = roleService.getAvailableCountries();
            if (countries == null || countries.isEmpty()) {
                DataResult<List<Map<String, Object>>> result = DataResult.NOT_FOUND();
                result.setDtoClass("List<Map<String, Object>>");
                return result;
            }
            DataResult<List<Map<String, Object>>> result = DataResult.SUCCESS(countries);
            result.setDtoClass("List<Map<String, Object>>");
            result.setGenericsClass("Map<String, Object>");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.FAILED(e.getMessage(), e.toString());
        }
    }

    /**
     * 5. Add role
     */
    @PostMapping("/addRole")
    public DataResult<Long> addRole(@RequestBody RoleDTO roleCreateDTO) {
        try {
            if (roleCreateDTO == null) {
                return DataResult.PARAMEMPTY();
            }

            if (roleCreateDTO.getRoleName() == null || roleCreateDTO.getRoleName().trim().isEmpty()) {
                return DataResult.BAD_REQUEST("Role name is required");
            }

            Long roleId = roleService.addRole(roleCreateDTO);
            DataResult<Long> result = DataResult.SUCCESS(roleId, "Role created successfully");
            result.setDtoClass("Long");
            return result;
        } catch (IllegalArgumentException e) {
            return DataResult.BAD_REQUEST(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.FAILED(e.getMessage(), e.toString());
        }
    }

    /**
     * 6. Get role detail
     */
    @GetMapping("/getRoleDetail/{id}")
    public DataResult<RoleDTO> getRoleDetail(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return DataResult.BAD_REQUEST("Invalid role ID");
            }

            RoleDTO roleDTO = roleService.getRoleDetail(id);
            if (roleDTO == null) {
                DataResult<RoleDTO> result = DataResult.NOT_FOUND();
                result.setDtoClass("RoleDTO");
                return result;
            }
            DataResult<RoleDTO> result = DataResult.SUCCESS(roleDTO);
            result.setDtoClass("RoleDTO");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.FAILED(e.getMessage(), e.toString());
        }
    }

    /**
     * 7. Delete role
     */
    @PostMapping("/deleteRole/{id}")
    public DataResult<Boolean> deleteRole(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return DataResult.BAD_REQUEST("Invalid role ID");
            }

            boolean result = roleService.deleteRole(id);
            if (result) {
                DataResult<Boolean> dataResult = DataResult.SUCCESS(true, "Role deleted successfully");
                dataResult.setDtoClass("Boolean");
                return dataResult;
            } else {
                return DataResult.NOT_FOUND();
            }
        } catch (RuntimeException e) {
            return DataResult.BAD_REQUEST(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.FAILED(e.getMessage(), e.toString());
        }
    }

    /**
     * 8. Update role
     */
    @PostMapping("/updateRole")
    public DataResult<Boolean> updateRole(@RequestBody RoleDTO roleUpdateDTO) {
        try {
            if (roleUpdateDTO == null || roleUpdateDTO.getId() == null) {
                return DataResult.PARAMEMPTY();
            }

            if (roleUpdateDTO.getRoleName() == null || roleUpdateDTO.getRoleName().trim().isEmpty()) {
                return DataResult.BAD_REQUEST("Role name is required");
            }

            boolean result = roleService.updateRole(roleUpdateDTO);
            if (result) {
                DataResult<Boolean> dataResult = DataResult.SUCCESS(true, "Role updated successfully");
                dataResult.setDtoClass("Boolean");
                return dataResult;
            } else {
                return DataResult.NOT_FOUND();
            }
        } catch (IllegalArgumentException e) {
            return DataResult.BAD_REQUEST(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.FAILED(e.getMessage(), e.toString());
        }
    }

    /**
     * Clone role
     */
    @PostMapping("/cloneRole")
    public DataResult<Boolean> cloneRole(@RequestBody Map<String, Object> request) {
        try {
            Long sourceRoleId = null;
            String newRoleName = null;

            if (request.containsKey("sourceRoleId")) {
                sourceRoleId = Long.valueOf(request.get("sourceRoleId").toString());
            }
            if (request.containsKey("newRoleName")) {
                newRoleName = request.get("newRoleName").toString();
            }

            if (sourceRoleId == null || sourceRoleId <= 0) {
                return DataResult.BAD_REQUEST("Invalid source role ID");
            }
            if (newRoleName == null || newRoleName.trim().isEmpty()) {
                return DataResult.BAD_REQUEST("New role name is required");
            }

            boolean result = roleService.cloneRole(sourceRoleId, newRoleName);
            if (result) {
                DataResult<Boolean> dataResult = DataResult.SUCCESS(true, "Role cloned successfully");
                dataResult.setDtoClass("Boolean");
                return dataResult;
            } else {
                return DataResult.NOT_FOUND();
            }
        } catch (IllegalArgumentException e) {
            return DataResult.BAD_REQUEST(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.FAILED(e.getMessage(), e.toString());
        }
    }

    /**
     * Get all role types with categories
     */
    @GetMapping("/getAllRoleTypes")
    public DataResult<Map<String, Object>> getAllRoleTypes() {
        try {
            Map<String, Object> response = new HashMap<>();

            List<Map<String, Object>> allRoleTypes = new ArrayList<>();
            for (PortalUserRoleType type : PortalUserRoleType.values()) {
                Map<String, Object> roleType = new HashMap<>();
                roleType.put("code", type.getCode());
                roleType.put("name", type.getValue());
                roleType.put("description", type.getValue());
                roleType.put("category", getRoleTypeCategory(type));
                allRoleTypes.add(roleType);
            }

            response.put("allRoleTypes", allRoleTypes);
            response.put("defaultRoleType", PortalUserRoleType.USER_DEFINED.getCode());

            DataResult<Map<String, Object>> result = DataResult.SUCCESS(response);
            result.setDtoClass("Map<String, Object>");
            result.setGenericsClass("PortalUserRoleType");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.FAILED(e.getMessage(), e.toString());
        }
    }

    /**
     * Get enum list
     */
    @GetMapping("/getEnumList")
    public DataResult<List<Map<String, Object>>> getEnumList(@RequestParam String enumCode) {
        try {
            if (enumCode == null || enumCode.trim().isEmpty()) {
                return DataResult.PARAMEMPTY();
            }

            List<Map<String, Object>> enumList = roleService.getEnumList(enumCode);
            if (enumList == null || enumList.isEmpty()) {
                DataResult<List<Map<String, Object>>> result = DataResult.NOT_FOUND();
                result.setDtoClass("List<Map<String, Object>>");
                return result;
            }
            DataResult<List<Map<String, Object>>> result = DataResult.SUCCESS(enumList);
            result.setDtoClass("List<Map<String, Object>>");
            result.setGenericsClass("Map<String, Object>");
            return result;
        } catch (IllegalArgumentException e) {
            return DataResult.BAD_REQUEST(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return DataResult.FAILED(e.getMessage(), e.toString());
        }
    }

    private String getRoleTypeCategory(PortalUserRoleType type) {
        switch (type) {
            case DISTRIBUTOR_COMPANY_ADMIN:
            case DISTRIBUTOR_COMPANY_USER:
                return "Distributor Roles";

            case DPP_COMPANY_ADMIN:
            case DPP_COMPANY_USER:
                return "DPP Partner Roles";

            case NO_DPP_COMPANY_ADMIN:
            case NO_DPP_COMPANY_USER:
                return "Non-Partner Roles";

            case CONSULTANT_COMPANY_ADMIN:
            case CONSULTANT_COMPANY_USER:
                return "Consultant Roles";

            case B2B_ADMIN:
            case B2B_PROCUREMENT:
            case B2B_INVENTORY:
            case B2B_FINANCE:
                return "B2B Roles";

            case OEM_COMPANY_ADMIN:
            case OEM_COMPANY_USER:
                return "OEM Roles";

            case UNAUTHORIZED_COMPANY_USER:
                return "Unauthorized Roles";

            case ANONYMOUS:
                return "System Roles";

            case USER_DEFINED:
            default:
                return "Custom Roles";
        }
    }
}