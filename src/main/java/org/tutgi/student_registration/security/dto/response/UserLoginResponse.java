package org.tutgi.student_registration.security.dto.response;

import org.tutgi.student_registration.features.profile.dto.response.ProfileResponse;
import org.tutgi.student_registration.features.users.dto.response.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
	private UserDto user;
	private TokenResponse token;
	private ProfileResponse profile;
}
