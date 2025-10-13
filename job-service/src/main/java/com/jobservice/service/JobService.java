package com.jobservice.service;

import com.jobservice.CompanyFeignClient;
import com.jobservice.dto.CompanyRes;
import com.jobservice.dto.JobRequest;
import com.jobservice.dto.JobResponse;
import com.jobservice.repository.JobRepository;
import com.sharepersistence.entity.Company;
import com.sharepersistence.entity.Job;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyFeignClient companyFeignClient;
    private final EntityManager entityManager;

    public JobResponse createJob(Long companyId, JobRequest req) {
        CompanyRes company = companyFeignClient.getCompany(companyId);
        if (company == null) throw new RuntimeException("Company not found with ID " + companyId);

        Job job = Job.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .location(req.getLocation())
                .employmentType(req.getEmploymentType())
                .salary(req.getSalary())
                .skills(req.getSkills())
                .experience(req.getExperience())
                .company(entityManager.getReference(Company.class, companyId))
                .build();
        jobRepository.save(job);

        return toResponse(job);
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID " + id));
        return toResponse(job);
    }

    public JobResponse updateJob(Long id, JobRequest req) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID " + id));

        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setLocation(req.getLocation());
        job.setEmploymentType(req.getEmploymentType());
        job.setSalary(req.getSalary());
        job.setSkills(req.getSkills());
        job.setExperience(req.getExperience());

        if (!job.getCompany().getId().equals(req.getCompanyId())) {
            CompanyRes company = companyFeignClient.getCompany(req.getCompanyId());
            if (company == null) throw new RuntimeException("Company not found with ID " + req.getCompanyId());
            job.setCompany(entityManager.getReference(Company.class,company.getId()));
        }

        jobRepository.save(job);
        return toResponse(job);
    }

    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with ID " + id));
        jobRepository.delete(job);
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> searchJobs(String keyword) {
        return jobRepository.searchJobsIgnoreCase(keyword == null ? "" : keyword)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> filterByEmploymentType(String employmentType) {
        return jobRepository.findByEmploymentTypeIgnoreCase(employmentType == null ? "" : employmentType)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> filterByLocation(String location) {
        return jobRepository.findByLocationIgnoreCase(location == null ? "" : location)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> filterByCompanyName(String companyName) {
        return jobRepository.findByCompanyNameIgnoreCase(companyName == null ? "" : companyName)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> getJobsByCompanyId(Long companyId) {
        if (companyId == null) throw new RuntimeException("Company ID cannot be null");
        return jobRepository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getSalary(),
                job.getSkills(),
                job.getExperience(),
                job.getCompany() != null ? job.getCompany().getId() : null,
                job.getCompany() !=null?job.getCompany().getName():null,
                job.getCompany() != null ? job.getCompany().getUserId():null
        );
    }
}
