package org.tutgi.student_registration.config.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.tutgi.student_registration.config.event.EntranceFormGenerateEvent;
import org.tutgi.student_registration.config.event.RegistrationFormGenerateEvent;
import org.tutgi.student_registration.config.event.SubjectChoiceFormGenerateEvent;
import org.tutgi.student_registration.data.docsUtils.Docx4jFillerService;
import org.tutgi.student_registration.data.enums.StorageDirectory;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.repositories.AcknowledgementRepository;
import org.tutgi.student_registration.data.repositories.EntranceFormRepository;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.data.repositories.SubjectChoiceRepository;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.students.dto.response.EntranceFormResponse;
import org.tutgi.student_registration.features.students.dto.response.SubjectChoiceResponse;
import org.tutgi.student_registration.features.students.service.StudentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class FormGenerateEventListener {

    private final Docx4jFillerService docxFillerService;
    private final StorageService storageService;
    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final EntranceFormRepository entranceFormRepository;
    private final SubjectChoiceRepository subjectChoiceRepository;
    private final AcknowledgementRepository ackRepository;
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEntranceFormGenerateEvent(EntranceFormGenerateEvent event) {
        Student student = studentRepository.findWithEntranceFormAndRelationsById(event.getStudentId());
        log.info("Received FormGenerateEvent for student: {}", student.getEngName());
        try {
            EntranceFormResponse response = studentService.getEntranceFormResponse(student);

            byte[] filled = docxFillerService.fillEntranceFormTemplate(
                    StorageDirectory.ENTRANCE_FORM_TEMPLATE.getDirectoryName(),
                    response
            );

            String oldPath = student.getEntranceForm().getDocxUrl();
            String newPath;

            if (oldPath == null) {
                newPath = storageService.store(filled, student.getEngName(), StorageDirectory.ENTRANCE_FORM);
            } else {
                newPath = storageService.update(filled, oldPath, student.getEngName(), StorageDirectory.ENTRANCE_FORM);
            }

            student.getEntranceForm().setDocxUrl(newPath);
            entranceFormRepository.save(student.getEntranceForm());
            log.info("Generated DOCX saved at: {}", newPath);
            
        } catch (Exception e) {
            log.error("Failed to generate form for student {}: {}", student.getEngName(), e.getMessage(), e);
        }
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSubjectChoiceFormGenerateEvent(SubjectChoiceFormGenerateEvent event) {
    	Student student = studentRepository.findWithSubjectChoiceAndRelationsById(event.getStudentId());
        log.info("Received FormGenerateEvent for student: {}", student.getEngName());
        try {
            SubjectChoiceResponse response = studentService.getSubjectChoiceFormResponse(student);

            byte[] filled = docxFillerService.fillSubjectChoiceTemplate(
                    StorageDirectory.SUBJECT_CHOICE_TEMPLATE.getDirectoryName(),
                    response
            );

            String oldPath = student.getSubjectChoice().getDocxUrl();
            String newPath;

            if (oldPath == null) {
                newPath = storageService.store(filled, student.getEngName(), StorageDirectory.SUBJECT_CHOICE);
            } else {
                newPath = storageService.update(filled, oldPath, student.getEngName(), StorageDirectory.SUBJECT_CHOICE);
            }

            student.getSubjectChoice().setDocxUrl(newPath);
            subjectChoiceRepository.save(student.getSubjectChoice());
            log.info("Generated DOCX saved at: {}", newPath);
            
        } catch (Exception e) {
            log.error("Failed to generate form for student {}: {}", student.getEngName(), e.getMessage(), e);
        }
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRegistrationFormGenerateEvent(RegistrationFormGenerateEvent event) {
    	Student student = studentRepository.findWithRegistrationAndRelationsById(event.getStudentId());
        log.info("Received FormGenerateEvent for student: {}", student.getEngName());
        try {
            SubjectChoiceResponse response = studentService.getSubjectChoiceFormResponse(student);

            byte[] filled = docxFillerService.fillSubjectChoiceTemplate(
                    StorageDirectory.REGISTRATION_FORM_TEMPLATE.getDirectoryName(),
                    response
            );

            String oldPath = student.getAcknowledgement().getDocxUrl();
            String newPath;

            if (oldPath == null) {
                newPath = storageService.store(filled, student.getEngName(), StorageDirectory.REGISTRATION_FORM);
            } else {
                newPath = storageService.update(filled, oldPath, student.getEngName(), StorageDirectory.REGISTRATION_FORM);
            }

            student.getAcknowledgement().setDocxUrl(newPath);
            ackRepository.save(student.getAcknowledgement());
            log.info("Generated DOCX saved at: {}", newPath);
            
        } catch (Exception e) {
            log.error("Failed to generate form for student {}: {}", student.getEngName(), e.getMessage(), e);
        }
    }
    
}

