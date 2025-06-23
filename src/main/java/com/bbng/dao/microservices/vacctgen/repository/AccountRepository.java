package com.bbng.dao.microservices.vacctgen.repository;


import com.bbng.dao.microservices.vacctgen.entity.Account;
import com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

//    @Query("select i from VirtualAccount i")
//    VirtualAccount findOne(Pageable pageRequest);

    @Query("select a from Account a")
    LinkedList<Account> findBatch(Pageable pageRequest);

    @Query("select a from Account a where a.status = ?1")
    List<Account> findAssignable(Account.Status status, Pageable page);

    Optional<Account> findFirstByStatus(Account.Status status);

    @Modifying
    @Query("update Account a set a.status = ?2 where a.id = ?1")
    void markAccountAsProvisioned(Long id, Account.Status status);

    List<Account> findAllByStatus(Account.Status status, Pageable page);

    @Modifying
    @Query("update Account a set a.status = ?2 where a in ?1")
    void markAllAsProvisioned(List<Account> accounts, Account.Status provisioned);

    @Modifying
    @Query(value = "UPDATE account SET status = ?1 WHERE value in (SELECT DISTINCT account_no FROM provisioned_account WHERE status = ?2 and mode = ?3 and provision_date < DATEADD(day, -30, GETDATE()) )", nativeQuery = true)
    int updateAccountStatusByProvisionedAccountModeAndStatus(Account.Status status, ProvisionedAccount.Status provisionedAccountStatus, ProvisionedAccount.Mode provisionedAccountMode);
}
