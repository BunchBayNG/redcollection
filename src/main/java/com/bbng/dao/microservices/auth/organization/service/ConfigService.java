package com.bbng.dao.microservices.auth.organization.service;


import com.bbng.dao.microservices.auth.organization.dto.request.ConfigSetupDto;
import com.bbng.dao.microservices.auth.organization.entity.SystemConfigEntity;
import com.bbng.dao.util.response.ResponseDto;

public interface ConfigService {

    ResponseDto<String> updateUserConfiguration(ConfigSetupDto configSetupDto);
    ResponseDto<SystemConfigEntity> getConfigResponse();
    SystemConfigEntity getConfig();

}
