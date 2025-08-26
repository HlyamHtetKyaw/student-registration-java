package org.tutgi.student_registration.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutgi.student_registration.data.enums.EntityType;
import org.tutgi.student_registration.data.models.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
}

