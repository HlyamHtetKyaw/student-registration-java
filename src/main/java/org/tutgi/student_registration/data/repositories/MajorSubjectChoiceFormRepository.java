package org.tutgi.student_registration.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.form.MajorSubjectChoiceForm;


@Repository
public interface MajorSubjectChoiceFormRepository extends JpaRepository<MajorSubjectChoiceForm, Long> {
}