package com.applicantservice.service;

import com.applicantservice.AuthFeignClient;
import com.applicantservice.dto.ApplicantRequest;
import com.applicantservice.dto.ApplicantResponse;
import com.applicantservice.dto.ResumeResponse;
import com.applicantservice.dto.ResumeUpdateRequest;
import com.applicantservice.repository.ApplicantRepository;
import com.applicantservice.repository.ResumeRepository;
import com.sharepersistence.entity.Applicant;
import com.sharepersistence.entity.Resume;
import com.sharepersistence.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
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

    private final EntityManager entityManager;


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

            User userRef = entityManager.getReference(User.class, req.getUserId());
            applicant.setUser(userRef);
        }

        applicantRepository.save(applicant);
        return toResponse(applicant);
    }


    public List<ApplicantResponse> getAllApplicants() {
        return applicantRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public ApplicantResponse updateApplicant(Long id, ApplicantRequest req) {
        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Applicant not found with ID: " + id));

        applicant.setFirstName(req.getFirstName());
        applicant.setLastName(req.getLastName());
        applicant.setEmail(req.getEmail());
        applicant.setPhone(req.getPhone());
        applicant.setEducation(req.getEducation());
        applicant.setExperience(req.getExperience());

        applicantRepository.save(applicant);
        return toResponse(applicant);
    }

    public void deleteApplicant(Long id) {
        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Applicant not found with ID: " + id));
        applicantRepository.delete(applicant);
    }

    public ResumeResponse uploadOrReplaceResume(Long applicantId, MultipartFile file) throws IOException {
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        List<Resume> existingResumes = resumeRepository.findByApplicantId(applicantId);
        for (Resume r : existingResumes) {
            try {
                Files.deleteIfExists(Paths.get(r.getFilePath()));
                resumeRepository.delete(r);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        String folder = "uploads/resumes/";
        Path directory = Paths.get(folder);
        Files.createDirectories(directory);


        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = directory.resolve(uniqueFileName);
        Files.write(filePath, file.getBytes());


        Resume resume = Resume.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .filePath(filePath.toString())
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

    public ResumeResponse updateResumeDetails(Long applicantId, ResumeUpdateRequest req) {
        Resume resume = resumeRepository.findFirstByApplicantId(applicantId)
                .orElseThrow(() -> new RuntimeException("Resume not found for applicant ID: " + applicantId));

        if (req.getSkills() != null && !req.getSkills().isEmpty())
            resume.setSkills(req.getSkills());
        if (req.getEducation() != null && !req.getEducation().isEmpty())
            resume.setEducation(req.getEducation());
        if (req.getExperience() != null && !req.getExperience().isEmpty())
            resume.setExperience(req.getExperience());

        Resume updatedResume = resumeRepository.save(resume);

        return new ResumeResponse(
                updatedResume.getId(),
                updatedResume.getFileName(),
                updatedResume.getFileType(),
                updatedResume.getFilePath(),
                updatedResume.getSkills(),
                updatedResume.getEducation(),
                updatedResume.getExperience(),
                updatedResume.getApplicant().getId()
        );
    }

    public ApplicantResponse toResponse(Applicant a) {
        List<ResumeResponse> resumeList = Optional.ofNullable(a.getResumes())
                .orElse(Collections.emptyList())
                .stream()
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
                resumeList
        );
    }
}
