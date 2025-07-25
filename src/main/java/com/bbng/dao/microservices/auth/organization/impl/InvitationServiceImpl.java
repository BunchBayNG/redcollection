package com.bbng.dao.microservices.auth.organization.impl;


import com.bbng.dao.microservices.auth.auditlog.Events;
import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogRequestDto;
import com.bbng.dao.microservices.auth.auditlog.service.AuditLogService;
import com.bbng.dao.microservices.auth.organization.dto.request.AssignRoleRequestDto;
import com.bbng.dao.microservices.auth.organization.dto.request.InviteRequestDto;
import com.bbng.dao.microservices.auth.organization.dto.request.OnboardOrgDto;
import com.bbng.dao.microservices.auth.organization.entity.OrgStaffEntity;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.enums.InvitationStatus;
import com.bbng.dao.microservices.auth.organization.enums.OrgStatus;
import com.bbng.dao.microservices.auth.organization.repository.OrgStaffRepository;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.organization.service.InvitationService;
import com.bbng.dao.microservices.auth.organization.utils.PasswordGenerator;
import com.bbng.dao.microservices.auth.passport.dto.response.UserResponseDto;
import com.bbng.dao.microservices.auth.passport.entity.PermissionEntity;
import com.bbng.dao.microservices.auth.passport.entity.RoleEntity;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.enums.AcctStatus;
import com.bbng.dao.microservices.auth.passport.enums.UserType;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.DataInitializerServiceImpl;
import com.bbng.dao.microservices.auth.passport.repository.PermissionRepository;
import com.bbng.dao.microservices.auth.passport.repository.RoleRepository;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.util.email.service.EmailVerificationService;
import com.bbng.dao.util.exceptions.customExceptions.EmailAlreadyExistsException;
import com.bbng.dao.util.exceptions.customExceptions.ForbiddenException;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.exceptions.customExceptions.UserNotFoundException;
import com.bbng.dao.util.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {

    private static final SecureRandom secureRandom = new SecureRandom();
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OrgStaffRepository orgStaffRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailVerificationService emailVerificationService;
    private final PermissionRepository permissionRepository;
    private final AuditLogService auditLogService;


    @Override
    @Transactional
    public ResponseDto<String> inviteUser(InviteRequestDto inviteRequestDto) {
        UserEntity userEntity = userRepository.findById(inviteRequestDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id: %s not found", inviteRequestDto.getUserId())));

        if (userRepository.existsByEmail(inviteRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists. Bad request");
        }


        String password = PasswordGenerator.generatePassword();

        RoleEntity roleOrganizationStaff = roleRepository.findByRoleName("ROLE_ORGANIZATION_STAFF").orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        RoleEntity roleRedtechStaff = roleRepository.findByRoleName("ROLE_REDTECH_STAFF").orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        ///  this is to invite staff
        UserType userType = userEntity.getUsertype().equals(UserType.ORGANIZATION_ADMIN) ? UserType.ORGANIZATION_STAFF : UserType.REDTECH_STAFF;
        RoleEntity role = userType.equals(UserType.REDTECH_STAFF) ? roleRedtechStaff : roleOrganizationStaff;

        UserEntity newSavedUser = userRepository.save(UserEntity.builder()
                .email(inviteRequestDto.getEmail())
                .firstName(inviteRequestDto.getFirstName())
                .lastName(inviteRequestDto.getLastName())
                .userName(inviteRequestDto.getFirstName().toUpperCase() + " " + inviteRequestDto.getLastName())
                .password(passwordEncoder.encode(password))
                .usertype(userType)
                .roleEntities(Set.of(role))
                .isEnabled(false)
                .isInvitedUser(true)
                .build());


        if (userType.equals(UserType.ORGANIZATION_STAFF)) {

            OrganizationEntity organizationEntity = organizationRepository.findOrganizationByMerchantAdminId(userEntity.getId()).orElseThrow(() -> new ResourceNotFoundException("Organization with id not found"));

            OrgStaffEntity orgStaffEntity = OrgStaffEntity.builder()
                    .invitationStatus(InvitationStatus.AWAITING_ACTIVATION)
                    .organizationId(organizationEntity.getId())
                    .userId(newSavedUser.getId())
                    .userRole(inviteRequestDto.getRole())
                    .invitedBy(inviteRequestDto.getUserId())
                    .build();

            orgStaffRepository.save(orgStaffEntity);


            auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                    .userId(userEntity.getId())
                    .userName(userEntity.getUserName())
                    .event(Events.INVITE_STAFF.name())
                    .isDeleted(false)
                    .description("Invitation to the provided user has be sent out!")
                    .merchantName(organizationEntity.getOrganizationName())
                    .merchantId(organizationEntity.getId())
                    .build());


            emailVerificationService.sendInvitationEmail(inviteRequestDto.getEmail(), organizationEntity.getOrganizationName(), password);

        }

        if(userType.equals(UserType.REDTECH_STAFF)) {

            OrgStaffEntity orgStaffEntity = OrgStaffEntity.builder()
                    .invitationStatus(InvitationStatus.AWAITING_ACTIVATION)
                    .organizationId("${orgId}")
                    .userId(newSavedUser.getId())
                    .userRole(inviteRequestDto.getRole())
                    .invitedBy(inviteRequestDto.getUserId())
                    .build();

            orgStaffRepository.save(orgStaffEntity);

            auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                    .userId(userEntity.getId())
                    .userName(userEntity.getUserName())
                    .event(Events.INVITE_STAFF.name())
                    .isDeleted(false)
                    .description("Invitation to the provided user has be sent out!")
                    .merchantName("${orgName}")
                    .merchantId("${orgId}")
                    .build());


            emailVerificationService.sendInvitationEmail(inviteRequestDto.getEmail(), "${orgName}", password);


        }


        return ResponseDto.<String>builder()
                .statusCode(201)
                .status(true)
                .message("Signup successful")
                .data(String.format("User with id: %s has been invited. Ask to check email for verification", newSavedUser.getId()))
                .build();

    }

    @Override
    public ResponseDto<List<UserResponseDto>> getAllStaff(String merchantAdminId) {
        //search the org staff repo and retrieve all the userId using the invitedById
        List<String> staffIds = orgStaffRepository.findUserIdByInvitedBy(merchantAdminId);

        // loop through them and get their userEntity
        List<UserEntity> userEntities = new ArrayList<>();
        for (String id : staffIds) {
            UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("This user is not found,. UserId: " + id));
            if (user.isInvitedUser()) {
                userEntities.add(user);
            }
        }

        List<UserResponseDto> result = userEntities.stream().map(user -> UserResponseDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .logoUrl(user.getLogoUrl())
                .phoneNumber(user.getPhoneNumber())
                .isEnabled(user.getIsEnabled())
                .isInvitedUser(user.isInvitedUser())
                .username(user.getUserName())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .userType(user.getUsertype().name()).build()).toList();

        return ResponseDto.<List<UserResponseDto>>builder()
                .status(true)
                .message("Returned All Staff that belong to a merchant")
                .statusCode(200)
                .data(result).build();
    }

    @Override
    public ResponseDto<String> disableStaff(String merchantAdminId, String staffId) {
        //get the user
        UserEntity userEntity = userRepository.findById(merchantAdminId).orElseThrow(() -> new UserNotFoundException("No user Found in the system with the provided merchantAdminId: " + merchantAdminId));
        boolean isOrganisationAdmin = userEntity.getUsertype().equals(UserType.ORGANIZATION_ADMIN);
        if (isOrganisationAdmin) {
            // get organization staff by the staff id
            OrgStaffEntity staffOrg = orgStaffRepository.findOrganizationByUserId(staffId).orElseThrow(() -> new UserNotFoundException("Can't find staff in the organizationStaff. Does this staff belongs to an organization?"));
            if (!staffOrg.getInvitedBy().equals(merchantAdminId)) {
                throw new ForbiddenException("As an OrganizationAdmin you can only disable a staff in your organization");
            }
        }
        UserEntity staff = userRepository.findById(staffId).orElseThrow(() -> new UserNotFoundException("No user found with the provided staffId: " + staffId));
        staff.setIsEnabled(false);
        userRepository.save(staff);
        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(userEntity.getId())
                .userName(userEntity.getUserName())
                .event(Events.DISABLE_STAFF.name())
                .isDeleted(false)
                .description("Staff has been disabled")
                .merchantName(userEntity.getFirstName() + " " + userEntity.getLastName())
                .merchantId(userEntity.getId())
                .build());
        return ResponseDto.<String>builder()
                .status(true)
                .statusCode(200)
                .message("Staff has been disabled!")
                .data("Staff with the id: " + staffId + " has been successfully disabled")
                .build();
    }

    @Override
    public ResponseDto<String> onboardOrg(OnboardOrgDto onboardOrgDto) {

        UserEntity userEntity = userRepository.findById(onboardOrgDto.getInviteUserId()).orElseThrow(()
                -> new ResourceNotFoundException(String.format("User with id: %s not found", onboardOrgDto.getInviteUserId())));

        if (userRepository.existsByEmail(onboardOrgDto.getContactEmail())) {
            throw new EmailAlreadyExistsException("Email already exists. Bad request");
        }

        String password = PasswordGenerator.generatePassword();

        RoleEntity roleOrganizationAdmin = roleRepository.findByRoleName("ROLE_ORGANIZATION_ADMIN").
                orElseThrow(() -> new ResourceNotFoundException("Role not found"));




        UserEntity newSavedUser = userRepository.save(UserEntity.builder()
                .email(onboardOrgDto.getContactEmail())
                .firstName(onboardOrgDto.getContactFirstName())
                .lastName(onboardOrgDto.getContactLastName())
                .phoneNumber(onboardOrgDto.getPhoneNumber())
                .userName(onboardOrgDto.getContactFirstName().toUpperCase() + " " + onboardOrgDto.getContactLastName())
                .password(passwordEncoder.encode(password))
                .usertype(UserType.ORGANIZATION_ADMIN)
                .acctStatus(AcctStatus.ACTIVE)
                .roleEntities(Set.of(roleOrganizationAdmin))
                .isEnabled(false)
                .isInvitedUser(true)
                .build());


        OrganizationEntity newOrgEntity = new OrganizationEntity();

        newOrgEntity.setMerchantAdminId(newSavedUser.getId());
        newOrgEntity.setOrganizationName(onboardOrgDto.getOrganizationName());
        newOrgEntity.setProductPrefix(onboardOrgDto.getProductPrefix());
        newOrgEntity.setOrgStatus(OrgStatus.ACTIVE);
        newOrgEntity.setBusinessLogoUrl(onboardOrgDto.getBusinessLogoUrl());


        ///  todo: make a call to UBA to get this data
        newOrgEntity.setRegisteredBVN(onboardOrgDto.getRegisteredBVN());
        newOrgEntity.setContactLastName(onboardOrgDto.getContactLastName());
        newOrgEntity.setContactFirstName(onboardOrgDto.getContactFirstName());
        newOrgEntity.setSettlementAccountNumber(generateAccountNumber());
        newOrgEntity.setSettlementAccountStatus("BALANCED");
        newOrgEntity.setSettlementBankName("United Bank of Africa");
        newOrgEntity.setSettlementAccountName("UBA-" + onboardOrgDto.getContactFirstName() + " " + onboardOrgDto.getContactLastName());

        organizationRepository.save(newOrgEntity);


        OrgStaffEntity orgStaffEntity = OrgStaffEntity.builder()
                .invitationStatus(InvitationStatus.AWAITING_ACTIVATION)
                .organizationId(newOrgEntity.getId())
                .userId(newSavedUser.getId())
                .userRole(null)
                .invitedBy(onboardOrgDto.getInviteUserId())
                .build();

        orgStaffRepository.save(orgStaffEntity);
        emailVerificationService.sendInvitationEmail(onboardOrgDto.getContactEmail(), newOrgEntity.getOrganizationName(), password);

        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(userEntity.getId())
                .userName(userEntity.getUserName())
                .event(Events.ONBOARD_MERCHANT.name())
                .isDeleted(false)
                .description("New Merchant has been onboarded!")
                .merchantName(newOrgEntity.getOrganizationName())
                .merchantId(newOrgEntity.getId())
                .build());


        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(userEntity.getId())
                .userName(userEntity.getUserName())
                .event(Events.ONBOARD_MERCHANT_ADMIN.name())
                .isDeleted(false)
                .description("Invitation to the provided user has be sent out!")
                .merchantName(newOrgEntity.getOrganizationName())
                .merchantId(newOrgEntity.getId())
                .build());


        return ResponseDto.<String>builder()
                .statusCode(201)
                .status(true)
                .message("Merchant onboard successful")
                .data(String.format("Merchant onboard successful, and merchant admin with id: %s has been invited. Ask to check email for verification", newSavedUser.getId()))
                .build();


    }


    public String generateAccountNumber() {
        // Generate a 10-digit number
        long accountNumber = secureRandom.nextLong(1_000_000_000L, 10_000_000_000L);
        return String.format("%010d", accountNumber); // Ensure it's 10 digits
    }

    @Override
    public ResponseDto<String> assignPermissionsToRole(AssignRoleRequestDto assignRoleRequestDto, DataInitializerServiceImpl dataInitializerService) {
        String adminId = assignRoleRequestDto.getAdminId();
        UserEntity admin = userRepository.findById(adminId).orElseThrow(() -> new UserNotFoundException("No User Found with the given user id: " + adminId));

        String staffEmail = assignRoleRequestDto.getEmail();
        UserEntity staff = userRepository.findByEmail(staffEmail).orElseThrow(() -> new ResourceNotFoundException("Can't find a staff with the provided email: " + staffEmail));

        if (admin.getUsertype().equals(UserType.ORGANIZATION_ADMIN)) {
            /// log.info("making sure this staff belongs to this organization");
            //make sure this staff belongs to his organization
            OrgStaffEntity orgStaffEntity = orgStaffRepository.findByUserId(staff.getId()).orElseThrow(() -> new ResourceNotFoundException("Can't find this staff in the organizationStaff"));
            if (!orgStaffEntity.getInvitedBy().equals(adminId)) {
                throw new ForbiddenException("You can only assign role to a staff that belongs to your organization");
            }
        }

        String roleId = assignRoleRequestDto.getRoleId();

        log.info("proceeding with assigning role to th staff");
        RoleEntity role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("No Role found with the given RoleId: " + roleId));
        List<PermissionEntity> permissionEntityList = new ArrayList<>();
        //set the permissions if it's not empty
        for (Long permissionId : assignRoleRequestDto.getPermissionsIds()) {
            PermissionEntity permission = permissionRepository.findById(permissionId).orElseThrow(() ->
                    new ResourceNotFoundException("No permission Found by the given PermissionsId: " + permissionId));
            permissionEntityList.add(permission);
        }
        log.info("Assigning permissions to the role");
        RoleEntity roleWithPermission = dataInitializerService.createRoleAndAssignPermissions(role.getRoleName(), permissionEntityList);


        log.info("done return results");
        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(adminId)
                .userName(admin.getUserName())
                .event(Events.ASSIGN_PERMISSION.name())
                .isDeleted(false)
                .description("Permission has been assigned to a staff")
                .merchantName(admin.getFirstName() + " " + admin.getLastName())
                .merchantId(adminId)
                .build());

        return ResponseDto.<String>builder()
                .message("Role has been accessing permissions successfully")
                .statusCode(200)
                .status(true)
                .data(String.format("Role %s has successfully been assign to permissions", roleWithPermission.getRoleName()))
                .build();

    }


    @Override
    public ResponseDto<String> createRoleWithPermissions(AssignRoleRequestDto assignRoleRequestDto, DataInitializerServiceImpl dataInitializerService) {
        List<PermissionEntity> permissionEntityList = new ArrayList<>();
        if (!assignRoleRequestDto.getPermissionsIds().isEmpty()) {
            log.info("permission list is not empty");
            for (Long permissionId : assignRoleRequestDto.getPermissionsIds()) {
                PermissionEntity permission = permissionRepository.findById(permissionId).orElseThrow(() ->
                        new ResourceNotFoundException("No permission Found by the given PermissionsId: " + permissionId));
                permissionEntityList.add(permission);
            }
        }
        log.info("permission list can be empty");
        RoleEntity role = dataInitializerService.createRoleAndAssignPermissions(assignRoleRequestDto.getRoleName(), permissionEntityList);
        // get the user using his email

        UserEntity user = userRepository.findByEmail(assignRoleRequestDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found with the provided email"));
        user.setRoleEntities(new HashSet<>(Set.of(role)));
        log.info("done adding roles to the user");
        userRepository.save(user);
        return ResponseDto.<String>builder()
                .message("Role Successfully created")
                .data("Role has been created Successfully! role ID: " + role.getId())
                .status(true)
                .statusCode(201)
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<String> disablePermission(String username, String merchantAdminId, Long permissionId) {
        log.info("we are in the disable permission service");
        //get the user with the id
        UserEntity authUser = userRepository.findByUsernameOrEmail(username, null).orElseThrow(() -> new UserNotFoundException("no user found with the extracted token user"));
        if (authUser.getUsertype().equals(UserType.ORGANIZATION_STAFF)) {
            throw new ForbiddenException("Only User with userType Organization Admin can perform this operation");
        }
        UserEntity staff = userRepository.findById(merchantAdminId).orElseThrow(() -> new UserNotFoundException("No user found with the given merchantId"));

        if (authUser.getUsertype().equals(UserType.ORGANIZATION_ADMIN)) {
            // make sure it's only staff from his organization he can disable
            log.info("making sure this staff belongs to these organization");
            //make sure this staff belongs to his organization
            OrgStaffEntity orgStaffEntity = orgStaffRepository.findByUserId(staff.getId()).orElseThrow(() -> new ResourceNotFoundException("Can't find this staff in the organizationStaff"));
            if (!orgStaffEntity.getInvitedBy().equals(authUser.getId())) {
                throw new ForbiddenException("You can only disable permission to a staff that belongs to your organization");
            }
        }
        PermissionEntity permission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException("No permission found with the given permissionId: " + permissionId));

        staff.getRoleEntities()
                .forEach(roleEntity -> {
                    Set<PermissionEntity> permissionEntities = roleEntity.getPermissions().stream()
                            .filter(permissionEntity ->
                                    !permissionEntity.getName().equals(permission.getName()))
                            .collect(Collectors.toSet());
                    roleEntity.setPermissions(permissionEntities);
                });

        userRepository.save(staff);
        log.info("done saving the staff entity");
        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(authUser.getId())
                .userName(authUser.getUserName())
                .event(Events.DISABLE_PERMISSION.name())
                .isDeleted(false)
                .description("Permission has been disabled for a staff")
                .merchantName(authUser.getUserName())
                .merchantId(authUser.getId())
                .build());
        return ResponseDto.<String>builder()
                .message("permission disabled SuccessFully")
                .status(true)
                .statusCode(200)
                .data("Permission with permissionName: " + permission.getName() + " has been disable for the user: " + staff.getUserName())
                .build();
    }


}
