package org.tutgi.student_registration.security.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.security.service.normal.Authenticatable;

public class ClaimsProvider {

    private ClaimsProvider() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Object> generateClaims(Authenticatable user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("identifier", user.getIdentifier());
        List<RoleName> roles = user.getAuthorities().stream().toList();
        claims.put("authorities", roles);
        return claims;
    }

}
