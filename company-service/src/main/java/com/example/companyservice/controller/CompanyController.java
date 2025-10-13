package com.example.companyservice.controller;

import com.example.companyservice.dto.AllCompanyResponse;
import com.example.companyservice.dto.CompanyRequest;
import com.example.companyservice.dto.CompanyResponse;
import com.example.companyservice.repository.CompanyRepository;
import com.example.companyservice.service.CompanyService;
import com.sharepersistence.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
            @PathVariable Long userId,
            @RequestBody CompanyRequest req) {

        try {
            CompanyResponse response = companyService.createCompany(userId, req);
            return ResponseEntity.ok(new ApiResponse<>(true, "Company created successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AllCompanyResponse>>> getAllCompanies() {
        List<AllCompanyResponse> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(new ApiResponse<>(true, "Companies fetched successfully", companies));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(@PathVariable Long id) {
        try {
            CompanyResponse response = companyService.getCompanyById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Company found", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AllCompanyResponse>> updateCompany(
            @PathVariable Long id,
            @RequestBody CompanyRequest req) {

        try {
            AllCompanyResponse response = companyService.updateCompany(id, req);
            return ResponseEntity.ok(new ApiResponse<>(true, "Company updated successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Company deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
