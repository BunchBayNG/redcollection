package com.bbng.dao.microservices.auth.config.service;


import com.bbng.dao.microservices.auth.config.dto.request.ConfigSetupDto;
import com.bbng.dao.microservices.auth.config.entity.SystemConfigEntity;
import com.bbng.dao.util.response.ResponseDto;

public interface ConfigService {

    ResponseDto<String> updateUserConfiguration(ConfigSetupDto configSetupDto);
    ResponseDto<SystemConfigEntity> getConfigResponse();
    SystemConfigEntity getConfig();

}
