package com.example.companyservice.controller;


import com.example.companyservice.dto.CompanyRequest;
import com.example.companyservice.dto.CompanyResponse;
import com.example.companyservice.repository.CompanyRepository;
import com.example.companyservice.service.CompanyService;
import com.sharepersistence.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    @PostMapping("/{userId}")
    public CompanyResponse createCompany(@PathVariable Long userId, @RequestBody CompanyRequest req) {
        return companyService.createCompany(userId,req);
    }

    @GetMapping("/all")
    public List<CompanyResponse> getAll() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public CompanyResponse getCompany(@PathVariable Long id) {
        Company company = companyRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("Company not found"));
        return companyService.toResponse(company);
    }

}
