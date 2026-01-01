package org.tutgi.student_registration.features.file.dto.response;

import org.springframework.core.io.Resource;

public record FileResponse(Resource resource, String fileName) {

}
