package com.bbng.dao.microservices.vacctnumnotify.repository;

import com.bbng.dao.microservices.vacctnumnotify.entity.BankAccountEntity;
import com.bbng.dao.microservices.vacctnumnotify.entity.BankCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {


    Optional<BankAccountEntity> findFirstByBankCategory(BankCategory bankAccountNumber);
}
