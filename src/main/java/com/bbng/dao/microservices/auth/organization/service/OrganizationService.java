package com.bbng.dao.microservices.auth.organization.service;


import com.bbng.dao.microservices.auth.organization.dto.request.UpdateOrgDto;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface OrganizationService {
    ResponseDto<String> updateBusinessDetails(UpdateOrgDto updateRequestDto);

    ResponseDto<OrganizationEntity> getOrganizationByUserId(String orgId);

    ResponseDto<Page<OrganizationEntity>>
    getAllOrg(String search, String merchantOrgId, String status, String sortBy,
              String sortOrder, LocalDate startDate, LocalDate endDate, int page, int size);
}
