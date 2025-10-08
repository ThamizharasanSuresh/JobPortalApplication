package com.jobservice.controller;


import com.jobservice.dto.JobRequest;
import com.jobservice.dto.JobResponse;
import com.jobservice.repository.JobRepository;
import com.jobservice.service.JobService;
import com.sharepersistence.dto.JobDTO;
import com.sharepersistence.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final JobRepository jobRepository;

    @PostMapping
    public JobResponse createJob(@RequestBody JobRequest req) {
        return jobService.createJob(req.getCompanyId(), req);
    }

    @GetMapping
    public List<JobResponse> getAll() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public JobDTO getJobById(@PathVariable Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isEmpty()) {
            return null;
        }
        Job j = job.get();
        JobDTO dto = new JobDTO();
        dto.setId(j.getId());
        dto.setTitle(j.getTitle());
        dto.setDescription(j.getDescription());
        dto.setLocation(j.getLocation());
        dto.setEmploymentType(j.getEmploymentType());
        dto.setSalary(j.getSalary());
        dto.setSkills(j.getSkills());
        dto.setCompanyname(j.getCompany().getName());
        return dto;
    }
}
