package org.tutgi.student_registration.startup.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.SubjectName;
import org.tutgi.student_registration.data.models.lookup.Subject;
import org.tutgi.student_registration.data.repositories.SubjectRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubjectsInitializer implements CommandLineRunner {

	private final SubjectRepository subjectRepository;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		for (SubjectName subjectName : SubjectName.values()) {
			subjectRepository.findByName(subjectName).orElseGet(() -> {
				Subject subject = new Subject();
				subject.setName(subjectName);
				log.info("Initializing subject name: {}", subjectName);
				return subjectRepository.save(subject);
			});
		}
	}
}
