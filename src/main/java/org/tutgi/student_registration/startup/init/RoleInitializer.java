package org.tutgi.student_registration.startup.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.models.Role;
import org.tutgi.student_registration.data.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;
	
	@Override
	public void run(String... args) throws Exception {
		for (RoleName roleName : RoleName.values()) {
			roleRepository.findByName(roleName).orElseGet(() -> {
				Role role = new Role();
				role.setName(roleName);
				log.info("Initializing role: {}", roleName);
				return roleRepository.save(role);
			});
		}
	}
}

