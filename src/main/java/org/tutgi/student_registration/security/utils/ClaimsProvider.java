package org.tutgi.student_registration.security.utils;

import java.util.HashMap;
import java.util.Map;

import org.tutgi.student_registration.data.models.Employee;

public class ClaimsProvider {

    private ClaimsProvider() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Object> generateClaims(final Employee user) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        return claims;
    }
}
