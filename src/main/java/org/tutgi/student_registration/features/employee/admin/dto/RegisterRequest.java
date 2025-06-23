package org.tutgi.student_registration.features.employee.admin.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "userType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmployeeRegisterRequest.class, name = "EMPLOYEE"),
    @JsonSubTypes.Type(value = StudentRegisterRequest.class, name = "STUDENT")
})
public sealed interface RegisterRequest permits EmployeeRegisterRequest, StudentRegisterRequest{

}
