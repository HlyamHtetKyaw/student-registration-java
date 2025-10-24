package org.tutgi.student_registration.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.form.EntranceForm;
import org.tutgi.student_registration.features.students.dto.response.FinanceVerifierDto;

@Repository
public interface EntranceFormRepository extends JpaRepository<EntranceForm, Long> {
	 @Query("SELECT new org.tutgi.student_registration.features.students.dto.response.FinanceVerifierDto(p.mmName, p.signatureUrl) " +
	           "FROM EntranceForm ef " +
	           "JOIN ef.profiles p " +
	           "WHERE ef.id = :formId AND LOWER(p.user.role.name) = 'finance'")
	    List<FinanceVerifierDto> findFinanceVerifiersByFormId(@Param("formId") Long formId);
}
