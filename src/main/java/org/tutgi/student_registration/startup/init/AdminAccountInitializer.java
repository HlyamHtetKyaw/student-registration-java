package org.tutgi.student_registration.startup.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.models.Role;
import org.tutgi.student_registration.data.models.User;
import org.tutgi.student_registration.data.repositories.RoleRepository;
import org.tutgi.student_registration.data.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminAccountInitializer implements CommandLineRunner {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	@Value("${admin.email}")
	private String emailAddr;
	
	@Override
	public void run(String... args) throws Exception {
		if (userRepository.findByEmail(emailAddr).isEmpty()) {
			Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
			User user = new User();
			user.setEmail(emailAddr);
			user.setPassword(passwordEncoder.encode("Admin8080@"));
			user.setRole(adminRole);
			userRepository.save(user);
			log.info("Admin account created successfully.");
		} else {
			log.info("Admin account already exists.");
		}
	}
}
