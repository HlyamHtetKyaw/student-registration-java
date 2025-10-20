package org.tutgi.student_registration.features.finance.dto.response;

import java.time.LocalDateTime;

import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.features.admin.dto.response.PaginatedAccountResponse;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class SubmittedStudentResponse {
	private Long studentId;
	private String studentNameMM;
	private String studentNameEng;
	private String enrollmentNumber;
	@JsonFormat(pattern = "EEEE, dd MMMM yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "EEEE, dd MMMM yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
