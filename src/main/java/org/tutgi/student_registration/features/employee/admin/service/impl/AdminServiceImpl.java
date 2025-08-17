//package org.tutgi.student_registration.features.employee.admin.service.impl;
//
//import java.util.Optional;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.tutgi.student_registration.config.response.dto.ApiResponse;
//import org.tutgi.student_registration.config.utils.NrcUtils;
//import org.tutgi.student_registration.core.nrc.models.NrcTownship;
//import org.tutgi.student_registration.core.nrc.models.NrcType;
//import org.tutgi.student_registration.core.nrc.service.NrcDataLoadingService;
//import org.tutgi.student_registration.data.models.Employee;
//import org.tutgi.student_registration.data.models.Students;
//import org.tutgi.student_registration.features.employee.admin.dto.EmployeeRegisterRequest;
//import org.tutgi.student_registration.features.employee.admin.dto.StudentRegisterRequest;
//import org.tutgi.student_registration.features.employee.admin.service.AdminService;
//import org.tutgi.student_registration.features.employee.shared.repository.EmployeeRepository;
//import org.tutgi.student_registration.features.students.repository.StudentsRepository;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AdminServiceImpl implements AdminService{
//	private final EmployeeRepository employeeRepository;
//	private final StudentsRepository studentsRepository;
//	private final PasswordEncoder passwordEncoder;
//	private final NrcDataLoadingService nrcDataLoadingService;
//	
//	@Override
//    @Transactional
//    public ApiResponse registerEmployee(final EmployeeRegisterRequest registerRequest) {
//        log.info("Registering new employee with email: {}", registerRequest.email());
//        
//        if (this.employeeRepository.findByEmail(registerRequest.email()).isPresent()) {
//            log.warn("Email already exists: {}", registerRequest.email());
//            return ApiResponse.builder()
//                    .success(0)
//                    .code(HttpStatus.CONFLICT.value())
//                    .message("Email is already in use")
//                    .build();
//        }
//
//        final Employee newEmployee = Employee.builder()
//                .department(registerRequest.department())
//                .role(registerRequest.role())
//                .email(registerRequest.email())
//                .build();
//
//        this.employeeRepository.save(newEmployee);
//
//        return ApiResponse.builder()
//                .success(1)
//                .code(HttpStatus.CREATED.value())
//                .data(true)
//                .message("Employee account created successfully.")
//                .build();
//    }
//    
//    @Override
//    @Transactional
//    public ApiResponse registerStudent(final StudentRegisterRequest registerRequest) {
//        log.info("Registering new student with roll number: {}", registerRequest.rollNo());
//        
//        if (this.studentsRepository.findByRollNo(registerRequest.rollNo()).isPresent()) {
//            log.warn("Roll number already exists: {}", registerRequest.rollNo());
//            return ApiResponse.builder()
//                    .success(0)
//                    .code(HttpStatus.CONFLICT.value())
//                    .message("Roll number is already in use.")
//                    .build();
//        }
//        log.info("Incoming Nrc input : {}",registerRequest.nrc());
//        Optional<String[]> nrcComp = NrcUtils.parseNrcComponents(registerRequest.nrc());
//        String[] parts = nrcComp.get();
//        String stateNumber = parts[0];
//        String townshipCode = parts[1];
//        String nrcType = parts[2];
//        String nrcNumber = parts[3];
//        
//        String engStateNumber = NrcUtils.convertBurmeseDigitsToEnglish(stateNumber);
//        
//        Optional<NrcTownship> foundTownship = nrcDataLoadingService.getNrcData().nrcTownships().stream()
//        		.filter(t->t.shortName().en().equalsIgnoreCase(townshipCode)||t.shortName().mm().equalsIgnoreCase(townshipCode))
//        		.findFirst();
//        String engTownshipCodeOrShort = foundTownship.get().shortName().en();
//        Optional<NrcType> foundType = nrcDataLoadingService.getNrcData().nrcTypes().stream()
//        		.filter(t->t.name().en().equalsIgnoreCase(nrcType) || t.name().mm().equalsIgnoreCase(nrcType))
//        		.findFirst();
//        String engType = foundType.get().name().en();
//        
//        String engNrcNumber = NrcUtils.convertBurmeseDigitsToEnglish(nrcNumber);
//        
//        String engNrc = String.format("%s/%s(%s)%s", 
//        		engStateNumber,engTownshipCodeOrShort,
//        		engType,engNrcNumber).toUpperCase();
//        log.info("Converted english Nrc : {}",engNrc);
//        
//        final Students newStudent = Students.builder()
//        		.rollNo(registerRequest.rollNo())
//        		.nrc(this.passwordEncoder.encode(engNrc)).build();
//
//        this.studentsRepository.save(newStudent);
//
//        return ApiResponse.builder()
//                .success(1)
//                .code(HttpStatus.CREATED.value())
//                .data(true)
//                .message("Student account created successfully.")
//                .build();
//    }
//}
