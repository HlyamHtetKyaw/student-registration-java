package org.tutgi.student_registration.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.personal.Sibling;

@Repository
public interface SiblingRepository extends JpaRepository<Sibling, Long> {
	@Modifying
	@Query("DELETE FROM Sibling s WHERE s.student.id = :studentId")
	void deleteByStudentId(@Param("studentId") Long studentId);
}
