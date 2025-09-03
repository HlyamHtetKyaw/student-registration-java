package org.tutgi.student_registration.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.personal.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
	Optional<Job> findByEntityId(Long entityId);
}
