package org.tutgi.student_registration.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.models.personal.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
    Optional<Contact> findByEntityId(Long entityId);
}