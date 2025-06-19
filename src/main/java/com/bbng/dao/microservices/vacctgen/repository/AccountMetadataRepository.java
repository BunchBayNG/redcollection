package com.bbng.dao.microservices.vacctgen.repository;


import com.bbng.dao.microservices.vacctgen.entity.AccountMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountMetadataRepository extends JpaRepository<AccountMetadata, String> {

}
