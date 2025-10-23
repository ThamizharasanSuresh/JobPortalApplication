package com.applicantservice.controller;


import com.applicantservice.dto.*;
import com.applicantservice.repository.ApplicantRepository;
import com.applicantservice.service.ApplicantService;
import com.sharepersistence.dto.ApiResponse;
import com.sharepersistence.entity.Applicant;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/applicants")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;
    private final ApplicantRepository applicantRepository;
    private final ApplicantModelAssembler assembler;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createApplicant(@RequestBody ApplicantRequest req) {
        try {
            ApplicantResponse response = applicantService.createApplicant(req);
            EntityModel<ApplicantResponse> entityModel = assembler.toModel(response);
            return ResponseEntity.ok(new ApiResponse<>(true, "Applicant created Profile successfully", entityModel));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllApplicants() {
        List<ApplicantResponse> applicants = applicantService.getAllApplicants();
        return ResponseEntity.ok(new ApiResponse<>(true, "All applicants fetched successfully", applicants));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getApplicantById(@PathVariable Long id) {
        try {
            Applicant response = applicantRepository.findById(id).orElseThrow(()->new RuntimeException("Applicant with id " + id + " not found"));
            return ResponseEntity.ok(new ApiResponse<>(true, "Applicant found", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateApplicant(@PathVariable Long id, @RequestBody ApplicantRequest req) {
        try {
            ApplicantResponse updated = applicantService.updateApplicant(id, req);
            return ResponseEntity.ok(new ApiResponse<>(true, "Applicant updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteApplicant(@PathVariable Long id) {
        try {
            applicantService.deleteApplicant(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Applicant deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/{id}/resume/upload")
    public ResponseEntity<ApiResponse<?>> uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            ResumeResponse response = applicantService.uploadOrReplaceResume(id, file);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resume uploaded successfully", response));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "File upload error: " + e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/resume/update")
    public ResponseEntity<ApiResponse<?>> updateResume(@PathVariable Long id, @RequestBody ResumeUpdateRequest req) {
        try {
            ResumeResponse updated = applicantService.updateResumeDetails(id, req);
            return ResponseEntity.ok(new ApiResponse<>(true, "Resume updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
