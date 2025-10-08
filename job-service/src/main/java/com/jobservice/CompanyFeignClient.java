package com.jobservice;



import com.sharepersistence.entity.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "company-service",
        url = "http://localhost:8084/api/companies",
        configuration = com.example.jobservice.config.FeignClientInterceptorConfig.class
)
public interface CompanyFeignClient  {
    @GetMapping("/{id}")
    Company getCompany(@PathVariable("id") Long id);
}

