package com.bbng.dao.microservices.vacctgen.repository;


import com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProvisionedAccountRepository extends JpaRepository<ProvisionedAccount, Long>, JpaSpecificationExecutor<ProvisionedAccount> {

    @Query("select COUNT(a) from ProvisionedAccount a where a.clientId =?1")
    int countBatchGroupable(String partnerId, String batchRef);

//    boolean exi(String batchRef, String partnerId);


//    @Modifying
//    @Query("update ProvisionedAccount a set a.status = ?3 where a.clientId = ?2")
//    int activateByBatchRef(String batchRef, String partnerId, ProvisionedAccount.Status status);
//

    @Modifying
    @Transactional
    @Query("update ProvisionedAccount a set a.status = ?3 where a.batchRef = ?1 and a.clientId = ?2")
    int activateByBatchRef(String batchRef, String partnerId, ProvisionedAccount.Status status);

    boolean existsAllByAccountNoIn(List<String> accountNumbers);

    @Modifying
    @Query("update ProvisionedAccount a set a.status = ?3 where a.accountNo in ?1 and a.clientId = ?2")
    int activateByAccountNumbers(List<String> accountNumbers, String partnerId,
                                 ProvisionedAccount.Status status);

    boolean existsAllByAccountNo(String accountNumber);

    @Modifying
    @Query("update ProvisionedAccount a set a.status = ?3 where a.accountNo = ?1 and a.clientId = ?2")
    void activateByAccountNumber(String accountNumber, String partnerId,
                                ProvisionedAccount.Status status);

    @Query("select pa from ProvisionedAccount pa where pa.accountNo = ?1")
    Optional<ProvisionedAccount> findByAccountNo(String accountNo);


    @Query("select pa from ProvisionedAccount pa where pa.accountNo = ?1 and pa.status = ?2")
    Optional<ProvisionedAccount> findByAccountNoAndStatus(String accountNo, ProvisionedAccount.Status status);


    @Query("select pa from ProvisionedAccount pa where pa.walletNo = ?1 and pa.mode = ?2")
    Optional<ProvisionedAccount> findByWalletNoAndMode(String walletNo, ProvisionedAccount.Mode mode);

    @Query("select pa from ProvisionedAccount pa where pa.initiatorRef = ?1 and pa.mode = ?2")
    Optional<ProvisionedAccount> findByInitiatorRefAndMode(String initiatorRef, ProvisionedAccount.Mode mode);


    @Query("select pa from ProvisionedAccount pa where pa.walletNo = ?1 and pa.accountNo = ?2")
    Optional<ProvisionedAccount> findByWalletNoAndAccountNo(String walletNo, String accountNo);


    @Modifying
    @Query(value = "update provisioned_account a set a.status = ?1 where a.mode = ?2 and a.provision_date < DATEADD(day, -1, GETDATE())", nativeQuery = true)
    int updateAccountStatusByMode(ProvisionedAccount.Status status, ProvisionedAccount.Mode mode);


}
