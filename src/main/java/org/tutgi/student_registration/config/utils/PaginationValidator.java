package org.tutgi.student_registration.config.utils;

import org.tutgi.student_registration.config.exceptions.BadRequestException;

public class PaginationValidator {

    public static void validatePageAndSize(final int page, final int size) {
        if (page < 1) {
            throw new BadRequestException("Page must be greater than 0");
        }
        if (size < 1) {
            throw new BadRequestException("Size must be greater than 0");
        }
    }
}
