package com.bbng.dao.microservices.auth.passport.repository;

import com.bbng.dao.microservices.auth.passport.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OtpEntity, String> {


    Optional<OtpEntity> findByEmail(String email);

    Optional<OtpEntity> findByOtp(String otp);

    Optional<OtpEntity> findByOtpAndExpired(String otp, boolean expired);

}
