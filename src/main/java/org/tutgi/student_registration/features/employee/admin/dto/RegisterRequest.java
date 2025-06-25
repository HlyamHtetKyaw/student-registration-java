package org.tutgi.student_registration.features.employee.admin.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "userType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmployeeRegisterRequest.class, name = "Employee"),
    @JsonSubTypes.Type(value = StudentRegisterRequest.class, name = "Student")
})
public sealed interface RegisterRequest permits EmployeeRegisterRequest, StudentRegisterRequest{}
