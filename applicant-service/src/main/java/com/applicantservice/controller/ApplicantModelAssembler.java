package com.applicantservice.assembler;

import com.applicantservice.controller.ApplicantController;
import com.applicantservice.dto.ApplicantResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ApplicantModelAssembler implements RepresentationModelAssembler<ApplicantResponse, EntityModel<ApplicantResponse>> {

    @Override
    public EntityModel<ApplicantResponse> toModel(ApplicantResponse applicantResponse) {
        EntityModel<ApplicantResponse> model = EntityModel.of(applicantResponse);
        model.add(linkTo(methodOn(ApplicantController.class)
                .getApplicantById(applicantResponse.getId()))
                .withSelfRel());
        model.add(linkTo(methodOn(ApplicantController.class)
                .getAllApplicants())
                .withRel("all-applicants"));
        try {
            model.add(linkTo(methodOn(ApplicantController.class)
                    .uploadResume(applicantResponse.getId(), null))
                    .withRel("upload-resume"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return model;
    }
}
