package com.bbng.dao.microservices.auth.passport.repository;



import com.bbng.dao.microservices.auth.passport.entity.TokenEntity;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    @Query("select t from TokenEntity t where t.userEntity.id = ?1 and t.expired = false and t.revoked = false")
    List<TokenEntity> findAllValidTokensByUserId(String id);

    @Query("select t from TokenEntity  t where t.token = ?1")
    Optional<TokenEntity> findByToken(String token);


    @Query("select t from TokenEntity t where t.userEntity = ?1 and t.expired = false and t.revoked = false")
    Optional<TokenEntity> findByUserEntityAndExpiredFalseAndRevokedFalse(UserEntity user);
//    @Query("select t from TokenEntity t where t.userEntity = ?1 and t.expired = false and t.revoked = false")
//    Optional<UserEntity> findByUserEntity(UserEntity user);


}

