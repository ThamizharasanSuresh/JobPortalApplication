package com.sharepersistence.dto;

import lombok.Data;

@Data
public class ApplicantDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String education;
    private String experience;
}
