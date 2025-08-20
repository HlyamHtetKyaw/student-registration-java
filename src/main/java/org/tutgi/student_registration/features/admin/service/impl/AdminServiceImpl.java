package org.tutgi.student_registration.features.admin.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.models.Role;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.repositories.RoleRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.features.admin.dto.RegisterRequest;
import org.tutgi.student_registration.features.admin.service.AdminService;
import org.tutgi.student_registration.security.utils.ServerUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	private final ServerUtil serverUtil;
	@Override
    @Transactional
    public ApiResponse registerUser(final RegisterRequest registerRequest) {
        log.info("Registering new user with email: {}", registerRequest.email());
        
        if (this.userRepository.findByEmail(registerRequest.email()).isPresent()) {
            log.warn("Email already exists: {}", registerRequest.email());
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CONFLICT.value())
                    .message("Email is already in use")
                    .build();
        }
        
        Role role = this.roleRepository.findByName(registerRequest.role())
                .orElseThrow(() -> new RuntimeException("Role not found"));
		User user = new User();
		user.setEmail(registerRequest.email());
		String password = ServerUtil.generatePassword();
		user.setPassword(this.passwordEncoder.encode(password));
		user.setRole(role);
		this.userRepository.save(user);
		this.serverUtil.sendPasswordToEmail(registerRequest.email(), "PasswordTemplate", password);
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("User account created successfully.")
                .build();
    }
}
