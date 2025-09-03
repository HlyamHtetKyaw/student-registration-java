package org.tutgi.student_registration.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	Student findByUserId(Long userId);
}

