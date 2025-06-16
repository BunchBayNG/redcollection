package com.bbng.dao.microservices.auth.organization.service;


import com.bbng.dao.microservices.auth.organization.dto.request.UpdateOrgDto;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.report.dto.CustomerFilterRequestDto;
import com.bbng.dao.microservices.report.dto.OrgFilterRequestDto;
import com.bbng.dao.microservices.report.entity.CustomerEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

public interface OrganizationService {
    ResponseDto<String> updateBusinessDetails(UpdateOrgDto updateRequestDto);

    ResponseDto<OrganizationEntity> getOrganizationByUserId(String orgId);

    ResponseDto<Page<OrganizationEntity>>  getAllOrg(OrgFilterRequestDto request);
}
