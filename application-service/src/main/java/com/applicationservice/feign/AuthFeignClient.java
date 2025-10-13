package com.applicationservice.feign;

import com.sharepersistence.dto.ApiResponse;
import com.sharepersistence.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "http://localhost:8083/api/auth")
public interface AuthFeignClient {
    @GetMapping("/users/{id}")
    ApiResponse<User> getUserById(@PathVariable("id") Long id);
}