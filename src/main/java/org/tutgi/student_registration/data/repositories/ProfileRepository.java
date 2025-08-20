package org.tutgi.student_registration.data.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tutgi.student_registration.data.models.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(Long id);
}
