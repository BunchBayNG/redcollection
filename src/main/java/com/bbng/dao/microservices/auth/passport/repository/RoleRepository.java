package com.bbng.dao.microservices.auth.passport.repository;



import com.bbng.dao.microservices.auth.passport.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    //    Recommended role types are SUPER_ADMIN, ORGANIZATION_ADMIN, ADMIN, USER
    Optional<RoleEntity> findByRoleName(String name);
    @Query("select role from RoleEntity role where role.createdBy = ?1")
    List<RoleEntity> findByCreatedBy(String username);
}
