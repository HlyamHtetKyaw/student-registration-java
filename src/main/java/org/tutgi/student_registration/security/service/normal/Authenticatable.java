package org.tutgi.student_registration.security.service.normal;

import java.util.List;

import org.tutgi.student_registration.data.enums.RoleName;

public interface Authenticatable {
    Long getId();                   // both Employee and Student have this
    String getIdentifier();         // email for employee, rollNo for student
    List<RoleName> getAuthorities();// roles or permissions
}

