package org.tutgi.student_registration.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tutgi.student_registration.data.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 Optional<User> findByEmail(String email);
//
//    Optional<User> findByUsername(String identifier);

//    @Modifying
//    @Transactional
//    @Query("UPDATE User u SET u.dateFormat = :dateFormat, u.currencyCode = :currencyCode WHERE u.id = :userId")
//    void updateDateFormatAndCurrencyCode(@Param("userId") Long userId,
//                                         @Param("dateFormat") String dateFormat,
//                                         @Param("currencyCode") String currencyCode);
//    
//    @Modifying
//    @Transactional
//    @Query("UPDATE User u SET u.setAmount = :setAmount WHERE u.id = :userId")
//    void updateSetAmountById(@Param("userId") Long userId,
//                             @Param("setAmount") BigDecimal setAmount);
}
