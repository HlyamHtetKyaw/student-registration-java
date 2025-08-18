package org.tutgi.student_registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
public class StudentRegistrationApplication {
	public static void main(String[] args) {
		if (isDevEnvironment()) {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
			);
		}
		SpringApplication.run(StudentRegistrationApplication.class, args);
	}

	private static boolean isDevEnvironment() {
		String profile = System.getenv("SPRING_PROFILES_ACTIVE");
		return profile == null || profile.equals("dev");
	}
}

