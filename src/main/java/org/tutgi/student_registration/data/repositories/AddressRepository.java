package org.tutgi.student_registration.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.models.personal.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	Optional<Address> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
    Optional<Address> findByEntityId(Long entityId);
}

