package com.example.companyservice.service;

import com.example.companyservice.AuthFeignClient;
import com.example.companyservice.dto.AllCompanyResponse;
import com.example.companyservice.dto.CompanyRequest;
import com.example.companyservice.dto.CompanyResponse;
import com.example.companyservice.repository.CompanyRepository;
import com.sharepersistence.dto.ApiResponse;
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


    public CompanyResponse createCompany(Long userId, CompanyRequest req) {
        ApiResponse<User> authResponse = null;
        try {
            authResponse = authFeignClient.getUserById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch user from Auth Service: " + e.getMessage());
        }

        if (authResponse == null || !authResponse.isSuccess() || authResponse.getData() == null) {
            throw new RuntimeException("User ID " + userId + " does not exist");
        }

        if (companyRepository.existsByName(req.getName())) {
            throw new RuntimeException("Company name already exists");
        }

        if (companyRepository.existsByUserId(userId)) {
            throw new RuntimeException("This user already has a registered company");
        }

        Company company = new Company();
        company.setName(req.getName());
        company.setDescription(req.getDescription());
        company.setIndustry(req.getIndustry());
        company.setLocation(req.getLocation());
        company.setWebsite(req.getWebsite());
        company.setLogoUrl(req.getLogoUrl());
        company.setUserId(userId);

        companyRepository.save(company);

        return toResponse(company);
    }


    public List<AllCompanyResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(company -> new AllCompanyResponse(
                        company.getId(),
                        company.getName(),
                        company.getDescription(),
                        company.getIndustry(),
                        company.getLocation(),
                        company.getWebsite(),
                        company.getLogoUrl()
                ))
                .collect(Collectors.toList());
    }


    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return toResponse(company);
    }


    public AllCompanyResponse updateCompany(Long id, CompanyRequest req) {
        Company company = companyRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (!company.getName().equals(req.getName()) && companyRepository.existsByName(req.getName())) {
            throw new RuntimeException("Company name already exists");
        }

        company.setName(req.getName());
        company.setDescription(req.getDescription());
        company.setIndustry(req.getIndustry());
        company.setLocation(req.getLocation());
        company.setWebsite(req.getWebsite());
        company.setLogoUrl(req.getLogoUrl());

        companyRepository.save(company);

        return new AllCompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getIndustry(),
                company.getLocation(),
                company.getWebsite(),
                company.getLogoUrl()
        );
    }


    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Company not found"));
        companyRepository.delete(company);
    }

    public CompanyResponse toResponse(Company c) {
        User user = null;

        if (c.getUserId() != null) {
            try {
                ApiResponse<User> userResponse = authFeignClient.getUserById(c.getUserId());
                if (userResponse != null && userResponse.isSuccess()) {
                    user = userResponse.getData();
                } else {
                    System.out.println("Warning: User not found for ID: " + c.getUserId());
                }
            } catch (Exception e) {
                System.out.println("Warning: Error fetching user for company " + c.getId() + ": " + e.getMessage());
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
