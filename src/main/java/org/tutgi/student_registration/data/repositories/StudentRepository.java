package org.tutgi.student_registration.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
	@Query("""
		    SELECT s FROM Student s
		    WHERE s.submitted = true
		      AND (
		           :keyword IS NULL OR :keyword = '' 
		           OR LOWER(s.mmName) LIKE LOWER(CONCAT('%', :keyword, '%'))
		           OR LOWER(s.engName) LIKE LOWER(CONCAT('%', :keyword, '%'))
		           OR LOWER(s.enrollmentNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
		      )
		    """)
		Page<Student> findAllFiltered(@Param("keyword") String keyword, Pageable pageable);
	
	Student findByUserId(Long userId);
}

