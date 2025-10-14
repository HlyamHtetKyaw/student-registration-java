package org.tutgi.student_registration.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.form.MajorSubjectChoiceForm;


@Repository
public interface MajorSubjectChoiceFormRepository extends JpaRepository<MajorSubjectChoiceForm, Long> {
	@Modifying
	@Query("DELETE FROM MajorSubjectChoiceForm m WHERE m.subjectChoice.id = :subjectChoiceId")
	void deleteBySubjectChoiceId(@Param("subjectChoiceId") Long subjectChoiceId);

}