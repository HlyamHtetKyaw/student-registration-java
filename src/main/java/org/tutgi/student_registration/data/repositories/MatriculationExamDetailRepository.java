package org.tutgi.student_registration.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.education.MatriculationExamDetail;

@Repository
public interface MatriculationExamDetailRepository extends JpaRepository<MatriculationExamDetail, Long> {
}