package com.bbng.dao.microservices.auth.organization.controller;


import com.bbng.dao.microservices.auth.organization.dto.request.AssignRoleRequestDto;
import com.bbng.dao.microservices.auth.organization.dto.request.InviteRequestDto;
import com.bbng.dao.microservices.auth.organization.dto.request.OnboardOrgDto;
import com.bbng.dao.microservices.auth.organization.service.InvitationService;
import com.bbng.dao.microservices.auth.organization.utils.GetUserFromToken;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.dto.response.UserResponseDto;
import com.bbng.dao.microservices.auth.passport.entity.PermissionEntity;
import com.bbng.dao.microservices.auth.passport.entity.RoleEntity;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.enums.UserType;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.DataInitializerServiceImpl;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.PermissionService;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.util.exceptions.customExceptions.UnauthorizedException;
import com.bbng.dao.util.exceptions.customExceptions.UserNotFoundException;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@RestController("api/v1/users")
public class InvitationController {

    private final InvitationService invitationService;
    private final PermissionService permissionService;
    private final HttpServletRequest request;
    private final DataInitializerServiceImpl dataInitializerService;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    @PostMapping("invite")
    public ResponseEntity<ResponseDto<String>> inviteUser(@RequestBody InviteRequestDto inviteRequestDto) {

        // make sure it's only those that have this permissions can actually access this route
        permissionService.checkPermission(request, "CAN_INVITE_STAFF", jwtService);
        return ResponseEntity.status(HttpStatus.OK).body(invitationService.inviteUser(inviteRequestDto));
    }


    @GetMapping("get-staff")
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getAllOrgStaff(@RequestParam String merchantAdminId) {

        permissionService.checkPermission(request, "CAN_GET_STAFF", jwtService);
        return ResponseEntity.status(HttpStatus.OK).body(invitationService.getAllStaff(merchantAdminId));
    }

    @PutMapping("disable-staff")
    public ResponseEntity<ResponseDto<String>> disableStaff(@RequestParam String merchantAdminId, @RequestParam String staffId) {

        permissionService.checkPermission(request, "CAN_DISABLE_STAFF", jwtService);

        return ResponseEntity.status(HttpStatus.OK).body(invitationService.disableStaff(merchantAdminId, staffId));
    }

    @PutMapping("onboard-org")
    public ResponseEntity<ResponseDto<String>> onboardOrg(@RequestBody OnboardOrgDto onboardOrgDto) {

        permissionService.checkPermission(request, "CAN_ONBOARD_ORG", jwtService);

        return ResponseEntity.status(HttpStatus.OK).body(invitationService.onboardOrg(onboardOrgDto));
    }


    @PutMapping("remove-permission")
    public ResponseEntity<ResponseDto<String>> removePermission(@RequestParam String merchantAdminId, @RequestParam Long permissionId) {

        permissionService.checkPermission(request, "CAN_REMOVE_PERMISSION", jwtService);
        String username = extractTokenFromHeader();
        return ResponseEntity.status(HttpStatus.OK).body(invitationService.disablePermission(username, merchantAdminId, permissionId));
    }

    @PutMapping("assign-role")
    public ResponseEntity<ResponseDto<String>> assignRole(@RequestBody AssignRoleRequestDto assignRoleRequestDto) {
        // check if roleId is present, if it's not present, then the merchant want to create a new Role
        if (assignRoleRequestDto.getRoleId() == null || assignRoleRequestDto.getRoleId().isEmpty()) {

            permissionService.checkPermission(request, "CAN_CREATE_ROLE", jwtService);
            // create roles
            log.info("creating new roles");
            return ResponseEntity.status(HttpStatus.OK).body(invitationService.createRoleWithPermissions(assignRoleRequestDto, dataInitializerService));
        }

        permissionService.checkPermission(request, "CAN_ASSIGN_PERMISSIONS", jwtService);

        return ResponseEntity.status(HttpStatus.OK).body(invitationService.assignPermissionsToRole(assignRoleRequestDto, dataInitializerService));
    }


    public void givePermissionToAdmins(HttpServletRequest request, Optional<PermissionEntity> permissionEntityOptional, boolean giveToredtechStaff) {
        if (permissionEntityOptional.isPresent()) {
            //extract token from request
            String userName = GetUserFromToken.extractTokenFromHeader(request, jwtService);
            UserEntity user = userRepository.findByUsernameOrEmail(userName, null).orElseThrow(() -> new
                    UserNotFoundException("Can't get user form your token. Is user already redtech user?"));
            boolean admin;
            if (giveToredtechStaff) {
                admin = user.getUsertype().equals(UserType.SUPER_ADMIN) || user.getUsertype().equals(UserType.ORGANIZATION_ADMIN) || user.getUsertype().equals(UserType.REDTECH_STAFF);
                //get his roles and check if he alred has this permission, if not give it that permission, else no nothing
                if (admin) {
                    log.info("its allowed user to create roles");
                    boolean hasPermissionAlready = user.getRoleEntities().stream().map(RoleEntity::getPermissions).anyMatch(permissionEntities -> permissionEntities.contains(permissionEntityOptional.get()));
                    if (!hasPermissionAlready) {
                        log.info("no match found creating roles");
                        dataInitializerService.createRoleAndAssignPermissions("ROLE_ORGANIZATION_ADMIN", List.of(permissionEntityOptional.get()));
                        dataInitializerService.createRoleAndAssignPermissions("ROLE_REDTECH_STAFF", List.of(permissionEntityOptional.get()));
                        dataInitializerService.createRoleAndAssignPermissions("ROLE_SUPER_ADMIN", List.of(permissionEntityOptional.get()));
                    }
                }


            } else {
                admin = user.getUsertype().equals(UserType.SUPER_ADMIN) || user.getUsertype().equals(UserType.ORGANIZATION_ADMIN);
                if (admin) {
                    log.info("its allowed user to create roles");
                    boolean hasPermissionAlready = user.getRoleEntities().stream().map(RoleEntity::getPermissions).anyMatch(permissionEntities -> permissionEntities.contains(permissionEntityOptional.get()));
                    if (!hasPermissionAlready) {
                        log.info("no match found creating roles");
                        dataInitializerService.createRoleAndAssignPermissions("ROLE_ORGANIZATION_ADMIN", List.of(permissionEntityOptional.get()));
                        dataInitializerService.createRoleAndAssignPermissions("ROLE_SUPER_ADMIN", List.of(permissionEntityOptional.get()));
                    }
                }
            }


        }


    }

    private String extractTokenFromHeader() {
        // get the header
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            throw new UnauthorizedException("You are not authorized to make this request, please log in!");
        }
        //get the token
        String token = header.substring(7);
        //get the username from the token
        return jwtService.getUsername(token);

    }

}
