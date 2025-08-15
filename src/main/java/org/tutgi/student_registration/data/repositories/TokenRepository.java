package org.tutgi.student_registration.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.Token;

import jakarta.transaction.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
	Optional<Token> findByRefreshtoken(String refreshtoken);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Token t WHERE t.id = :id")
	void deleteTokenData(@Param("id") Long id);
}
