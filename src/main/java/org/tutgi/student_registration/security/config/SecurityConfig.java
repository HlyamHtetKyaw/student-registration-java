package org.tutgi.student_registration.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.tutgi.student_registration.data.enums.Authority;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.security.filter.CustomAuthenticationEntryPoint;
import org.tutgi.student_registration.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                        		"/tutgi/api/v1/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/api/v1/public/**",
                                "/api/v1/users/change-password",
                                "/api/v1/users"
                        ).permitAll()
                        .requestMatchers("/tutgi/api/v1/admin/**").hasRole(RoleName.ADMIN.name())
                        .requestMatchers("/tutgi/api/v1/profile/**")
                        .hasAnyRole(
                            RoleName.DEAN.name(),
                            RoleName.FINANCE.name(),
                            RoleName.STUDENT_AFFAIR.name()
                         )
                        .requestMatchers("/tutgi/api/v1/student/**").hasRole(RoleName.STUDENT.name())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
