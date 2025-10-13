package com.jobservice;



import com.jobservice.dto.CompanyRes;
import com.sharepersistence.entity.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "company-service",
        url = "http://localhost:8084/api/companies"
)
public interface CompanyFeignClient  {
    @GetMapping("/{id}")
    CompanyRes getCompany(@PathVariable("id") Long id);
}

