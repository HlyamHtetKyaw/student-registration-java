//package org.tutgi.student_registration.startup.init;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.tutgi.student_registration.data.enums.RoleName;
//import org.tutgi.student_registration.data.models.Employee;
//import org.tutgi.student_registration.features.employee.shared.repository.EmployeeRepository;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class AdminAccountInitializer implements CommandLineRunner {
//
//	private final EmployeeRepository employeeRepository;
//	
//	@Value("${admin.email}")
//	private String emailAddr;
//	
//	@Override
//	public void run(String... args) throws Exception {
//		if (employeeRepository.findByEmail(emailAddr).isEmpty()) {
//			Employee employee = Employee.builder()
//					.email(emailAddr)
//					.department("Adminstration")
//					.loginFirstTime(true)
//					.role(RoleName.ADMIN).build();
//
//			employeeRepository.save(employee);
//			log.info("Admin account created successfully.");
//		} else {
//			log.info("Admin account already exists.");
//		}
//	}
//}
