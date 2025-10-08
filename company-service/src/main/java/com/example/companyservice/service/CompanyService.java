package com.example.companyservice.service;



import com.example.companyservice.AuthFeignClient;
import com.example.companyservice.dto.CompanyRequest;
import com.example.companyservice.dto.CompanyResponse;
import com.example.companyservice.repository.CompanyRepository;
import com.sharepersistence.entity.Company;
import com.sharepersistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final AuthFeignClient authFeignClient;

    public CompanyResponse createCompany(Long Id, CompanyRequest req) {
        Company company = new Company();
        company.setName(req.getName());
        company.setDescription(req.getDescription());
        company.setIndustry(req.getIndustry());
        company.setLocation(req.getLocation());
        company.setWebsite(req.getWebsite());
        company.setLogoUrl(req.getLogoUrl());
        company.setUserId(Id);
        companyRepository.save(company);
        return toResponse(company);
    }

    public CompanyResponse getcompanybyId(Long Id){
        Company com = companyRepository.findById(Math.toIntExact(Id)).orElseThrow(()->new RuntimeException("Company Not Found"));
        return toResponse(com);
    }
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CompanyResponse toResponse(Company c) {
        User user = null;
        if (c.getUserId() != null) {
            try {
                user = authFeignClient.getUserById(c.getUserId());
            } catch (Exception e) {
                // Log warning and proceed
                System.out.println("User not found for ID: " + c.getUserId());
            }
        }

        return new CompanyResponse(
                c.getId(),
                c.getName(),
                c.getDescription(),
                c.getIndustry(),
                c.getLocation(),
                c.getWebsite(),
                c.getLogoUrl(),
                user
        );
    }

}
