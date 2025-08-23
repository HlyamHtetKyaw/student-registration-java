package org.tutgi.student_registration.features.profile.dto.request;

import org.springframework.web.multipart.MultipartFile;
import org.tutgi.student_registration.config.annotations.ValidFile;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadProfilePictureRequest(
    @ValidFile
    @Schema(type = "string", format = "binary", description = "The image file to upload")
    MultipartFile file
) {}
