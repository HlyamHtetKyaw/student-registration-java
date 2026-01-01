package org.tutgi.student_registration.features.users.dto.response;

import java.time.LocalDateTime;

import org.tutgi.student_registration.data.enums.RoleName;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto{
	String name;
	String email;
	RoleName role;
	@JsonFormat(pattern = "EEEE, dd MMMM yyyy HH:mm:ss")
	LocalDateTime createdAt;
	@JsonFormat(pattern = "EEEE, dd MMMM yyyy HH:mm:ss")
	LocalDateTime updatedAt;
}
