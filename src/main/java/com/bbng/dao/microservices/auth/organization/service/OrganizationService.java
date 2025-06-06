package com.bbng.dao.microservices.auth.organization.service;


import com.bbng.dao.microservices.auth.organization.dto.request.UpdateOrgDto;
import com.bbng.dao.microservices.auth.organization.dto.response.KycStatsResponseDto;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OrganizationService {
    ResponseDto<String> updateBusinessDetails(UpdateOrgDto updateRequestDto);

    ResponseDto<OrganizationEntity> getOrganizationByUserId(String orgId);

}
