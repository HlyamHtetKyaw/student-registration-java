package org.tutgi.student_registration.startup.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tutgi.student_registration.data.enums.MajorName;
import org.tutgi.student_registration.data.models.lookup.Major;
import org.tutgi.student_registration.data.repositories.MajorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MajorInitializer implements CommandLineRunner {

    private final MajorRepository majorRepository;

    @Override
    public void run(String... args) {
        for (MajorName majorName : MajorName.values()) {
            majorRepository.findByName(majorName).orElseGet(() -> {
                Major major = new Major(majorName);
                log.info("Initializing major: {} ({})", majorName.getEngName(), majorName.name());
                return majorRepository.save(major);
            });
        }
    }
}



