package com.jobservice.controller;


import com.jobservice.dto.JobRequest;
import com.jobservice.dto.JobResponse;
import com.jobservice.repository.JobRepository;
import com.jobservice.service.JobService;
import com.sharepersistence.dto.JobDTO;
import com.sharepersistence.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;
    private final JobRepository jobRepository;
    private final JobModelAssembler assembler;

    @PostMapping
    public EntityModel<JobResponse> createJob(@RequestBody JobRequest req) {
        JobResponse response = jobService.createJob(req.getCompanyId(), req);
        return assembler.toModel(response);
    }

    @GetMapping
    public CollectionModel<EntityModel<JobResponse>> getAll() {
        List<EntityModel<JobResponse>> jobs = jobService.getAllJobs()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(jobs, linkTo(methodOn(JobController.class).getAll()).withSelfRel());
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

    @GetMapping("/search")
    public CollectionModel<EntityModel<JobResponse>> searchJobs(@RequestParam("keyword") String keyword) {
        List<EntityModel<JobResponse>> jobs = jobService.searchJobs(keyword)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(jobs, linkTo(methodOn(JobController.class).searchJobs(keyword)).withSelfRel());
    }

    @GetMapping("/filter/location")
    public CollectionModel<EntityModel<JobResponse>> filterByLocation(@RequestParam("location") String location) {
        List<EntityModel<JobResponse>> jobs = jobService.filterByLocation(location)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(jobs, linkTo(methodOn(JobController.class).filterByLocation(location)).withSelfRel());
    }

    @GetMapping("/filter/employment-type")
    public CollectionModel<EntityModel<JobResponse>> filterByEmploymentType(@RequestParam("type") String type) {
        List<EntityModel<JobResponse>> jobs = jobService.filterByEmploymentType(type)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(jobs, linkTo(methodOn(JobController.class).filterByEmploymentType(type)).withSelfRel());
    }

    @GetMapping("/filter/company")
    public CollectionModel<EntityModel<JobResponse>> filterByCompany(@RequestParam("company") String company) {
        List<EntityModel<JobResponse>> jobs = jobService.filterByCompanyName(company)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(jobs, linkTo(methodOn(JobController.class).filterByCompany(company)).withSelfRel());
    }
}
