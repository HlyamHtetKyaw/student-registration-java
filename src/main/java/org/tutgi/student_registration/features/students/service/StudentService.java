package org.tutgi.student_registration.features.students.service;

import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest;

public interface StudentService {
	ApiResponse createEntranceForm(final EntranceFormRequest entranceFormRequest);
	ApiResponse updateEntranceForm(final EntranceFormUpdateRequest entranceFormUpdateRequest);
	ApiResponse getEntranceForm();
	
	ApiResponse createSubjectChoiceForm(final SubjectChoiceFormRequest subjectChoiceFormRequest);
}
