package org.tutgi.student_registration.security.dto;

public record CustomUserPrincipal (
		Long userId,
		String identifier
) {}

