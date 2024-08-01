package com.bloodbank.management.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bloodbank.management.entity.ResetTokens;

import java.util.Optional;

@Repository
public interface ResetTokensRepository extends JpaRepository<ResetTokens, Long> {
    Optional<ResetTokens> findByResetToken(String resetToken);
}
