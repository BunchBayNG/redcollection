package com.bbng.dao.microservices.auth.organization.repository;

import com.bbng.dao.microservices.auth.organization.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface APIKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
    @Query("select (count(o) > 0) from ApiKeyEntity o where o.testKey = ?1")
    boolean existsByTestKey(String testKey);

    @Query("select (count(o) > 0) from ApiKeyEntity o where o.liveKey = ?1")
    boolean existsByLiveKey(String liveKey);

    List<ApiKeyEntity> findByUserId(String id);

    Optional<ApiKeyEntity> findByTestKey(String testKey);

    Optional<ApiKeyEntity> findByLiveKey(String liveKey);
}
