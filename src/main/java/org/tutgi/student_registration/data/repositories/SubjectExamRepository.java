package org.tutgi.student_registration.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.enums.SubjectName;
import org.tutgi.student_registration.data.models.education.SubjectExam;

@Repository
public interface SubjectExamRepository extends JpaRepository<SubjectExam, Long> {
	Optional<SubjectExam> findBySubject_NameAndMed_Id(SubjectName name, Long medId);
}