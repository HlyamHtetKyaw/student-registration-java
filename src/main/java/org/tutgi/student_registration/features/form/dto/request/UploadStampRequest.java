package org.tutgi.student_registration.features.form.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import org.tutgi.student_registration.config.annotations.ValidFile;

public record UploadStampRequest(
        @ValidFile
        @Schema(type = "string", format = "binary", description = "The stamp image file to upload")
        MultipartFile file
) {}
