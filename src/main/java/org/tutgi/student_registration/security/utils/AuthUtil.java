package org.tutgi.student_registration.security.utils;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.tutgi.student_registration.security.service.normal.Authenticatable;
import org.tutgi.student_registration.security.service.normal.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final JwtService jwtService;

    public <T extends Authenticatable> Map<String, Object> generateTokens(final T user) {

        final String accessToken = jwtService.generateToken(
            ClaimsProvider.generateClaims(user),
            user.getIdentifier(),
            15 * 60 * 1000
        );

        final String refreshToken = jwtService.generateToken(
            ClaimsProvider.generateClaims(user),
            user.getIdentifier(),
            7 * 24 * 60 * 60 * 1000
        );

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

}
