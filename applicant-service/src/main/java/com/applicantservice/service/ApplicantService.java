package com.applicantservice.service;

import com.applicantservice.AuthFeignClient;
import com.applicantservice.dto.ApplicantRequest;
import com.applicantservice.dto.ApplicantResponse;
import com.applicantservice.dto.ResumeResponse;
import com.applicantservice.repository.ApplicantRepository;
import com.applicantservice.repository.ResumeRepository;
import com.sharepersistence.entity.Applicant;
import com.sharepersistence.entity.Resume;
import com.sharepersistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeParserService resumeParserService;
    private final AuthFeignClient authFeignClient;

    public ApplicantResponse createApplicant(ApplicantRequest req) {
        Applicant applicant = Applicant.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .education(req.getEducation())
                .experience(req.getExperience())
                .build();

        if (req.getUserId() != null) {
            User user = authFeignClient.getUserById(req.getUserId());
            applicant.setUser(user);
        }

        applicantRepository.save(applicant);
        return toResponse(applicant);
    }

    public List<ApplicantResponse> getAllApplicants() {
        return applicantRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ResumeResponse uploadResumeFile(Long applicantId, MultipartFile file) throws IOException {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        String folder = "uploads/";
        Path path = Paths.get(folder + file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        Resume resume = Resume.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .filePath(path.toString())
                .applicant(applicant)
                .build();
        resume = resumeParserService.parseResume(resume);
        resumeRepository.save(resume);
        return new ResumeResponse(
                resume.getId(),
                resume.getFileName(),
                resume.getFileType(),
                resume.getFilePath(),
                resume.getSkills(),
                resume.getEducation(),
                resume.getExperience(),
                applicant.getId()
        );
    }


    private ApplicantResponse toResponse(Applicant a) {
        List<ResumeResponse> resumeList = Optional.ofNullable(a.getResumes())
                .orElse(Collections.emptyList()).stream()
                .map(r -> new ResumeResponse(
                        r.getId(),
                        r.getFileName(),
                        r.getFileType(),
                        r.getFilePath(),
                        r.getSkills(),
                        r.getEducation(),
                        r.getExperience(),
                        a.getId()
                ))
                .toList();
        return new ApplicantResponse(
                a.getId(),
                a.getFirstName(),
                a.getLastName(),
                a.getEmail(),
                a.getPhone(),
                a.getEducation(),
                a.getExperience(),
                a.getUser() != null ? a.getUser().getId() : null,
                resumeList != null ? resumeList : null
        );
    }
}
