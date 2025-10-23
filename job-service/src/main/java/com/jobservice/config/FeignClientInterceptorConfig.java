package com.jobservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignClientInterceptorConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                var context = SecurityContextHolder.getContext();
                if (context.getAuthentication() != null &&
                        context.getAuthentication().getCredentials() != null) {
                    String token = context.getAuthentication().getCredentials().toString();
                    template.header("Authorization", "Bearer " + token);
                }
            }
        };
    }
}
