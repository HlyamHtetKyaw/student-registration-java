package org.tutgi.student_registration.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.enums.MajorName;
import org.tutgi.student_registration.data.models.lookup.Major;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
	Optional<Major> findByName(MajorName name);
}