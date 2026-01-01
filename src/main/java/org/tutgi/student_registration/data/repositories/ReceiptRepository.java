package org.tutgi.student_registration.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.enums.YearType;
import org.tutgi.student_registration.data.models.form.Receipt;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
	List<Receipt> findByYear(YearType year);
	boolean existsByYear(YearType year);
}