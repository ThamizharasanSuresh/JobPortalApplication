package com.applicationservice.controller;

import com.applicationservice.dto.ApplicationRequest;
import com.applicationservice.dto.ApplicationResponse;
import com.applicationservice.service.ApplicationService;
import com.sharepersistence.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationModelAssembler assembler;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> apply(@RequestBody ApplicationRequest req) {
        try {
            ApplicationResponse response = applicationService.apply(req);
            EntityModel<ApplicationResponse> entityModel = assembler.toModel(response);
            return ResponseEntity.ok(new ApiResponse<>(true, "Application submitted successfully", entityModel));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/applicant/{id}")
    public ResponseEntity<ApiResponse<?>> getByApplicant(@PathVariable Long id) {
        try {
            List<ApplicationResponse> responses = applicationService.getByApplicant(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Applications fetched for applicant", responses));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<ApiResponse<?>> getByJob(@PathVariable Long id) {
        try {
            List<ApplicationResponse> responses = applicationService.getByJob(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Applications fetched for job", responses));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.deleteApplication(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Application deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
