package com.example.companyservice.controller;


import com.example.companyservice.controller.CompanyController;
import com.example.companyservice.dto.CompanyResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CompanyModelAssembler implements RepresentationModelAssembler<CompanyResponse, EntityModel<CompanyResponse>> {

    @Override
    public EntityModel<CompanyResponse> toModel(CompanyResponse company) {
        return EntityModel.of(company,
                linkTo(methodOn(CompanyController.class).getCompany(company.getId())).withSelfRel(),
                linkTo(methodOn(CompanyController.class).getAllCompanies()).withRel("companies"));
    }
}
