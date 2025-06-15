//package org.tutgi.student_registration.security.utils;
//
//import java.util.Map;
//
//import org.springframework.stereotype.Component;
//import org.tutgi.student_registration.security.service.normal.JwtService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class AuthUtil {
//
//    private final JwtService jwtService;
//
//    public Map<String, Object> generateTokens(final User user) {
//        log.debug("Generating tokens for user: {}", user.getEmail());
//
//        final String accessToken = this.jwtService.generateToken(ClaimsProvider.generateClaims(user),
//                user.getEmail(),
////                15 * 60 * 1000
//                3 * 60 * 60 * 1000
//        );
//        final String refreshToken = this.jwtService.generateToken(ClaimsProvider.generateClaims(user),
//                user.getEmail(), 7 * 24 * 60 * 60 * 1000);
//
//        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
//    }
//}
