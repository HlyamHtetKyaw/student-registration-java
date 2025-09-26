package org.tutgi.student_registration.features.admin.dto.response;

import java.time.LocalDateTime;

import org.tutgi.student_registration.data.enums.RoleName;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class PaginatedAccountResponse {
	private Long id;
    private String email;
    private RoleName role;
    @JsonFormat(pattern = "EEEE, dd MMMM yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "EEEE, dd MMMM yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
