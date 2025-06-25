package org.tutgi.student_registration.security.dto;

import org.tutgi.student_registration.data.enums.UserType;

public record CustomUserPrincipal (
		Long userId,
		String identifier,
		UserType userType
) {}

