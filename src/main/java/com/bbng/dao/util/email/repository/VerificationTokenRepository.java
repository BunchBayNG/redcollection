package com.bbng.dao.util.email.repository;


import com.bbng.dao.util.email.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByVerificationTokenAndExpired(String token, boolean expired);
}
