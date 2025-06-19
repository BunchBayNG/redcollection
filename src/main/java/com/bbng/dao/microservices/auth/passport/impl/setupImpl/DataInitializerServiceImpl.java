package com.bbng.dao.microservices.auth.passport.impl.setupImpl;


import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.passport.dto.request.SignUpDto;
import com.bbng.dao.microservices.auth.passport.entity.PermissionEntity;
import com.bbng.dao.microservices.auth.passport.entity.RoleEntity;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.enums.AcctStatus;
import com.bbng.dao.microservices.auth.passport.enums.UserType;
import com.bbng.dao.microservices.auth.passport.repository.PermissionRepository;
import com.bbng.dao.microservices.auth.passport.repository.RoleRepository;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.util.exceptions.customExceptions.InternalServerException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Component
@Transactional
@Slf4j
public class DataInitializerServiceImpl implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;


    private Map<String, List<String>> rolePermissionsMap;


    public DataInitializerServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository,
                                      PermissionService permissionService, PasswordEncoder passwordEncoder,
                                      UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.roleRepository = roleRepository;

        this.permissionRepository = permissionRepository;
        this.permissionService = permissionService;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;

    }


    @PostConstruct
    public void initialize() {
        this.rolePermissionsMap = allocatePermissionsToRoles();
    }


    @Override
    public void run(String... args) {
        createRoleIfNotFound("ROLE_ORGANIZATION_ADMIN");
        createRoleIfNotFound("ROLE_SUPER_ADMIN");
        createRoleIfNotFound("ROLE_REDTECH_STAFF");
        createRoleIfNotFound("ROLE_ORGANIZATION_STAFF");
        //        Permission creation coming thru
        permissionService.createPermissionIfNotFound("CREATE_USER", "The \"Create\" operation involves adding new data to the system.");
        permissionService.createPermissionIfNotFound("READ_USER", "The \"Read\" operation involves retrieving existing data from the system.");
        permissionService.createPermissionIfNotFound("UPDATE_USER", "The \"Update\" operation involves modifying existing data in the system.");
        permissionService.createPermissionIfNotFound("DELETE_USER", "The \"Delete\" operation involves removing existing data from the system.");

        rolePermissionsMap = allocatePermissionsToRoles();

        createSystemUser(SignUpDto.builder()
                .firstName("Super")
                .lastName("Admin")
                .organizationName("REDTECH")
                .email("cakpomughe@gmail.com")
                .password(passwordEncoder.encode("123456Abc!"))
                .build());
        createRedtechAdminPermissions();
        createMerchantAdminPermissions();
    }

    private void createMerchantAdminPermissions() {
        List<String> names = new ArrayList<>(Arrays.asList(
                "ADMIN_GET_ACCOUNT_NUMBER",
                "CREATE_TEST_APIKEY",
                "CREATE_LIVE_APIKEY",
                "CAN_INVITE_STAFF",
                "CAN_GET_STAFF",
                "CAN_DISABLE_STAFF",
                "CAN_ONBOARD_ORG",
                "CAN_REMOVE_PERMISSION",
                "CAN_CREATE_ROLE",
                "CAN_ASSIGN_PERMISSIONS",
                "UPDATE_BUSINESS_ON_SIGNUP",
                "UPDATE_COMPANY_DETAILS",
                "GET_ORGANIZATION_BY_USERID",
                "GET-ORGANIZATION-NOTIFICATIONS",
                "DELETE-ORGANIZATION-NOTIFICATIONS",
                "MERCHANT_GET_AUDIT_LOGS",
                "GET-DAILY-REPORTS",
                "GET-GRAPH-REPORTS",
                "SUBMIT_DISPUTE",
                "CHECK_DISPUTE_STATUS"
        ));

        List<String> descriptions = new ArrayList<>(Arrays.asList(
                "the \"ADMIN_GET_ACCOUNT_NUMBER\" permission grant the permission to get an account number",
                "the \"CREATE_TEST_APIKEY\" permission grant the permission to create test ApiKey",
                "the \"CREATE_LIVE_APIKEY\" permission grant the permission to create live ApiKey",
                "the \"CAN_INVITE_STAFF\" permission grant the permission to invite staff",
                "the \"CAN_GET_STAFF\" permission grant the permission to get staff",
                "the \"CAN_DISABLE_STAFF\" permission grant the permission to disable staff",
                "the \"CAN_ONBOARD_ORG\" permission grant the permission to onboard merchant",
                "the \"CAN_REMOVE_PERMISSION\" permission grant the permission to remove permission",
                "the \"CAN_CREATE_ROLE\" permission grant the permission to create role",
                "the \"CAN_ASSIGN_PERMISSIONS\" permission grant the permission to assign permissions",
                "the \"UPDATE_BUSINESS_ON_SIGNUP\" permission grant the permission to update business on signUp",
                "the \"UPDATE_COMPANY_DETAILS\" permission grant the permission to update company details",
                "the \"GET_ORGANIZATION_BY_USERID\" permission grant the permission to get organization by userId",
                "the \"GET-ORGANIZATION-NOTIFICATIONS\" permission grant the permission to get an organization Notifications",
                "the \"DELETE-ORGANIZATION-NOTIFICATIONS\" permission grant the permission to delete an organization Notifications",
                "the \"MERCHANT_GET_AUDIT_LOGS\" permission grant the permission to get merchant audit logs",
                "the \"GET-DAILY-REPORTS\" permission grant the permission to get daily reports",
                "the \"GET-GRAPH-REPORTS\" permission grant the permission to get graph reports",
                "the \"SUBMIT_DISPUTE\" permission grant the permission to submit a dispute",
                "the \"CHECK_DISPUTE_STATUS\" permission grant the permission to check a dispute status"

        ));
        Set<PermissionEntity> permissionEntitySet = permissionService.createListOfPermissionIfNotFound(names, descriptions);
        List<PermissionEntity> permissionEntityList = new ArrayList<>(permissionEntitySet);
        log.info("creating permissions for organization admin");
        assignPermissionsToUserType(permissionEntityList);

    }

    private void assignPermissionsToUserType(List<PermissionEntity> permissionEntityList) {
        List<UserEntity> users = userRepository.findByOrganizationAdmin();
        users.forEach(user -> {
            user.getRoleEntities().forEach(role -> {
                createRoleAndAssignPermissions(role.getRoleName(), permissionEntityList);
            });
        });


    }


    private void createRedtechAdminPermissions() {
        List<String> names = new ArrayList<>(Arrays.asList(
                "ADMIN_GET_AUDIT_LOGS",
                "CREATE_TEST_APIKEY",
                "CREATE_LIVE_APIKEY",
                "ADMIN_GET_SENT_EMAILS",
                "ADMIN_SEND_EMAILS_NOTIFICATIONS",
                "ADMIN_UPLOAD_EMAILS_HEADER_IMAGE",
                "ADMIN_SET_UP_USER_CONFIG",
                "CAN_INVITE_STAFF",
                "CAN_GET_STAFF",
                "CAN_DISABLE_STAFF",
                "CAN_ONBOARD_ORG",
                "CAN_REMOVE_PERMISSION",
                "CAN_CREATE_ROLE",
                "CAN_ASSIGN_PERMISSIONS",
                "UPDATE_BUSINESS_ON_SIGNUP",
                "UPDATE_COMPANY_DETAILS",
                "GET_ORGANIZATION_BY_USERID",
                "GET-ORGANIZATION-NOTIFICATIONS",
                "SEND_NOTIFICATIONS",
                "DELETE-ORGANIZATION-NOTIFICATIONS",
                "ADMIN_DELETE_ACCOUNT_TRANSACTION",
                "GET-DAILY-REPORTS",
                "GET-GRAPH-REPORTS",
                "SUBMIT_DISPUTE",
                "CHECK_DISPUTE_STATUS",
                "ADMIN_ATTAIN_TO_DISPUTE",
                "ADMIN_GET_DISPUTES"
        ));

        List<String> descriptions = new ArrayList<>(Arrays.asList(

                "the \"ADMIN_GET_AUDIT_LOGS\" permission grant the permission to get audit logs",
                "the \"CREATE_TEST_APIKEY\" permission grant the permission to create test ApiKey",
                "the \"CREATE_LIVE_APIKEY\" permission grant the permission to create live ApiKey",
                "the \"ADMIN_GET_SENT_EMAILS\" permission grant the permission to get sent emails",
                "the \"ADMIN_SEND_EMAILS_NOTIFICATIONS\" permission grant the permission to send email notifications",
                "the \"ADMIN_UPLOAD_EMAILS_HEADER_IMAGE\" permission grant the permission to upload email header image",
                "the \"ADMIN_SET_UP_USER_CONFIG\" permission grant the permission to set up user-config",
                "the \"CAN_INVITE_STAFF\" permission grant the permission to invite staff",
                "the \"CAN_GET_STAFF\" permission grant the permission to get staff",
                "the \"CAN_DISABLE_STAFF\" permission grant the permission to disable staff",
                "the \"CAN_ONBOARD_ORG\" permission grant the permission to onboard merchant",
                "the \"CAN_REMOVE_PERMISSION\" permission grant the permission to remove permission",
                "the \"CAN_CREATE_ROLE\" permission grant the permission to create role",
                "the \"CAN_ASSIGN_PERMISSIONS\" permission grant the permission to assign permissions",
                "the \"UPDATE_BUSINESS_ON_SIGNUP\" permission grant the permission to update business on signUp",
                "the \"UPDATE_COMPANY_DETAILS\" permission grant the permission to update company details",
                "the \"GET_ORGANIZATION_BY_USERID\" permission grant the permission to get organization by userId",
                "the \"GET-ORGANIZATION-NOTIFICATIONS\" permission grant the permission to get an organization Notifications",
                "the \"SEND_NOTIFICATIONS\" permission grant the permission to send out notifications to admins",
                "the \"DELETE-ORGANIZATION-NOTIFICATIONS\" permission grant the permission to delete an organization Notifications",
                "the \"ADMIN_DELETE_ACCOUNT_TRANSACTION\" permission grant the permission to delete account Transactions",
                "the \"GET-DAILY-REPORTS\" permission grant the permission to get daily reports",
                "the \"GET-GRAPH-REPORTS\" permission grant the permission to get graph reports",
                "the \"SUBMIT_DISPUTE\" permission grant the permission to submit a dispute",
                "the \"CHECK_DISPUTE_STATUS\" permission grant the permission to check a dispute status",
                "the \"ADMIN_ATTAIN_TO_DISPUTE\" permission grant the permission to Attain to disputes",
                "the \"ADMIN_GET_DISPUTES\" permission grant the permission to get all disputes"
        ));
        Set<PermissionEntity> permissionEntitySet = permissionService.createListOfPermissionIfNotFound(names, descriptions);
        List<PermissionEntity> permissionEntityList = new ArrayList<>(permissionEntitySet);
        log.info("creating permissions for organization admin");
        assignPermissionsToUserType(permissionEntityList);

    }


    private void createRoleIfNotFound(String roleName) {
        Optional<RoleEntity> roleOptional = roleRepository.findByRoleName(roleName);
        if (roleOptional.isEmpty()) {
            RoleEntity role = RoleEntity.builder().roleName(roleName).build();
            roleRepository.save(role);
        }
    }

    private void createSystemUser(SignUpDto request) {

        Optional<UserEntity> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            UserEntity user = UserEntity.builder()
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .userName(request.getFirstName().toUpperCase() + " " + request.getLastName().toUpperCase())
                    .password(request.getPassword())
                    .usertype(UserType.SUPER_ADMIN).acctStatus(AcctStatus.ACTIVE)
                    .isEnabled(true)
                    .isInvitedUser(false)
                    .build();

            Optional<RoleEntity> roleOptional = roleRepository.findByRoleName("ROLE_SUPER_ADMIN");
            user.setRoleEntities(Collections.singleton(roleOptional.get()));
            userRepository.saveAndFlush(user);

//            UserEntity savedUser = userRepository.saveAndFlush(user);
//            OrganizationEntity organizationEntity = OrganizationEntity.builder()
//                    .organizationName(request.getOrganizationName())
//                    .merchantAdminId(savedUser.getId())
//                    .build();
//
//            OrganizationEntity savedOrg = organizationRepository.save(organizationEntity);
//            organizationRepository.save(savedOrg);

        } else if (optionalUser.get().getUserName() == null) {
            var user = optionalUser.get();
            user.setUserName(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
            userRepository.save(user);
        }
    }


    public Map<String, List<String>> allocatePermissionsToRoles() {
        try {
            // Map role names to associated permissions
            Map<String, List<String>> rolePermissionsMap = new HashMap<>();
            rolePermissionsMap.put("ROLE_SUPER_ADMIN", Arrays.asList(
                    "CREATE_USER", "READ_USER", "UPDATE_USER", "DELETE_USER",  "CAN_ONBOARD_ORG","CAN_CREATE_ROLE",
                    "ADMIN_GET_AUDIT_LOGS", "CREATE_TEST_APIKEY", "CAN_REMOVE_PERMISSION", "CAN_ASSIGN_PERMISSIONS",
                    "CREATE_LIVE_APIKEY", "ADMIN_GET_SENT_EMAILS", "UPDATE_BUSINESS_ON_SIGNUP", "UPDATE_COMPANY_DETAILS",
                    "ADMIN_SEND_EMAILS_NOTIFICATIONS", "ADMIN_UPLOAD_EMAILS_HEADER_IMAGE", "DELETE-ORGANIZATION-NOTIFICATIONS",
                    "ADMIN_SET_UP_USER_CONFIG", "CAN_INVITE_STAFF", "CAN_GET_STAFF", "CAN_DISABLE_STAFF",
                    "GET_ORGANIZATION_BY_USERID", "GET-ORGANIZATION-NOTIFICATIONS", "SEND_NOTIFICATIONS",
                    "ADMIN_DELETE_ACCOUNT_TRANSACTION", "GET-DAILY-REPORTS", "GET-GRAPH-REPORTS", "SUBMIT_DISPUTE",
                    "CHECK_DISPUTE_STATUS", "ADMIN_ATTAIN_TO_DISPUTE", "ADMIN_GET_DISPUTES"
                ));
            rolePermissionsMap.put("ROLE_ORGANIZATION_ADMIN", Arrays.asList("CREATE_USER", "READ_USER", "UPDATE_USER"));

            rolePermissionsMap.put("ROLE_REDTECH_STAFF", Arrays.asList( "ADMIN_GET_AUDIT_LOGS", "ADMIN_GET_SENT_EMAILS",
                    "UPDATE_BUSINESS_ON_SIGNUP", "UPDATE_COMPANY_DETAILS", "ADMIN_SEND_EMAILS_NOTIFICATIONS",
                    "ADMIN_UPLOAD_EMAILS_HEADER_IMAGE", "DELETE-ORGANIZATION-NOTIFICATIONS",
                    "ADMIN_SET_UP_USER_CONFIG", "GET_ORGANIZATION_BY_USERID", "GET-ORGANIZATION-NOTIFICATIONS", "SEND_NOTIFICATIONS",
                    "ADMIN_DELETE_ACCOUNT_TRANSACTION", "GET-DAILY-REPORTS", "GET-GRAPH-REPORTS", "SUBMIT_DISPUTE",
                    "CHECK_DISPUTE_STATUS", "ADMIN_ATTAIN_TO_DISPUTE", "ADMIN_GET_DISPUTES"));


            rolePermissionsMap.put("ROLE_ORGANIZATION_STAFF", Arrays.asList("CREATE_USER", "UPDATE_USER", "READ_USER"));

            for (Map.Entry<String, List<String>> entry : rolePermissionsMap.entrySet()) {

                List<PermissionEntity> permissions = entry.getValue().stream()
                        .map(permissionName -> permissionService.createPermissionIfNotFound(permissionName, "Description for " + permissionName))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());

                createRoleAndAssignPermissions(entry.getKey(), permissions);
            }
            return rolePermissionsMap;  // Return the populated map
        } catch (Exception e) {
            // Log the exception or handle it as needed
            log.error("Error occurred: {}", e.getMessage());
            return Collections.emptyMap();  // Return an empty map or handle the error case
        }
    }


    public RoleEntity createRoleAndAssignPermissions(String roleName, List<PermissionEntity> permissions) {
        Optional<RoleEntity> result = Optional.empty();
        try {
            // Check if the role already exists
            RoleEntity role = roleRepository.findByRoleName(roleName).orElse(null);

            // If the role doesn't exist, create it
            if (role == null) {
                log.info("role is null creating new role");
                role = RoleEntity.builder().roleName(roleName).build();
                role = roleRepository.save(role);
                log.info("done saving role. Rolename: {}", role.getRoleName());
            }
            log.info("printing role name: {}", role.getRoleName());
            // Assign permissions to role that didn't have permission yet
            if (permissions != null && !permissions.isEmpty()) {
                // Initialize the permissions set if it's null
                log.info("permissions is not null nor empty");
                if (role.getPermissions() == null) {
                    role.setPermissions(new HashSet<>());
                }


                // Filter out existing permissions ensuring there is no duplicate

                RoleEntity finalRole = role;
                List<PermissionEntity> newPermissions = permissions.stream()
                        .filter(permission -> !finalRole.getPermissions().contains(permission))
                        .toList();

                // Add only new permissions
                if (!newPermissions.isEmpty()) {
                    role.getPermissions().addAll(newPermissions);
                    role = roleRepository.save(role);
                }

            }
            log.info("returning role. {}", role.getRoleName());
            return role;
        } catch (Exception e) {
            // log the exception
            e.printStackTrace();
            throw new InternalServerException("Error ocurred while trying to create roles and assign permissions");

        }


    }


}


