package com.applicationservice.controller;


import com.applicationservice.dto.ApplicationRequest;
import com.applicationservice.dto.ApplicationResponse;
import com.applicationservice.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping
    public ApplicationResponse apply(@RequestBody ApplicationRequest req) {
        return applicationService.apply(req);
    }

    @GetMapping(value = "/applicant/{id}")
    public List<ApplicationResponse> getByApplicant(@PathVariable Long id) {
        return applicationService.getByApplicant(id);
    }

    @GetMapping(value = "/job/{id}")
    public List<ApplicationResponse> getByJob(@PathVariable Long id) {
        return applicationService.getByJob(id);
    }
}
