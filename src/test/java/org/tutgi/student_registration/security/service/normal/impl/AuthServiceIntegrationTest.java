//package org.tutgi.student_registration.security.service.normal.impl;
//
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Map;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.tutgi.student_registration.config.response.dto.ApiResponse;
//import org.tutgi.student_registration.config.utils.DtoUtil;
//import org.tutgi.student_registration.data.enums.RoleName;
//import org.tutgi.student_registration.data.models.Employee;
//import org.tutgi.student_registration.data.models.Students;
//import org.tutgi.student_registration.features.employee.shared.repository.EmployeeRepository;
//import org.tutgi.student_registration.features.students.dto.response.StudentDto;
//import org.tutgi.student_registration.features.students.repository.StudentsRepository;
//import org.tutgi.student_registration.features.users.utils.UserUtil;
//import org.tutgi.student_registration.security.dto.CheckRequest;
//import org.tutgi.student_registration.security.dto.ConfirmRequest;
//import org.tutgi.student_registration.security.dto.EmployeeLoginRequest;
//import org.tutgi.student_registration.security.dto.StudentLoginRequest;
//import org.tutgi.student_registration.security.service.normal.AuthService;
//import org.tutgi.student_registration.security.utils.AuthUserUtility;
//import org.tutgi.student_registration.security.utils.AuthUtil;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ActiveProfiles("test")
//class AuthServiceIntegrationTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private ObjectMapper objectMapper;
//    @Autowired private EmployeeRepository employeeRepository;
//    @Autowired private StudentsRepository studentRepository;
//    @Autowired private PasswordEncoder passwordEncoder;
//    @Autowired private AuthService authService;
//    @Autowired private AuthUtil authUtil;
//    @Autowired private UserUtil userUtil;
//    @Autowired private ModelMapper modelMapper;
//    
//    private final String BASE_URL = "/tutgi/api/v1/auth";
//
//    private String testEmail = "testuser@example.com";
//    private String testPassword = "test123";
//
//    @BeforeEach
//    void setup() {
//        employeeRepository.deleteAll();
//
//        Employee user = new Employee();
//        user.setDepartment("DEPARTMENT");
//        user.setEmail(testEmail);
//        user.setPassword(passwordEncoder.encode(testPassword));
//        user.setRole(RoleName.ADMIN);
//        user.setName("Test User");
//        user.setLoginFirstTime(true);
//
//        employeeRepository.save(user);
//    }
//
//    @Test
//    void confirmEmployeeThenLogin_successfulFlow() throws Exception {
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
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(true))
//                .andExpect(jsonPath("$.message").value("User confirm successful."));
//
//        EmployeeLoginRequest loginRequest = new EmployeeLoginRequest(newEmail, newPassword);
//        mockMvc.perform(post(BASE_URL + "/employee/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(1))
//                .andExpect(jsonPath("$.data.accessToken").exists())
//                .andExpect(jsonPath("$.data.currentUser.email").value(newEmail));
//    }
//
//    @Test
//    void studentLogin_successfulFlow() throws Exception {
//        String rollNo = "1Test-1";
//        String nrc = "13/MASATA(N)000000";
//        Students student = new Students();
//        student.setRollNo(rollNo);
//        student.setNrc(passwordEncoder.encode(nrc));
//
//        studentRepository.save(student);
//        StudentLoginRequest loginRequest = new StudentLoginRequest(rollNo, nrc);
//        mockMvc.perform(post(BASE_URL + "/students/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(1))
//                .andExpect(jsonPath("$.data.accessToken").exists())
//                .andExpect(jsonPath("$.data.currentUser.rollNo").value(rollNo));
//    }
//    @Test
//    void checkUser_returnsLoginFirstTimeStatus() throws Exception {
//        CheckRequest checkRequest = new CheckRequest(testEmail);
//
//        mockMvc.perform(post(BASE_URL + "/check")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(checkRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.loginFirstTime").value(true));
//    }
//
//    @Test
//    void confirmUser_setsPasswordAndName() throws Exception {
//        String newEmail = "newuser1@example.com";
//        String newDepartment = "NewDepartment";
//        
//        Employee unconfirmedUser = new Employee();
//        unconfirmedUser.setEmail(newEmail);
//        unconfirmedUser.setDepartment(newDepartment);
//        unconfirmedUser.setLoginFirstTime(true);
//        unconfirmedUser.setRole(RoleName.FINANCE);
//        employeeRepository.save(unconfirmedUser);
//
//        ConfirmRequest confirmRequest = new ConfirmRequest(newEmail, "New User", "Newpass123@");
//
//        mockMvc.perform(patch(BASE_URL + "/confirm")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(confirmRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(true))
//                .andExpect(jsonPath("$.message").value("User confirm successful."));
//    }
//
//    @Test
//    void confirmUser_alreadyConfirmed_throwsError() throws Exception {
//        ConfirmRequest confirmRequest = new ConfirmRequest(testEmail, "anotherPass", "Should Fail");
//
//        mockMvc.perform(patch(BASE_URL + "/confirm")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(confirmRequest)))
//                .andExpect(status().isBadRequest());
//    }
//    
//    @Test
//    void shouldReturnCurrentUserSuccessfully() throws Exception {
//        mockMvc.perform(get(BASE_URL + "/me")
//                        .header("Authorization","Bearer "+getToken())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(1))
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("User retrieved successfully"));
//    }
//    
//    String getToken(){
//    	String rollNo = "1Test-2";
//        String nrc = "13/MASATA(N)000000";
//        Students student = Students.builder().rollNo(rollNo).nrc(passwordEncoder.encode(nrc)).build();
//        studentRepository.save(student);
//        AuthenticatedUser authUser = AuthUserUtility.fromStudent(student);
//
//        Map<String, Object> tokenData = authUtil.generateTokens(authUser);
//        String accessToken = (String) tokenData.get("accessToken");
//    	return accessToken;
//    }
//}
//
