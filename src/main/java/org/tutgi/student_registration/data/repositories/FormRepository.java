package org.tutgi.student_registration.data.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.form.Form;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

    boolean existsByIsOpen(boolean isOpen);

    Page<Form> findAllByIsOpen(boolean isOpen, Pageable pageable);

    List<Form> findAllByIsOpen(boolean isOpen);
}
