package com.example.companyservice.controller;


import com.example.companyservice.dto.CompanyRequest;
import com.example.companyservice.dto.CompanyResponse;
import com.example.companyservice.repository.CompanyRepository;
import com.example.companyservice.service.CompanyService;
import com.sharepersistence.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyModelAssembler assembler;

    @PostMapping("/{userId}")
    public EntityModel<CompanyResponse> createCompany(@PathVariable Long userId, @RequestBody CompanyRequest req) {
        CompanyResponse response = companyService.createCompany(userId, req);
        return assembler.toModel(response);
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<CompanyResponse>> getAll() {
        List<EntityModel<CompanyResponse>> companies = companyService.getAllCompanies()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(companies,
                linkTo(methodOn(CompanyController.class).getAll()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<CompanyResponse> getCompany(@PathVariable Long id) {
        CompanyResponse response = companyService.getcompanybyId(id);
        return assembler.toModel(response);
    }

}
