package com.applicationservice.controller;


import com.applicationservice.controller.ApplicationController;
import com.applicationservice.dto.ApplicationResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ApplicationModelAssembler implements RepresentationModelAssembler<ApplicationResponse, EntityModel<ApplicationResponse>> {

    @Override
    public EntityModel<ApplicationResponse> toModel(ApplicationResponse applicationResponse) {
        return EntityModel.of(applicationResponse,
                linkTo(methodOn(ApplicationController.class).apply(null)).withSelfRel(),
                linkTo(methodOn(ApplicationController.class).getByApplicant(applicationResponse.getApplicantId())).withRel("Get Application"),
                linkTo(methodOn(ApplicationController.class).deleteApplication(applicationResponse.getId())).withRel("Delete Application")
        );
    }
}
