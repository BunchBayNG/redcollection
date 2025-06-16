package com.bbng.dao.microservices.auth.passport.service;


import com.bbng.dao.microservices.auth.organization.dto.request.CreateLiveApiKeyRequestDto;
import com.bbng.dao.microservices.auth.organization.dto.request.CreateTestApiKeyRequestDto;
import com.bbng.dao.util.response.ResponseDto;

public interface ApiKeyService {
    ResponseDto<String> createTestKey(CreateTestApiKeyRequestDto testApiKeyRequestDto, String username);

    ResponseDto<String> createLiveKey(CreateLiveApiKeyRequestDto createLiveApiKey, String username);
}
