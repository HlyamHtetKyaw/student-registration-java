package org.tutgi.student_registration.features.file.service.impl;

import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.BadRequestException;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.data.enums.FormType;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.models.education.SubjectChoice;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.data.models.form.RegistrationForm;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.file.dto.response.FileResponse;
import org.tutgi.student_registration.features.file.service.FileService;
import org.tutgi.student_registration.features.users.utils.UserUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final StorageService storageService;
	private final UserUtil userUtil;

	@Override
	public FileResponse downloadFormByType(FormType formType, Long studentId) {
	    Long userId = userUtil.getCurrentUserInternal().userId();
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new EntityNotFoundException("User not found"));

	    Student student = studentRepository.findById(studentId)
	            .orElseThrow(() -> new EntityNotFoundException("Student not found"));

	    if (user.getRole().getName() == RoleName.STUDENT) {
	        Student currentUserStudent = studentRepository.findByUserId(userId);
	        if(currentUserStudent==null) {
	        	throw new EntityNotFoundException("Student not found");
	        }
	        if (!currentUserStudent.getId().equals(studentId)) {
	            throw new BadRequestException("You are not authorized to access this student's form");
	        }
	    }
		
		String filePath = switch (formType) {
		case ENTRANCE_FORM -> {
			EntranceForm entranceForm = student.getEntranceForm();
			if (entranceForm == null || entranceForm.getDocxUrl() == null) {
				throw new EntityNotFoundException("Entrance form not available");
			}
			yield entranceForm.getDocxUrl();
		}
		case SUBJECT_CHOICE -> {
			SubjectChoice subjectChoice = student.getSubjectChoice();
			if (subjectChoice == null || subjectChoice.getDocxUrl() == null) {
				throw new EntityNotFoundException("Subject choice form not available");
			}
			yield subjectChoice.getDocxUrl();
		}
		case REGISTRATION -> {
			RegistrationForm registrationForm = student.getRegistrationForm();
			if (registrationForm == null || registrationForm.getDocxUrl() == null) {
				throw new EntityNotFoundException("Registration form not available");
			}
			yield registrationForm.getDocxUrl();
		}
		};

		String displayName = switch (formType) {
		case ENTRANCE_FORM -> "EntranceForm.docx";
		case SUBJECT_CHOICE -> "SubjectChoiceForm.docx";
		case REGISTRATION -> "RegistrationForm.docx";
		};
		Resource resource = storageService.loadAsResource(filePath);
		return new FileResponse(resource,displayName);
	}
	
	@Override
	public String getFilePathByType(FormType formType, Long studentId) {
		Long userId = userUtil.getCurrentUserInternal().userId();
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new EntityNotFoundException("User not found"));

	    Student student = studentRepository.findById(studentId)
	            .orElseThrow(() -> new EntityNotFoundException("Student not found"));

	    if (user.getRole().getName() == RoleName.STUDENT) {
	        Student currentUserStudent = studentRepository.findByUserId(userId);
	        if(currentUserStudent==null) {
	        	throw new EntityNotFoundException("Student not found");
	        }
	        if (!currentUserStudent.getId().equals(studentId)) {
	            throw new BadRequestException("You are not authorized to access this student's form");
	        }
	    }
	    return switch (formType) {
	        case ENTRANCE_FORM -> {
	            EntranceForm entranceForm = student.getEntranceForm();
	            if (entranceForm == null || entranceForm.getDocxUrl() == null) {
	                throw new EntityNotFoundException("Entrance form not available");
	            }
	            yield entranceForm.getDocxUrl();
	        }
	        case SUBJECT_CHOICE -> {
	            SubjectChoice subjectChoice = student.getSubjectChoice();
	            if (subjectChoice == null || subjectChoice.getDocxUrl() == null) {
	                throw new EntityNotFoundException("Subject choice form not available");
	            }
	            yield subjectChoice.getDocxUrl();
	        }
	        case REGISTRATION -> {
	            RegistrationForm registrationForm = student.getRegistrationForm();
	            if (registrationForm == null || registrationForm.getDocxUrl() == null) {
	                throw new EntityNotFoundException("Registration form not available");
	            }
	            yield registrationForm.getDocxUrl();
	        }
	    };
	}


}
