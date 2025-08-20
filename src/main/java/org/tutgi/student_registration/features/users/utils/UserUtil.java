package org.tutgi.student_registration.features.users.utils;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.config.exceptions.UnauthorizedException;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.features.users.dto.response.UserDto;
import org.tutgi.student_registration.security.dto.CustomUserPrincipal;
import org.tutgi.student_registration.security.service.normal.JwtService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserUtil {

	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public Object getCurrentUserDto(final String authHeader) {
		String email = extractEmailFromToken(authHeader);
        User user = findUserByEmail(email);
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getName());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
	}

	public Claims extractClaimsFromToken(final String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			log.warn("Invalid Authorization header received");
			throw new UnauthorizedException("Unauthorized: Missing or invalid token");
		}

		final String token = authHeader.substring(7);
		return this.jwtService.validateToken(token);
	}

	public CustomUserPrincipal getCurrentUserInternal() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
			throw new UnauthorizedException("User is not authenticated");
		}
		if (authentication instanceof AnonymousAuthenticationToken) {
		    throw new UnauthorizedException("Anonymous users are not allowed");
		}
		if (!(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {
			throw new UnauthorizedException("Invalid authentication principal");
		}
		
		return principal;
	}
	
	public String extractEmailFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header received");
            throw new SecurityException("Unauthorized: Missing or invalid token");
        }

        String token = authHeader.substring(7);
        Claims claims = jwtService.validateToken(token);
        return claims.getSubject();
    }
	
	public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found for email: {}", email);
                    return new SecurityException("User not found");
                });
    }

}
