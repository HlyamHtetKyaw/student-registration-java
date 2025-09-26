package org.tutgi.student_registration.features.profile.service.impl;

import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.config.exceptions.UnauthorizedException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.enums.FileType;
import org.tutgi.student_registration.data.enums.StorageDirectory;
import org.tutgi.student_registration.data.models.Profile;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.repositories.ProfileRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.profile.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.profile.dto.request.UpdateProfileRequest;
import org.tutgi.student_registration.features.profile.dto.request.UploadFileRequest;
import org.tutgi.student_registration.features.profile.dto.response.ProfileResponse;
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
    private final StorageService storageService;
    private final UserUtil userUtil;
    
    @Override
    @Transactional
    public ApiResponse createProfile(final RegisterRequest registerRequest) {
    	Long userId = userUtil.getCurrentUserInternal().userId();
        final var user = this.userRepository.findById(userId).orElseThrow(() -> {
        	return new EntityNotFoundException("User not found");
		});
        if(user.getProfile()!=null) {
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
        
        profile.assignUser(user);
        
        ProfileResponse profileDto = ProfileResponse
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
                .message("Profile created successfully.")
                .build();
    }
    
    @Override
    @Transactional
    public ApiResponse updateProfile(final UpdateProfileRequest updateRequest) {
        Long userId = userUtil.getCurrentUserInternal().userId();
        
        User user = userRepository.findById(userId).orElseThrow(() ->
            new EntityNotFoundException("User not found")
        );

        Profile profile = user.getProfile();
        if (profile == null) {
            throw new EntityNotFoundException("Profile not found.");
        }

        Optional.ofNullable(updateRequest.getMmName()).ifPresent(profile::setMmName);
        Optional.ofNullable(updateRequest.getEngName()).ifPresent(profile::setEngName);
        Optional.ofNullable(updateRequest.getNrc()).ifPresent(profile::setNrc);

        profileRepository.save(profile);

        ProfileResponse profileDto = ProfileResponse.builder()
            .mmName(profile.getMmName())
            .engName(profile.getEngName())
            .nrc(profile.getNrc())
            .photoUrl(profile.getPhotoUrl())
            .signatureUrl(profile.getSignatureUrl())
            .build();

        return ApiResponse.builder()
            .success(1)
            .code(HttpStatus.OK.value())
            .data(profileDto)
            .message("Profile updated successfully.")
            .build();
    }

	@Override
	public ApiResponse retrieveProfile() {
		Long userId = userUtil.getCurrentUserInternal().userId();
        Profile profile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("Profile not found."));
        ProfileResponse profileDto = ProfileResponse
                .builder()
                .mmName(profile.getMmName())
                .engName(profile.getEngName())
                .nrc(profile.getNrc())
                .photoUrl(profile.getPhotoUrl())
                .signatureUrl(profile.getSignatureUrl())
                .build();
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(profileDto)
                .message("Profile retrieved successfully.")
                .build();
	}
	
    @Transactional
    @Override
    public ApiResponse uploadFile(UploadFileRequest fileRequest, FileType type) {
        Long userId = userUtil.getCurrentUserInternal().userId();
        Profile profile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("Profile not found."));

        String currentFile = getFilePathByType(profile, type);
        String filename;

        if (currentFile != null) {
            filename = storageService.update(fileRequest.file(), currentFile, getDirectoryByType(type));
        } else {
            filename = storageService.store(fileRequest.file(), getDirectoryByType(type));
        }

        setFilePathByType(profile, type, filename);
        profileRepository.save(profile);

        return ApiResponse.builder()
            .success(1)
            .code(HttpStatus.OK.value())
            .data(filename)
            .message("File uploaded successfully.")
            .build();
    }


    @Override
    public Resource retrieveFile(String filePath, FileType type) {
        Long userId = userUtil.getCurrentUserInternal().userId();
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found for user."));

        String expectedPath = getFilePathByType(profile,type);

        if (!filePath.equals(expectedPath)) {
            throw new UnauthorizedException("You are not allowed to access this file.");
        }

        return storageService.loadAsResource(filePath);
    }


	@Override
	public ApiResponse deleteFile(FileType type) {
	    Long userId = userUtil.getCurrentUserInternal().userId();
	    Profile profile = profileRepository.findByUserId(userId)
	        .orElseThrow(() -> new EntityNotFoundException("Profile not found for user."));

	    String filePath = getFilePathByType(profile, type);
	    if (filePath == null) {
	        throw new EntityNotFoundException("File not found for user.");
	    }

	    storageService.delete(filePath);
	    setFilePathByType(profile, type, null);
	    profileRepository.save(profile);

	    return ApiResponse.builder()
	        .success(1)
	        .code(HttpStatus.OK.value())
	        .data(true)
	        .message("File deleted successfully")
	        .build();
	}

	
	private String getFilePathByType(Profile profile, FileType type) {
	    return switch (type) {
	        case PROFILE_PHOTO -> profile.getPhotoUrl();
	        case SIGNATURE -> profile.getSignatureUrl();
	    };
	}

	private void setFilePathByType(Profile profile, FileType type, String path) {
	    switch (type) {
	        case PROFILE_PHOTO -> profile.setPhotoUrl(path);
	        case SIGNATURE -> profile.setSignatureUrl(path);
	    }
	}

	private StorageDirectory getDirectoryByType(FileType type) {
	    return switch (type) {
	        case PROFILE_PHOTO -> StorageDirectory.PROFILE_PICTURES;
	        case SIGNATURE -> StorageDirectory.SIGNATURE_PICTURES;
	    };
	}
}
