package org.tutgi.student_registration.features.form.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormResponse {
    private Long id;
    private String academicYear;
    private String number;
    private String code;
    private String stampUrl;
    private boolean isOpen;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
