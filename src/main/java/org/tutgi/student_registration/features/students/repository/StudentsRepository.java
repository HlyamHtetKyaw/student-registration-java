package org.tutgi.student_registration.features.students.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.Employee;
import org.tutgi.student_registration.data.models.Students;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Long> {
	Optional<Students> findByRollNo(String rollNo);
}
