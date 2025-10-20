package org.tutgi.student_registration.features.students.service;

import org.springframework.core.io.Resource;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.enums.FileType;
import org.tutgi.student_registration.data.enums.SignatureType;
import org.tutgi.student_registration.data.enums.YearType;
import org.tutgi.student_registration.features.profile.dto.request.UploadFileRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.EntranceFormUpdateRequest;
import org.tutgi.student_registration.features.students.dto.request.RegistrationFormRequest;
import org.tutgi.student_registration.features.students.dto.request.SubjectChoiceFormRequest;
import org.tutgi.student_registration.features.students.dto.request.UpdateSubjectChoiceFormRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface StudentService {
	ApiResponse createEntranceForm(final EntranceFormRequest entranceFormRequest);
	ApiResponse updateEntranceForm(final EntranceFormUpdateRequest entranceFormUpdateRequest);
	ApiResponse getEntranceForm();
	
	ApiResponse createSubjectChoiceForm(final SubjectChoiceFormRequest subjectChoiceFormRequest);
	ApiResponse updateSubjectChoiceForm(final UpdateSubjectChoiceFormRequest updateSubjectChoiceFormRequest);
	ApiResponse getSubjectChoiceForm();
	
	ApiResponse updateForRegistratinForm(final RegistrationFormRequest registrationFormRequest);
	ApiResponse getRegistrationForm();
	
	ApiResponse uploadSignatureForETF(final UploadFileRequest fileRequest);
	ApiResponse uploadPayment(final UploadFileRequest fileRequest);
	ApiResponse uploadPhotoForETF(final UploadFileRequest fileRequest);
	
	ApiResponse uploadSignatureForSCF(final UploadFileRequest fileRequest,final SignatureType type,final String guardiaName);
	ApiResponse uploadSignatureForRF(final UploadFileRequest fileRequest,final SignatureType type,final String guardiaName);
	
	Resource retrieveFileForETF(final String filePath,final FileType type);
	Resource retrieveFileForSCF(final String filePath,final SignatureType type);
	Resource retrieveFileForRF(final String filePath,final SignatureType type);
	
	ApiResponse acknowledge() throws JsonProcessingException;
	ApiResponse getReceiptByYear(YearType year);
}
