package org.tutgi.student_registration.features.admin.service.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.DuplicateEntityException;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginatedApiResponse;
import org.tutgi.student_registration.config.response.dto.PaginationMeta;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.models.Role;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.repositories.RoleRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.features.admin.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.admin.dto.request.ResendRequest;
import org.tutgi.student_registration.features.admin.dto.response.PaginatedAccountResponse;
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
        
        userRepository.findByEmail(registerRequest.email())
        .ifPresent(user -> {
            throw new DuplicateEntityException("Email already exists");
        });

        Role role = this.roleRepository.findByName(registerRequest.role())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
		User user = new User();
		user.setEmail(registerRequest.email());
		String password = ServerUtil.generatePassword();
		user.setPassword(this.passwordEncoder.encode(password));
		user.setRole(role);
		this.userRepository.save(user);
		this.serverUtil.sendPasswordEmail(registerRequest.email(), "PasswordTemplate", password);
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("User account created successfully.")
                .build();
    }
	
	@Override
	@Transactional
	@CacheEvict(value = "usersCache", allEntries = true)
	public ApiResponse resendPassword(final ResendRequest resendRequest) {
		log.info("Resending new password to email: {}", resendRequest.email());
		
		User user = this.userRepository.findByEmail(resendRequest.email()).orElseThrow(() -> new EntityNotFoundException("Email not found"));
		
		String password = ServerUtil.generatePassword();
		user.setPassword(this.passwordEncoder.encode(password));
		this.userRepository.save(user);
		this.serverUtil.sendPasswordEmail(resendRequest.email(), "PasswordTemplate", password);
		return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Password resend successfully.")
                .build();
	}
	
	@Override
	@Cacheable(value = "usersCache")
    public PaginatedApiResponse<PaginatedAccountResponse> getAllAccountsPaginated(
            String keyword, RoleName role, Pageable pageable) {
	 
	 Page<User> userPage = this.userRepository.findAllFiltered(role, keyword, pageable);

        List<PaginatedAccountResponse> userResponses = userPage.getContent().stream()
                .map(user -> PaginatedAccountResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .role(user.getRole().getName())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build()
                ).toList();

        PaginationMeta meta = new PaginationMeta();
        meta.setTotalItems(userPage.getTotalElements());
        meta.setTotalPages(userPage.getTotalPages());
        meta.setCurrentPage(pageable.getPageNumber()+1);
        return PaginatedApiResponse.<PaginatedAccountResponse>builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Fetched successfully")
                .meta(meta)
                .data(userResponses)
                .build();
    }
}
