package com.applicationservice.controller;


import com.applicationservice.dto.ApplicationRequest;
import com.applicationservice.dto.ApplicationResponse;
import com.applicationservice.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ApplicationModelAssembler assembler;

    @PostMapping
    public EntityModel<ApplicationResponse> apply(@RequestBody ApplicationRequest req) {
        ApplicationResponse response = applicationService.apply(req);
        return assembler.toModel(response);
    }

    @GetMapping(value = "/applicant/{id}")
    public CollectionModel<EntityModel<ApplicationResponse>> getByApplicant(@PathVariable Long id) {
        List<EntityModel<ApplicationResponse>> applications = applicationService.getByApplicant(id)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(applications,
                linkTo(methodOn(ApplicationController.class).getByApplicant(id)).withSelfRel(),
                linkTo(methodOn(ApplicationController.class).apply(null)).withRel("apply"));
    }

    @GetMapping(value = "/job/{id}")
    public CollectionModel<EntityModel<ApplicationResponse>> getByJob(@PathVariable Long id) {
        List<EntityModel<ApplicationResponse>> applications = applicationService.getByJob(id)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(applications,
                linkTo(methodOn(ApplicationController.class).getByJob(id)).withSelfRel(),
                linkTo(methodOn(ApplicationController.class).apply(null)).withRel("apply"));
    }
}
