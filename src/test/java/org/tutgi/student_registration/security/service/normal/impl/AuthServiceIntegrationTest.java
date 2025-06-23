package org.tutgi.student_registration.security.service.normal.impl;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.models.Employee;
import org.tutgi.student_registration.features.employee.shared.repository.EmployeeRepository;
import org.tutgi.student_registration.security.dto.CheckRequest;
import org.tutgi.student_registration.security.dto.ConfirmRequest;
import org.tutgi.student_registration.security.dto.LoginRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class AuthServiceIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private final String BASE_URL = "/tutgi/api/v1/auth";

    private String testEmail = "testuser@example.com";
    private String testPassword = "test123";

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();

        Employee user = new Employee();
        user.setDepartment("DEPARTMENT");
        user.setEmail(testEmail);
        user.setPassword(passwordEncoder.encode(testPassword));
        user.setRole(RoleName.ADMIN);
        user.setName("Test User");
        user.setLoginFirstTime(true);

        employeeRepository.save(user);
    }

//    @Test
//    void confirmThenLogin_successfulFlow() throws Exception {
//        String newEmail = "newuser@example.com";
//        String department = "NewDepartment";
//        String newPassword = "Newpass123@";
//        String name = "New User";
//
//        Employee unconfirmedUser = new Employee();
//        unconfirmedUser.setEmail(newEmail);
//        unconfirmedUser.setDepartment(department);
//        unconfirmedUser.setLoginFirstTime(true);
//        unconfirmedUser.setRole(RoleName.FINANCE);
//        employeeRepository.save(unconfirmedUser);
//
//        ConfirmRequest confirmRequest = new ConfirmRequest(newEmail, name, newPassword);
//        mockMvc.perform(patch(BASE_URL + "/confirm")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(confirmRequest)))
//        		.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(true))
//                .andExpect(jsonPath("$.message").value("User confirm successful."));
//
//        LoginRequest loginRequest = new LoginRequest(newEmail, newPassword);
//        mockMvc.perform(post(BASE_URL + "/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(1))
//                .andExpect(jsonPath("$.data.accessToken").exists())
//                .andExpect(jsonPath("$.data.currentUser.email").value(newEmail));
//    }


    @Test
    void checkUser_returnsLoginFirstTimeStatus() throws Exception {
        CheckRequest checkRequest = new CheckRequest(testEmail);

        mockMvc.perform(post(BASE_URL + "/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.loginFirstTime").value(true));
    }

    @Test
    void confirmUser_setsPasswordAndName() throws Exception {
        String newEmail = "newuser1@example.com";
        String newDepartment = "NewDepartment";
        
        Employee unconfirmedUser = new Employee();
        unconfirmedUser.setEmail(newEmail);
        unconfirmedUser.setDepartment(newDepartment);
        unconfirmedUser.setLoginFirstTime(true);
        unconfirmedUser.setRole(RoleName.FINANCE);
        employeeRepository.save(unconfirmedUser);

        ConfirmRequest confirmRequest = new ConfirmRequest(newEmail, "New User", "Newpass123@");

        mockMvc.perform(patch(BASE_URL + "/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true))
                .andExpect(jsonPath("$.message").value("User confirm successful."));
    }

    @Test
    void confirmUser_alreadyConfirmed_throwsError() throws Exception {
        ConfirmRequest confirmRequest = new ConfirmRequest(testEmail, "anotherPass", "Should Fail");

        mockMvc.perform(patch(BASE_URL + "/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmRequest)))
                .andExpect(status().isBadRequest());
    }
}

