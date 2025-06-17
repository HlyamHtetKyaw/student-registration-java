package org.tutgi.student_registration.features.users.utils;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.config.exceptions.UnauthorizedException;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.features.users.dto.response.UserDto;
import org.tutgi.student_registration.features.users.repository.UserRepository;
import org.tutgi.student_registration.security.service.normal.JwtService;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserUtil {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserUtil(final JwtService jwtService, final UserRepository userRepository, final ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserDto getCurrentUserDto(final String authHeader) {
        final String email = this.extractEmailFromToken(authHeader);
        final User user = this.findUserByEmail(email);
        return this.modelMapper.map(user, UserDto.class);
    }

    public String extractEmailFromToken(final String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header received");
            throw new UnauthorizedException("Unauthorized: Missing or invalid token");
        }

        final String token = authHeader.substring(7);
        final Claims claims = this.jwtService.validateToken(token);
        return claims.getSubject();
    }


    public User findUserByEmail(final String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found for email: {}", email);
                    return new UnauthorizedException("User not found");
                });
    }

    public UserDto getCurrentUserInternal() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("User is not authenticated");
        }
        
        final String userEmail = authentication.getPrincipal().toString();

        final User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedException("Authenticated user not found in the system"));

        return this.modelMapper.map(user, UserDto.class);
    }

}
