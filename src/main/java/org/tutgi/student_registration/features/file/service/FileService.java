package org.tutgi.student_registration.features.file.service;

import org.tutgi.student_registration.data.enums.FormType;
import org.tutgi.student_registration.features.file.dto.response.FileResponse;

public interface FileService {
	FileResponse downloadFormByType(FormType formType, Long studentId);
	String getFilePathByType(FormType formType, Long studentId);
}
