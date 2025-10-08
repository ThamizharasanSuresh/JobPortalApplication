package com.jobservice.service;


import com.jobservice.CompanyFeignClient;
import com.jobservice.dto.JobRequest;
import com.jobservice.dto.JobResponse;
import com.jobservice.repository.JobRepository;
import com.sharepersistence.entity.Company;
import com.sharepersistence.entity.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyFeignClient companyFeignClient;

    public JobResponse createJob(Long id , JobRequest req) {
        System.out.println("its work");
        Company company = companyFeignClient.getCompany(id);
        Job job = Job.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .location(req.getLocation())
                .employmentType(req.getEmploymentType())
                .salary(req.getSalary())
                .skills(req.getSkills())
                .company(company)
                .build();
        jobRepository.save(job);
        return toResponse(job);
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(job.getId(), job.getTitle(), job.getDescription(), job.getLocation(),
                job.getEmploymentType(), job.getSalary(), job.getSkills(),
                job.getCompany() != null ? job.getCompany().getName() : null);
    }


}
