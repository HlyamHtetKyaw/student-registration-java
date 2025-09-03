package org.tutgi.student_registration.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.personal.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
	Optional<Parent> findByStudentIdAndParentType_Name(Long studentId, ParentName name);

}
