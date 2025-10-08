package com.applicantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
@EntityScan(basePackages = {"com.sharepersistence.entity"})
@EnableJpaRepositories(basePackages = {"com.applicantservice.repository"})
@ComponentScan(basePackages = {"com.applicantservice", "com.sharepersistence"})
public class ApplicantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicantServiceApplication.class, args);
    }

}
