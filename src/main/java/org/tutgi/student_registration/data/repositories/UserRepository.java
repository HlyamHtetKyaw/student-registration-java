package org.tutgi.student_registration.data.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.enums.RoleName;
import org.tutgi.student_registration.data.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 Optional<User> findByEmail(String email);
	 
	 @Query("""
		        SELECT u FROM User u
		        WHERE (:role IS NULL OR u.role.name = :role)
		          AND (:keyword IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
		        """)
		    Page<User> findAllFiltered(@Param("role") RoleName role,
		                               @Param("keyword") String keyword,
		                               Pageable pageable);
}
