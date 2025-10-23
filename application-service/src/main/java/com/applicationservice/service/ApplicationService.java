package com.applicationservice.service;

import com.applicationservice.Kafka.ProducerService;
import com.applicationservice.dto.ApplicationRequest;
import com.applicationservice.dto.ApplicationResponse;
import com.applicationservice.feign.ApplicantFeignClient;
import com.applicationservice.feign.AuthFeignClient;
import com.applicationservice.feign.JobFeignClient;
import com.applicationservice.repository.ApplicationRepository;
import com.sharepersistence.dto.ApiResponse;
import com.sharepersistence.dto.ApplicantDTO;
import com.sharepersistence.dto.JobDTORequest;
import com.sharepersistence.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import feign.FeignException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicantFeignClient applicantFeignClient;
    private final JobFeignClient jobFeignClient;
    private final AuthFeignClient authFeignClient;
    private final JavaMailSender mailSender;
    private final ProducerService producerService;

    public ApplicationResponse apply(ApplicationRequest req) {
        ApiResponse<ApplicantDTO> applicant;
        ApiResponse<JobDTORequest> job;

        try {
            applicant = applicantFeignClient.getApplicantById(req.getApplicantId());
        } catch (FeignException e) {
            throw new RuntimeException("Applicant not found");
        }

        try {
            job = jobFeignClient.getJobById(req.getJobId());
        } catch (FeignException e) {
            throw new RuntimeException("Job not found");
        }


        Application application = Application.builder()
                .applicant(Applicant.builder().id(applicant.getData().getId()).firstName(applicant.getData().getFirstName()).build())
                .job(Job.builder().id(job.getData().getId()).title(job.getData().getTitle()).company(Company.builder().id(job.getData().getCompanyId()).build()).build())
                .status("Applied")
                .appliedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Long userId = job.getData().getUserId();
        ApiResponse<User> user = authFeignClient.getUserById(userId);

        applicationRepository.save(application);

        sendEmailToCompany(user.getData().getEmail(), applicant.getData().getFirstName(), job.getData().getTitle());

        producerService.sendApplicationSubmittedEvent(applicant.getData().getFirstName().concat(" "+applicant.getData().getLastName()));

        return toResponse(application);
    }


    public List<ApplicationResponse> getByApplicant(Long applicantId) {
        return applicationRepository.findByApplicantId(applicantId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ApplicationResponse> getByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }


    public void deleteApplication(Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        applicationRepository.delete(app);
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

    private void sendEmailToCompany(String companyEmail, String applicantName, String jobTitle) {
        if (companyEmail == null || companyEmail.isEmpty()) {
            System.out.println("companyEmail is null or empty");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(companyEmail);
        message.setSubject("New Job Application: " + jobTitle);
        message.setText("Applicant " + applicantName + " has applied for the job: " + jobTitle);
        message.setFrom(applicantName);
        mailSender.send(message);
        System.out.println("Mail sent to " + companyEmail);
    }
}
