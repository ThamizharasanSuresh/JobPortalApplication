package com.applicantservice.controller;

import com.applicantservice.dto.ApplicantRequest;
import com.applicantservice.dto.ApplicantResponse;
import com.applicantservice.dto.ResumeResponse;
import com.applicantservice.repository.ApplicantRepository;
import com.applicantservice.service.ApplicantService;
import com.sharepersistence.dto.ApplicantDTO;
import com.sharepersistence.entity.Applicant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applicants")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;
    private final ApplicantRepository applicantRepository;

    @PostMapping
    public ApplicantResponse createApplicant(@RequestBody ApplicantRequest req) {
        return applicantService.createApplicant(req);
    }

    @GetMapping
    public List<ApplicantResponse> getAll() {
        return applicantService.getAllApplicants();
    }

    // Upload resume file
    @PostMapping("/{id}/resume/upload")
    public ResumeResponse uploadResumeFile(@PathVariable Long id,
                                           @RequestParam("file") MultipartFile file) throws IOException {
        return applicantService.uploadResumeFile(id, file);
    }

    @GetMapping("/{id}")
    public ApplicantDTO getApplicantById(@PathVariable Long id) {
        Optional<Applicant> applicant = applicantRepository.findById(id);
        if (applicant.isEmpty()) {
            return null;
        }
        Applicant a = applicant.get();
        ApplicantDTO dto = new ApplicantDTO();
        dto.setId(a.getId());
        dto.setFirstName(a.getFirstName());
        dto.setLastName(a.getLastName());
        dto.setEmail(a.getEmail());
        dto.setPhone(a.getPhone());
        dto.setEducation(a.getEducation());
        dto.setExperience(a.getExperience());
        return dto;
    }


}

