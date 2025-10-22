package org.tutgi.student_registration.config.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.config.event.FormGenerateEvent;
import org.tutgi.student_registration.data.docsUtils.Docx4jFillerService;
import org.tutgi.student_registration.data.enums.StorageDirectory;
import org.tutgi.student_registration.data.models.Student;
import org.tutgi.student_registration.data.repositories.StudentRepository;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.students.dto.response.EntranceFormResponse;
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

    @Async
    @EventListener
    public void handleFormGenerateEvent(FormGenerateEvent event) {
        Student student = event.getStudent();
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
            studentRepository.save(student);
            log.info("Generated DOCX saved at: {}", newPath);

        } catch (Exception e) {
            log.error("Failed to generate form for student {}: {}", student.getEngName(), e.getMessage(), e);
        }
    }
}

