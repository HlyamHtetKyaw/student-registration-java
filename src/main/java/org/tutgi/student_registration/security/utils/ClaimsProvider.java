package org.tutgi.student_registration.security.utils;

import java.util.HashMap;
import java.util.Map;

import org.tutgi.student_registration.data.models.User;

public class ClaimsProvider {

    private ClaimsProvider() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Object> generateClaims(final User user) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        return claims;
    }
}
