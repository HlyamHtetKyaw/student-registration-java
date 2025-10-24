package org.tutgi.student_registration.config.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.tutgi.student_registration.config.event.EntranceFormGenerateEvent;
import org.tutgi.student_registration.config.event.RegistrationFormGenerateEvent;
import org.tutgi.student_registration.config.event.SubjectChoiceFormGenerateEvent;
import org.tutgi.student_registration.config.listener.FormGenerationTracker.StudentFormTracker;
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
    private final FormGenerationTracker formGenerationTracker;
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEntranceFormGenerateEvent(EntranceFormGenerateEvent event) {
        Long studentId = event.getStudentId();
        StudentFormTracker tracker = formGenerationTracker.getTracker(studentId);
        try {
            Student student = studentRepository.findWithEntranceFormAndRelationsById(studentId);
            EntranceFormResponse response = studentService.getEntranceFormResponse(student);

            byte[] filled = docxFillerService.fillEntranceFormTemplate(
                    StorageDirectory.ENTRANCE_FORM_TEMPLATE.getDirectoryName(),
                    response
            );

            String oldPath = student.getEntranceForm().getDocxUrl();
            String newPath = (oldPath == null)
                    ? storageService.store(filled, student.getEngName(), StorageDirectory.ENTRANCE_FORM)
                    : storageService.update(filled, oldPath, student.getEngName(), StorageDirectory.ENTRANCE_FORM);

            student.getEntranceForm().setDocxUrl(newPath);
            entranceFormRepository.save(student.getEntranceForm());
            tracker.entranceForm.complete(newPath);
            log.info("Generated entrance form for {}", student.getEngName());

        } catch (Exception e) {
            tracker.entranceForm.complete(null);
            log.error("Failed to generate entrance form for student {}: {}", studentId, e.getMessage(), e);
        }
    }

    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSubjectChoiceFormGenerateEvent(SubjectChoiceFormGenerateEvent event) {
        Long studentId = event.getStudentId();
        StudentFormTracker tracker = formGenerationTracker.getTracker(studentId);
        try {
            Student student = studentRepository.findWithSubjectChoiceAndRelationsById(studentId);
            SubjectChoiceResponse response = studentService.getSubjectChoiceFormResponse(student);

            byte[] filled = docxFillerService.fillSubjectChoiceTemplate(
                    StorageDirectory.SUBJECT_CHOICE_TEMPLATE.getDirectoryName(),
                    response
            );

            String oldPath = student.getSubjectChoice().getDocxUrl();
            String newPath = (oldPath == null)
                    ? storageService.store(filled, student.getEngName(), StorageDirectory.SUBJECT_CHOICE)
                    : storageService.update(filled, oldPath, student.getEngName(), StorageDirectory.SUBJECT_CHOICE);

            student.getSubjectChoice().setDocxUrl(newPath);
            subjectChoiceRepository.save(student.getSubjectChoice());
            tracker.subjectChoiceForm.complete(newPath);
            log.info("Generated subject choice form for {}", student.getEngName());

        } catch (Exception e) {
            tracker.subjectChoiceForm.complete(null);
            log.error("Failed to generate subject choice form for student {}: {}", studentId, e.getMessage(), e);
        }
    }

    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRegistrationFormGenerateEvent(RegistrationFormGenerateEvent event) {
        Long studentId = event.getStudentId();
        StudentFormTracker tracker = formGenerationTracker.getTracker(studentId);
        try {
            Student student = studentRepository.findWithRegistrationAndRelationsById(studentId);
            SubjectChoiceResponse response = studentService.getSubjectChoiceFormResponse(student);

            byte[] filled = docxFillerService.fillSubjectChoiceTemplate(
                    StorageDirectory.REGISTRATION_FORM_TEMPLATE.getDirectoryName(),
                    response
            );

            String oldPath = student.getAcknowledgement().getDocxUrl();
            String newPath = (oldPath == null)
                    ? storageService.store(filled, student.getEngName(), StorageDirectory.REGISTRATION_FORM)
                    : storageService.update(filled, oldPath, student.getEngName(), StorageDirectory.REGISTRATION_FORM);

            student.getAcknowledgement().setDocxUrl(newPath);
            ackRepository.save(student.getAcknowledgement());
            tracker.registrationForm.complete(newPath);
            log.info("Generated registration form for {}", student.getEngName());

        } catch (Exception e) {
            tracker.registrationForm.complete(null);
            log.error("Failed to generate registration form for student {}: {}", studentId, e.getMessage(), e);
        }
    }

    
}

