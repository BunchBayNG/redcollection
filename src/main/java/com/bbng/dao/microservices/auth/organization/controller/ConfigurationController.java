package com.bbng.dao.microservices.auth.organization.controller;


import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController("api/v1/redtech/admin/manage-config")
public class ConfigurationController {

    private final PermissionService permissionService;
    private final JWTService jwtService;
    private final HttpServletRequest request;

//    @PutMapping("setup-user-config")
//    public ResponseEntity<ResponseDto<String>> updateConfiguration(@RequestBody ConfigSetupDto configSetupDto) {
//
//
//        permissionService.checkPermission(request, "ADMIN_SET_UP_USER_CONFIG", jwtService);
//        return ResponseEntity.status(HttpStatus.OK).body(configService.updateUserConfiguration(configSetupDto));
//    }


}
