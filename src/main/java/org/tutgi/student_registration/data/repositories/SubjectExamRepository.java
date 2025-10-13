package org.tutgi.student_registration.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.education.SubjectExam;

@Repository
public interface SubjectExamRepository extends JpaRepository<SubjectExam, Long> {
}