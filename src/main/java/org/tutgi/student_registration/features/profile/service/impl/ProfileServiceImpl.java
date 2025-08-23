package org.tutgi.student_registration.features.profile.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.tutgi.student_registration.config.exceptions.EntityNotFoundException;
import org.tutgi.student_registration.config.response.dto.ApiResponse;
import org.tutgi.student_registration.data.enums.FolderType;
import org.tutgi.student_registration.data.enums.StorageDirectory;
import org.tutgi.student_registration.data.models.Profile;
import org.tutgi.student_registration.data.repositories.ProfileRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;
import org.tutgi.student_registration.data.storage.StorageService;
import org.tutgi.student_registration.features.profile.dto.request.RegisterRequest;
import org.tutgi.student_registration.features.profile.dto.request.UploadProfilePictureRequest;
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
        
//        if (file != null && !file.isEmpty()) {
//            String filename = storageService.store(file,FolderType.PROFILE.name());
//            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/files/")
//                    .path(filename)
//                    .toUriString();
//            profile.setPhotoUrl(fileUrl);
//        }
        
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

    @Transactional
	@Override
	public ApiResponse uploadProfilePicture(UploadProfilePictureRequest profileRequest) {
    	Long userId = userUtil.getCurrentUserInternal().userId();
	    final Profile profile = this.profileRepository.findByUserId(userId)
	            .orElseThrow(() -> new EntityNotFoundException("Profile not found."));
	
	    final String filename = storageService.store(
	    		profileRequest.file(),StorageDirectory.PROFILE_PICTURES,
	    		profile.getEngName(),profile.getId());
	
	    profile.setPhotoUrl(filename);
	    
	    this.profileRepository.save(profile);
	
	    return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(filename)
                .message("File uploaded successfully.")
                .build();
	}

}
