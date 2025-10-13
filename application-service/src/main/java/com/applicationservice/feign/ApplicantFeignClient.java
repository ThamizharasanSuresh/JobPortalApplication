package com.applicationservice.feign;

import com.applicationservice.config.FeignClientInterceptorConfig;

import com.sharepersistence.dto.ApiResponse;
import com.sharepersistence.dto.ApplicantDTO;
import com.sharepersistence.entity.Applicant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "applicant-service",
        url = "http://localhost:8081/api/applicants",
        configuration = FeignClientInterceptorConfig.class
)
public interface ApplicantFeignClient {
    @GetMapping(value = "/{id}")
    ApiResponse<ApplicantDTO> getApplicantById(@PathVariable("id") Long id);
}