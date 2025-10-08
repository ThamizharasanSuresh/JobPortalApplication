package com.applicationservice.service;


import com.applicationservice.dto.ApplicationRequest;
import com.applicationservice.dto.ApplicationResponse;
import com.applicationservice.feign.ApplicantFeignClient;
import com.applicationservice.feign.JobFeignClient;
import com.applicationservice.repository.ApplicationRepository;

import com.sharepersistence.dto.ApplicantDTO;
import com.sharepersistence.dto.JobDTO;
import com.sharepersistence.entity.Applicant;
import com.sharepersistence.entity.Application;
import com.sharepersistence.entity.Job;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicantFeignClient applicantFeignClient;
    private final JobFeignClient jobFeignClient;

    public ApplicationResponse apply(ApplicationRequest req) {
        // Validate Applicant via applicant-service
        ApplicantDTO applicant = null;
        try {
            applicant = applicantFeignClient.getApplicantById(req.getApplicantId());
        } catch (FeignException e) {
            System.out.println(e.getMessage());
        }

        if (applicant == null || applicant.getId() == null) {
            throw new RuntimeException("Applicant not found");
        }

        JobDTO job = null;
        try {
            job = jobFeignClient.getJobById(req.getJobId());
        } catch (FeignException e) {
            System.out.println(e.getMessage());
        }
        if (job == null || job.getId() == null) {
            throw new RuntimeException("Job not found");
        }

        Application app = Application.builder()
                .applicant(Applicant.builder().id(applicant.getId()).firstName(applicant.getFirstName()).build())
                .job(Job.builder().id(job.getId()).title(job.getTitle()).build())
                .status("Applied")
                .appliedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        applicationRepository.save(app);
        return toResponse(app);
    }

    public List<ApplicationResponse> getByApplicant(Long applicantId) {
        return applicationRepository.findByApplicantId(applicantId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ApplicationResponse> getByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ApplicationResponse toResponse(Application app) {
        return new ApplicationResponse(
                app.getId(),
                app.getApplicant().getId(),
                app.getApplicant().getFirstName(),
                app.getJob().getId(),
                app.getJob().getTitle(),
                app.getStatus(),
                app.getAppliedAt(),
                app.getUpdatedAt()
        );
    }
}
