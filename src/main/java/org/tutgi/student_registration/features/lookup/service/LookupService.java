package org.tutgi.student_registration.features.lookup.service;

import org.tutgi.student_registration.config.response.dto.ApiResponse;

public interface LookupService {
	ApiResponse getAllSubjects();
	ApiResponse getAllMajors();
}
