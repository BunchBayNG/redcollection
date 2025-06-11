package com.bbng.dao.microservices.auth.passport.repository;


import com.bbng.dao.microservices.auth.passport.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    @Query("select p from PermissionEntity  p where p.name = ?1 or p.description=?2")
    Optional<PermissionEntity> findByNameOrDescription(String name, String description);

    boolean existsByNameOrDescription(String permissionName, String permissionName1);

    @Query("SELECT p FROM PermissionEntity p WHERE p.name IN :names OR p.description IN :descriptions")
    List<PermissionEntity> findAllByNameInOrDescriptionIn(@Param("names") List<String> names,
                                                          @Param("descriptions") List<String> descriptions);

    @Query("SELECT p FROM PermissionEntity p WHERE p.name NOT LIKE :prefix%")
    List<PermissionEntity> findPermissionsNotStartingWith(@Param("prefix") String prefix);


}
