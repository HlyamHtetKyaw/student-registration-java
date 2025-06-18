package org.tutgi.student_registration.config.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) { super(message); }
}
