package com.jobservice.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class SwagerConfig {
    @Bean
    public OpenAPI customOpenAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter JWT token in the format: Bearer <token>");


        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
        return new OpenAPI()
                .info(new Info()
                        .title("Job Portal Application - Job Service")
                        .version("1.0")
                        .description("This is the API for my Project is Job Portal Application"))
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme));
    }
}
