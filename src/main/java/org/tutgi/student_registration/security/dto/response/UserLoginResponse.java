package org.tutgi.student_registration.security.dto.response;

import org.tutgi.student_registration.data.enums.RoleName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
	private String name;
	private String email;
	private RoleName role;
	private Object accessToken;
	private Object refreshToken;
}
