//package com.pramaindia.cust_manag.controller;
//
//import com.pramaindia.cust_manag.dto.request.CompanyInfoRequestDTO;
//import com.pramaindia.cust_manag.dto.response.CompanyInfoResponseDTO;
//import com.pramaindia.cust_manag.service.CompanyInfoService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/company-info")
//@Tag(name = "Company Information Management", description = "APIs for managing company information")
//public class CompanyAddressController {
//
//    private final CompanyInfoService companyInfoService;
//
//    @Operation(
//            summary = "Create a new company",
//            description = "Create a new company with the provided details"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "201",
//                    description = "Company created successfully",
//                    content = @Content(schema = @Schema(implementation = CompanyInfoResponseDTO.class))
//            ),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid input data",
//                    content = @Content(schema = @Schema(implementation = Map.class))
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error"
//            )
//    })
//    @PostMapping("/addCompany")
//    public ResponseEntity<CompanyInfoResponseDTO> createCompany(
//            @Parameter(description = "Company information details", required = true)
//            @Valid @RequestBody CompanyInfoRequestDTO request) {
//
//        CompanyInfoResponseDTO response = companyInfoService.createCompany(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @Operation(
//            summary = "Get all companies",
//            description = "Retrieve a list of all companies"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Successfully retrieved all companies",
//                    content = @Content(schema = @Schema(implementation = CompanyInfoResponseDTO.class))
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error"
//            )
//    })
//    @GetMapping("/getAllCompanies")
//    public ResponseEntity<List<CompanyInfoResponseDTO>> getAllCompanies() {
//        return ResponseEntity.ok(companyInfoService.getAllCompanies());
//    }
//
//    @Operation(
//            summary = "Get company by ID",
//            description = "Retrieve a company by its unique ID"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Company found",
//                    content = @Content(schema = @Schema(implementation = CompanyInfoResponseDTO.class))
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Company not found",
//                    content = @Content(schema = @Schema(implementation = Map.class))
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error"
//            )
//    })
//    @GetMapping("/getCompanyById/{id}")
//    public ResponseEntity<CompanyInfoResponseDTO> getCompanyById(
//            @Parameter(description = "Company ID", required = true, example = "1")
//            @PathVariable Long id) {
//        return ResponseEntity.ok(companyInfoService.getCompanyById(id));
//    }
//
//    @Operation(
//            summary = "Get company by SAP ID",
//            description = "Retrieve a company by its SAP ID"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Company found",
//                    content = @Content(schema = @Schema(implementation = CompanyInfoResponseDTO.class))
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Company not found",
//                    content = @Content(schema = @Schema(implementation = Map.class))
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error"
//            )
//    })
//    @GetMapping("/getCompanyBySapId/{sapId}")
//    public ResponseEntity<CompanyInfoResponseDTO> getCompanyBySapId(
//            @Parameter(description = "Company SAP ID", required = true, example = "SAP001")
//            @PathVariable String sapId) {
//        return ResponseEntity.ok(companyInfoService.getCompanyBySapId(sapId));
//    }
//
//    @Operation(
//            summary = "Search companies by name",
//            description = "Search for companies by company name (partial match)"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Companies found",
//                    content = @Content(schema = @Schema(implementation = CompanyInfoResponseDTO.class))
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error"
//            )
//    })
////    @GetMapping("/searchCompanies/{companyName}")
////    public ResponseEntity<List<CompanyInfoResponseDTO>> searchCompaniesByName(
////            @Parameter(description = "Company name (partial match)", required = true, example = "Tech")
////            @PathVariable String companyName) {
////        return ResponseEntity.ok(companyInfoService.searchCompaniesByName(companyName));
////    }
//
////    @Operation(
////            summary = "Update company information",
////            description = "Update an existing company by its ID"
////    )
////    @ApiResponses(value = {
////            @ApiResponse(
////                    responseCode = "200",
////                    description = "Company updated successfully",
////                    content = @Content(schema = @Schema(implementation = CompanyInfoResponseDTO.class))
////            ),
////            @ApiResponse(
////                    responseCode = "400",
////                    description = "Invalid input data",
////                    content = @Content(schema = @Schema(implementation = Map.class))
////            ),
////            @ApiResponse(
////                    responseCode = "404",
////                    description = "Company not found",
////                    content = @Content(schema = @Schema(implementation = Map.class))
////            ),
////            @ApiResponse(
////                    responseCode = "500",
////                    description = "Internal server error"
////            )
////    })
//    @PutMapping("/updateCompany/{id}")
//    public ResponseEntity<CompanyInfoResponseDTO> updateCompany(
//            @Parameter(description = "Company ID", required = true, example = "1")
//            @PathVariable Long id,
//            @Parameter(description = "Updated company details", required = true)
//            @Valid @RequestBody CompanyInfoRequestDTO request) {
//
//        return ResponseEntity.ok(companyInfoService.updateCompany(id, request));
//    }
//
//    @Operation(
//            summary = "Delete company",
//            description = "Delete a company by its ID (soft delete)"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Company deleted successfully",
//                    content = @Content(schema = @Schema(implementation = Map.class))
//            ),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Company not found",
//                    content = @Content(schema = @Schema(implementation = Map.class))
//            ),
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error"
//            )
//    })
//    @DeleteMapping("/deleteCompany/{id}")
//    public ResponseEntity<Map<String, String>> deleteCompany(
//            @Parameter(description = "Company ID", required = true, example = "1")
//            @PathVariable Long id) {
//        companyInfoService.deleteCompany(id);
//        return ResponseEntity.ok(
//                Map.of("message", "Company with ID " + id + " was deleted successfully.")
//        );
//    }
//}