package org.tutgi.student_registration.startup.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.lookup.ParentType;
import org.tutgi.student_registration.data.repositories.ParentTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class ParentTypeInitializer implements CommandLineRunner {

	private final ParentTypeRepository parentTypeRepository;
	
	@Override
	public void run(String... args) throws Exception {
		for (ParentName parentName : ParentName.values()) {
			parentTypeRepository.findByName(parentName).orElseGet(() -> {
				ParentType parentType = new ParentType();
				parentType.setName(parentName);
				log.info("Initializing parent name: {}", parentName);
				return parentTypeRepository.save(parentType);
			});
		}
	}
}