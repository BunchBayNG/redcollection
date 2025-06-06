package com.bbng.dao.util.email.repository;

import com.bbng.dao.util.email.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

}
