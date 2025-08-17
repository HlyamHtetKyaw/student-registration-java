package org.tutgi.student_registration.security.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.Cookie;
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    private final String BASE_URL = "/tutgi/api/v1/auth";
    
    @Value("${test.auth.email}")
private String email;

@Value("${test.auth.password}")
private String password;

@Test
public void testLogin() throws Exception {
    String jsonRequest = String.format("""
        {
            "email": "%s",
            "password": "%s"
        }
    """, email, password);

    mockMvc.perform(post(BASE_URL + "/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(cookie().exists("refreshToken"))
            .andExpect(jsonPath("$.success").value(1))
            .andExpect(jsonPath("$.message").value("You are successfully logged in!"));
}

}

