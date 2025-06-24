package org.tutgi.student_registration.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tutgi.student_registration.config.exceptions.UnauthorizedException;
import org.tutgi.student_registration.security.service.normal.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtService jwtService;

    private List<String> getPermittedUrls() {
        return Arrays.asList(
                "/tutgi/api/v1/auth/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**",
                "/api/v1/public/**",
                "/api/v1/users/change-password",
                "/api/v1/users"
        );
    }

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
    	log.info("Incoming request: {}", request.getRequestURI());

    	String requestPath = request.getRequestURI();

        if (isPermitted(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
            return;
        }

        final String token = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            final Claims claims = jwtService.validateToken(token);

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (UnauthorizedException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token.");
        }
    }
    
    private boolean isPermitted(String requestPath) {
        return getPermittedUrls().stream().anyMatch(pattern -> pathMatcher.match(pattern, requestPath));
    }

}