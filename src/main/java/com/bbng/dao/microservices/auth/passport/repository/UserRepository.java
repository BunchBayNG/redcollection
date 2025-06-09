package com.bbng.dao.microservices.auth.passport.repository;



import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {


    @Query("select u from UserEntity u where u.userName = ?1 or u.email = ?2")
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    @Query("select u from UserEntity u where u.email = ?1")
    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("select u from UserEntity u where u.id = ?1")
    Optional<UserEntity> findById(String id);
    //get every user that is enabled and does not have an admin or redtechstaff as there userType
    @Query("select usr from UserEntity usr where usr.isEnabled = true and (usr.usertype = 'ORGANIZATION_ADMIN' or usr.usertype = 'ORGANIZATION_STAFF')")
    List<UserEntity> getAllUsers();

    @Query("select usr from UserEntity usr where usr.isEnabled = true and (usr.usertype = 'ORGANIZATION_ADMIN')")
    List<UserEntity> findByOrganizationAdmin();


}



