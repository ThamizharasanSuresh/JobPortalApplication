package com.jobservice.controller;


import com.jobservice.controller.JobController;
import com.jobservice.dto.JobResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class JobModelAssembler implements RepresentationModelAssembler<JobResponse, EntityModel<JobResponse>> {

    @Override
    public EntityModel<JobResponse> toModel(JobResponse jobResponse) {
        return EntityModel.of(jobResponse,
                linkTo(methodOn(JobController.class).getJobById(jobResponse.getId())).withSelfRel(),
                linkTo(methodOn(JobController.class).getAllJobs()).withRel("all-jobs"),
                linkTo(methodOn(JobController.class).filterByCompany(jobResponse.getCompanyName())).withRel("company-jobs"),
                linkTo(methodOn(JobController.class).filterByLocation(jobResponse.getLocation())).withRel("jobs-by-location")
        );
    }
}
