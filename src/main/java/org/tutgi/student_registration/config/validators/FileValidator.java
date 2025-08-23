package org.tutgi.student_registration.config.validators;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.tutgi.student_registration.config.annotations.ValidFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    @Value("${file.upload.max-size}")
    private long maxSize;

    @Value("#{'${file.upload.allowed-types}'.split(',')}")
    private List<String> allowedTypes;

    @Value("#{'${file.upload.allowed-extensions}'.split(',')}")
    private List<String> allowedExtensions;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return buildViolation(context, "File is empty.");
        }

        if (file.getSize() > maxSize) {
            return buildViolation(context, "File exceeds max size of " + (maxSize / 1024 / 1024) + " MB.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            return buildViolation(context, "Invalid content type: " + contentType);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasValidExtension(originalFilename)) {
            return buildViolation(context, "Invalid file extension. Allowed: " + String.join(", ", allowedExtensions));
        }

        return true;
    }

    private boolean hasValidExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) return false;

        String ext = filename.substring(dotIndex + 1).toLowerCase();
        return allowedExtensions.contains(ext);
    }

    private boolean buildViolation(ConstraintValidatorContext context, String msg) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        return false;
    }
}



