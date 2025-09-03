package org.tutgi.student_registration.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.enums.ParentName;
import org.tutgi.student_registration.data.models.lookup.ParentType;

@Repository
public interface ParentTypeRepository extends JpaRepository<ParentType, Long> {
	Optional<ParentType> findByName(ParentName name);
}
