package com.applicationservice.feign;



import com.applicationservice.config.FeignClientInterceptorConfig;
import com.sharepersistence.dto.ApiResponse;
import com.sharepersistence.dto.JobDTORequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "job-service",
        url = "http://localhost:8085/api/jobs",
        configuration = FeignClientInterceptorConfig.class
)
public interface JobFeignClient {
    @GetMapping(value = "/{id}")
    ApiResponse<JobDTORequest> getJobById(@PathVariable("id") Long id);
}