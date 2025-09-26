package org.tutgi.student_registration.security.service.normal.impl;

import java.util.List;

import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.security.service.normal.Authenticatable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AuthenticatedUser implements Authenticatable {
    private final Long id;
    private final String identifier;
    private final List<RoleName> authorities;
}

