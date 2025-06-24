package org.tutgi.student_registration.features.employee.admin.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "userType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmployeeRegisterRequest.class, name = "Employee"),
    @JsonSubTypes.Type(value = StudentRegisterRequest.class, name = "Student")
})
@Schema(
    description = "Base interface for user creation data by admin",
    discriminatorProperty = "userType",
    discriminatorMapping = {
        @DiscriminatorMapping(value = "Employee", schema = EmployeeRegisterRequest.class),
        @DiscriminatorMapping(value = "Student", schema = StudentRegisterRequest.class)
    }
)
public sealed interface RegisterRequest permits EmployeeRegisterRequest, StudentRegisterRequest{
    @Schema(
        description = "Type of user being registered",
        example = "Student",
        allowableValues = {"Student", "Employee"},
        type="string"
    )
    String getUserType();
}
