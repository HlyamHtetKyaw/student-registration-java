package org.tutgi.student_registration.features.admin.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.DuplicateEntityException;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.models.Role;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.repositories.RoleRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.features.admin.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.admin.dto.request.ResendRequest;
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
        
        this.userRepository.findByEmail(registerRequest.email()).orElseThrow(() -> new DuplicateEntityException("Email already exists"));
        
        Role role = this.roleRepository.findByName(registerRequest.role())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
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
	@Override
	public ApiResponse resendPassword(final ResendRequest resendRequest) {
		log.info("Resending new password to email: {}", resendRequest.email());
		
		User user = this.userRepository.findByEmail(resendRequest.email()).orElseThrow(() -> new EntityNotFoundException("Email not found"));
		
		String password = ServerUtil.generatePassword();
		user.setPassword(this.passwordEncoder.encode(password));
		this.userRepository.save(user);
		this.serverUtil.sendPasswordToEmail(resendRequest.email(), "PasswordTemplate", password);
		return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Password resend successfully.")
                .build();
	}
}
