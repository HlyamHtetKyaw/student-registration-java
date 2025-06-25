package org.tutgi.student_registration.config.beans;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tutgi.student_registration.features.employee.admin.dto.EmployeeRegisterRequest;
import org.tutgi.student_registration.features.employee.admin.dto.StudentRegisterRequest;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Registeration API")
                        .description("API Documentation for Student Registeration")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("TUTGI")
                                .email("hlan1559@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public GroupedOpenApi mainApi() {
        return GroupedOpenApi.builder()
            .group("v1")
            .packagesToScan("org.tutgi.student_registration")
            .build();
    }

}