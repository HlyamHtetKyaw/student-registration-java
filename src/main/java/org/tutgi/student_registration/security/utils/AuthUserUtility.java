package org.tutgi.student_registration.security.utils;

import java.util.List;

import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.security.service.normal.impl.AuthenticatedUser;

public class AuthUserUtility {
    public static AuthenticatedUser fromUser(User u) {
        return new AuthenticatedUser(
            u.getId(), u.getEmail(),
            List.of(u.getRole().getName())
        );
    }
//
//    public static AuthenticatedUser fromStudent(Students s) {
//        return new AuthenticatedUser(
//            s.getId(), s.getRollNo(),
//            List.of(Authority.STUDENT.name()),
//            UserType.STUDENT
//        );
//    }
}

