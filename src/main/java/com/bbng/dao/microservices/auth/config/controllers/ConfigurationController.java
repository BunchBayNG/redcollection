package com.bbng.dao.microservices.auth.config.controllers;


import com.bbng.dao.microservices.auth.config.dto.request.ConfigSetupDto;
import com.bbng.dao.microservices.auth.config.entity.ServiceConfigEntity;
import com.bbng.dao.microservices.auth.config.entity.SystemConfigEntity;
import com.bbng.dao.microservices.auth.config.service.ConfigService;
import com.bbng.dao.microservices.auth.config.service.ServiceConfigService;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.PermissionService;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("${apiVersion}" +  "/admin/manage-config")
public class ConfigurationController {

    private final PermissionService permissionService;
    private final JWTService jwtService;
    private final HttpServletRequest request;
    private final ConfigService configService;
    private final ServiceConfigService serviceConfigService;

//    @PutMapping("setup-user-config")
//    public ResponseEntity<ResponseDto<String>> updateConfiguration(@RequestBody ConfigSetupDto configSetupDto) {
//
//
//        permissionService.checkPermission(request, "ADMIN_SET_UP_USER_CONFIG", jwtService);
//        return ResponseEntity.status(HttpStatus.OK).body(configService.updateUserConfiguration(configSetupDto));
//    }


    @GetMapping("/get")
    public ResponseEntity<ResponseDto<SystemConfigEntity>> getConfig() {
        return ResponseEntity.ok(configService.getConfigResponse());
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto<String>> updateConfig(@Valid @RequestBody ConfigSetupDto request) {
        return ResponseEntity.ok(configService.updateUserConfiguration(request));
    }

    @GetMapping
    public ResponseEntity<List<ServiceConfigEntity>> getAll() {
        return ResponseEntity.ok(serviceConfigService.getAll());
    }


}
