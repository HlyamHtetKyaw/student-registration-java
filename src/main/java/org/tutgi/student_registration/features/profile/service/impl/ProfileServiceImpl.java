package org.tutgi.student_registration.features.profile.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.models.Profile;
import org.tutgi.student_registration.data.repositories.ProfileRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.features.profile.dto.RegisterRequest;
import org.tutgi.student_registration.features.profile.dto.response.ProfileDto;
import org.tutgi.student_registration.features.profile.service.ProfileService;
import org.tutgi.student_registration.features.users.utils.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserUtil userUtil;

    @Override
    @Transactional
    public ApiResponse createProfile(final Long userId, final RegisterRequest registerRequest) {
        var user = this.userRepository.findById(userId).orElse(null);
        if(user == null){
            log.warn("User not found: {}", userId);
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("User not found")
                    .build();
        }

        if(this.profileRepository.findByUserId(userId).isPresent()){
            log.warn("Profile already exists: {}", userId);
            return ApiResponse.builder()
                    .success(0)
                    .code(HttpStatus.CONFLICT.value())
                    .message("Profile already exists")
                    .build();
        }

        log.info("Creating profile with email: {}", user.getEmail());

        Profile profile =  new Profile();
        profile.setMmName(registerRequest.mmName());
        profile.setEngName(registerRequest.engName());
        profile.setNrc(registerRequest.nrc());
        profile.setUser(user);

        ProfileDto profileDto = ProfileDto
                                    .builder()
                                    .mmName(profile.getMmName())
                                    .engName(profile.getEngName())
                                    .nrc(profile.getNrc())
                                    .build();

        this.profileRepository.save(profile);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(profileDto)
                .message("User profile created successfully.")
                .build();
    }

}
