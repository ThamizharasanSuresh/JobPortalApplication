package com.applicantservice.controller;

import com.applicantservice.assembler.ApplicantModelAssembler;
import com.applicantservice.dto.ApplicantRequest;
import com.applicantservice.dto.ApplicantResponse;
import com.applicantservice.dto.ResumeResponse;
import com.applicantservice.repository.ApplicantRepository;
import com.applicantservice.service.ApplicantService;
import com.sharepersistence.dto.ApplicantDTO;
import com.sharepersistence.entity.Applicant;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/applicants")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicantService applicantService;
    private final ApplicantRepository applicantRepository;
    private final ApplicantModelAssembler assembler;

    @PostMapping
    public EntityModel<ApplicantResponse> createApplicant(@RequestBody ApplicantRequest req) {
        ApplicantResponse response = applicantService.createApplicant(req);
        return assembler.toModel(response);
    }

    @GetMapping
    public CollectionModel<EntityModel<ApplicantResponse>> getAll() {
        List<EntityModel<ApplicantResponse>> applicants = applicantService.getAllApplicants()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(applicants,
                linkTo(methodOn(ApplicantController.class).getAll()).withSelfRel());
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

