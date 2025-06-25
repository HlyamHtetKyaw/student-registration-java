package org.tutgi.student_registration.security.utils;

import java.util.List;

import org.tutgi.student_registration.data.enums.Authority;
import org.tutgi.student_registration.data.enums.UserType;
import org.tutgi.student_registration.data.models.Employee;
import org.tutgi.student_registration.data.models.Students;
import org.tutgi.student_registration.security.service.normal.impl.AuthenticatedUser;

public class AuthUserUtility {
    public static AuthenticatedUser fromEmployee(Employee e) {
        return new AuthenticatedUser(
            e.getId(), e.getEmail(),
            List.of(e.getRole().name()),
            UserType.EMPLOYEE
        );
    }

    public static AuthenticatedUser fromStudent(Students s) {
        return new AuthenticatedUser(
            s.getId(), s.getRollNo(),
            List.of(Authority.STUDENT.name()),
            UserType.STUDENT
        );
    }
}

