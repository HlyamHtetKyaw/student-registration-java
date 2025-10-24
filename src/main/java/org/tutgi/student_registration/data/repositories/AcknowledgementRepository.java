package org.tutgi.student_registration.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutgi.student_registration.data.models.form.Acknowledgement;

public interface AcknowledgementRepository extends JpaRepository<Acknowledgement, Long> {
}