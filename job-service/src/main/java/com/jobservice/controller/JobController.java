package com.jobservice.controller;

import com.jobservice.Kafka.ProducerService;
import com.jobservice.dto.JobRequest;
import com.jobservice.dto.JobResponse;
import com.jobservice.service.JobService;
import com.sharepersistence.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobModelAssembler assembler;
    private final ProducerService producerService;

    @PostMapping
    public ResponseEntity<ApiResponse<EntityModel<JobResponse>>> createJob(@RequestBody JobRequest req) {
        try {
            JobResponse response = jobService.createJob(req.getCompanyId(), req);
            EntityModel<JobResponse> model = assembler.toModel(response);
            producerService.sendJobCreatedEvent(response.getTitle(),response.getCompanyName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Job created successfully", model));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> getAllJobs() {
        List<JobResponse> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(new ApiResponse<>(true, "Jobs fetched successfully", jobs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long id) {
        try {
            JobResponse response = jobService.getJobById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job found", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getJobsByCompany(@PathVariable Long companyId) {
        try {
            List<JobResponse> jobs = jobService.getJobsByCompanyId(companyId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Jobs by company fetched successfully", jobs));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(@PathVariable Long id, @RequestBody JobRequest req) {
        try {
            JobResponse response = jobService.updateJob(id, req);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job updated successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJob(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Job deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<JobResponse>>> searchJobs(@RequestParam("keyword") String keyword) {
        List<JobResponse> jobs = jobService.searchJobs(keyword);
        if (!jobs.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Search results", jobs));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No Jobs found", null));
    }

    @GetMapping("/filter/location")
    public ResponseEntity<ApiResponse<List<JobResponse>>> filterByLocation(@RequestParam("location") String location) {
        List<JobResponse> jobs = jobService.filterByLocation(location);
        if (!jobs.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Filtered by location", jobs));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No Jobs found in this location", null));
    }

    @GetMapping("/filter/employment-type")
    public ResponseEntity<ApiResponse<List<JobResponse>>> filterByEmploymentType(@RequestParam("type") String type) {
        List<JobResponse> jobs = jobService.filterByEmploymentType(type);
        if (!jobs.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Filtered by employment type", jobs));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No Jobs found", null));
    }

    @GetMapping("/filter/company")
    public ResponseEntity<ApiResponse<List<JobResponse>>> filterByCompany(@RequestParam("company") String company) {
        List<JobResponse> jobs = jobService.filterByCompanyName(company);
        if (!jobs.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Filtered by company", jobs));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No Jobs found in this company", null));
    }
}
