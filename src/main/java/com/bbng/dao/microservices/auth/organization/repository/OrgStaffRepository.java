package com.bbng.dao.microservices.auth.organization.repository;


import com.bbng.dao.microservices.auth.organization.entity.OrgStaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrgStaffRepository extends JpaRepository<OrgStaffEntity, String> {


    @Query("select o.organizationId from OrgStaffEntity o where o.userId = ?1")
    Optional<String> findOrganizationIdByUserId(String userId);

    @Query("select o from OrgStaffEntity o where o.userId = ?1")
    Optional<OrgStaffEntity> findOrganizationByUserId(String userId);

    @Query("select (count(o) > 0) from OrgStaffEntity o where o.organizationId = ?1 and o.userId = ?2")
    boolean existByOrganizationIdAndUserId(String organizationId, String userId);

    @Query("select o.userId from OrgStaffEntity o where o.invitedBy = ?1")
    List<String> findUserIdByInvitedBy(String merchantAdminId);

    @Query("select o from OrgStaffEntity o where o.userId = ?1")
    Optional<OrgStaffEntity> findByUserId(String userId);


}
