package org.tutgi.student_registration.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
}
