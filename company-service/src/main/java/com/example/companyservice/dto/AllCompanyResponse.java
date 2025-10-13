package com.example.companyservice.dto;




import com.sharepersistence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllCompanyResponse {
    private Long id;
    private String name;
    private String description;
    private String industry;
    private String location;
    private String website;
    private String logoUrl;
}