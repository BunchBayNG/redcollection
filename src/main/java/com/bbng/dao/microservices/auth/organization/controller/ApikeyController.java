package com.bbng.dao.microservices.auth.organization.controller;

import com.bbng.dao.microservices.auth.organization.dto.request.CreateLiveApiKeyRequestDto;
import com.bbng.dao.microservices.auth.organization.dto.request.CreateTestApiKeyRequestDto;
import com.bbng.dao.microservices.auth.organization.utils.GetUserFromToken;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.PermissionService;
import com.bbng.dao.microservices.auth.passport.service.ApiKeyService;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${apiVersion}" +  "/apikey")

public class ApikeyController {
    private final ApiKeyService apiKeyService;
    private final PermissionService permissionService;
    private final JWTService jwtService;
    private final HttpServletRequest request;


    public ApikeyController(ApiKeyService apiKeyService, PermissionService permissionService, JWTService jwtService, HttpServletRequest request) {
        this.apiKeyService = apiKeyService;
        this.permissionService = permissionService;
        this.jwtService = jwtService;
        this.request = request;
    }

    @PostMapping("create-test-apiKey")
    public ResponseEntity<ResponseDto<String>> createTestApiKey(@RequestBody CreateTestApiKeyRequestDto testKeyRequest){
        log.info("assigning permissions to create testApiKey");

        permissionService.checkPermission(request,"CREATE_TEST_APIKEY", jwtService);
        String username = GetUserFromToken.extractTokenFromHeader(request,jwtService);
        return ResponseEntity.status(HttpStatus.OK).body(apiKeyService.createTestKey(testKeyRequest, username));
    }

    @PostMapping("create-live-apiKey")
    public ResponseEntity<ResponseDto<String>> createLiveApiKey(@RequestBody CreateLiveApiKeyRequestDto liveKeyRequest){
        log.info("assigning permissions to Create Live api key");

        permissionService.checkPermission(request,"CREATE_LIVE_APIKEY", jwtService);
        String username = GetUserFromToken.extractTokenFromHeader(request, jwtService);
        return ResponseEntity.status(HttpStatus.OK).body(apiKeyService.createLiveKey(liveKeyRequest, username));
    }

}
