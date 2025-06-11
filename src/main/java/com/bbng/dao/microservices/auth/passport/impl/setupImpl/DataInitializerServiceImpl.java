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
                                      UserRepository userRepository, OrganizationRepository organizationRepository ) {
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
                "GET_COMMISSION",
                "CREATE_LIVE_APIKEY",
                "CAN_RE_QUERY",
                "CAN_CHECK_SERVICE_PROVIDER_STATUS",
                "CAN_GET_SERVICE_CATEGORIES",
                "CAN_GENERATE_ORDER_ID",
                "CAN_GET_PRICE_LIST_DATA",
                "CAN_GET_PRICE_LIST_TV",
                "GET_COMMISSION_WALLET_BY_ID",
                "GET_COMMISSION_WALLET_BY_WALLET_ID",
                "GET_COMMISSION_WALLET_TRANSACTIONS",
                "GET_COMMISSION_WALLET_TRANSACTION_BY_ID",
                "CAN_INVITE_STAFF",
                "CAN_GET_STAFF",
                "CAN_DISABLE_STAFF",
                "CAN_ONBOARD_ORG",
                "CAN_REMOVE_PERMISSION",
                "CAN_CREATE_ROLE",
                "CAN_ASSIGN_PERMISSIONS",
                "UPDATE_BUSINESS_ON_SIGNUP",
                "UPDATE_COMPANY_DETAILS",
                "UPDATE_COMPANY_ADDRESS",
                "UPDATE_COMPANY_CONTACTS",
                "UPDATE_COMPANY_IDENTITY",
                "UPDATE_COMPANY_ACCOUNT",
                "GET_ORGANIZATION_BY_USERID",
                "TOP_UP_WALLET",
                "WITHDRAWAL",
                "GET_WALLET_TRANSACTIONS",
                "GET_WALLET_TRANSACTIONS_BY_ID",
                "GET_WALLET",
                "GET-ORGANIZATION-NOTIFICATIONS",
                "DELETE-ORGANIZATION-NOTIFICATIONS",
                "MERCHANT_GET_AUDIT_LOGS",
                "GET-DAILY-REPORTS",
                "GET-GRAPH-REPORTS",
                "SUBMIT_DISPUTE",
                "CHECK_DISPUTE_STATUS",
                "VALIDATE_CUSTOMER_METERNO",
                "CAN_GET_SERVICE_PROVIDERS"
        ));

        List<String> descriptions = new ArrayList<>(Arrays.asList(
                "the \"ADMIN_GET_ACCOUNT_NUMBER\" permission grant the permission to get an account number",
                "the \"CREATE_TEST_APIKEY\" permission grant the permission to create test ApiKey",
                "the \"GET_COMMISSION\" permission grant the permission to Get a merchant Commission",
                "the \"CREATE_LIVE_APIKEY\" permission grant the permission to create live ApiKey",
                "the \"CAN_RE_QUERY\" permission grant the permission to make re-query",
                "the \"CAN_CHECK_SERVICE_PROVIDER_STATUS\" permission grant the permission to check service provider status",
                "the \"CAN_GET_SERVICE_CATEGORIES\" permission grant the permission to get service categories",
                "the \"CAN_GENERATE_ORDER_ID\" permission grant the permission to generate orderId",
                "the \"CAN_GET_PRICE_LIST_DATA\" permission grant the permission to get price list data",
                "the \"CAN_GET_PRICE_LIST_TV\" permission grant the permission to get price list tv",
                "the \"GET_COMMISSION_WALLET_BY_ID\" permission grant the permission to get commissionWallet by id",
                "the \"GET_COMMISSION_WALLET_BY_WALLET_ID\" permission grant the permission to get commissionWallet by walletId",
                "the \"GET_COMMISSION_WALLET_TRANSACTIONS\" permission grant the permission to get commissionWallet transactions",
                "the \"GET_COMMISSION_WALLET_TRANSACTION_BY_ID\" permission grant the permission to get commissionWallet transactions by TransactionId",
                "the \"CAN_INVITE_STAFF\" permission grant the permission to invite staff",
                "the \"CAN_GET_STAFF\" permission grant the permission to get staff",
                "the \"CAN_DISABLE_STAFF\" permission grant the permission to disable staff",
                "the \"CAN_ONBOARD_ORG\" permission grant the permission to onboard merchant",
                "the \"CAN_REMOVE_PERMISSION\" permission grant the permission to remove permission",
                "the \"CAN_CREATE_ROLE\" permission grant the permission to create role",
                "the \"CAN_ASSIGN_PERMISSIONS\" permission grant the permission to assign permissions",
                "the \"UPDATE_BUSINESS_ON_SIGNUP\" permission grant the permission to update business on signUp",
                "the \"UPDATE_COMPANY_DETAILS\" permission grant the permission to update company details",
                "the \"UPDATE_COMPANY_ADDRESS\" permission grant the permission to update company address",
                "the \"UPDATE_COMPANY_CONTACTS\" permission grant the permission to update company contacts",
                "the \"UPDATE_COMPANY_IDENTITY\" permission grant the permission to update company identity",
                "the \"UPDATE_COMPANY_ACCOUNT\" permission grant the permission to update company account",
                "the \"GET_ORGANIZATION_BY_USERID\" permission grant the permission to get organization by userId",
                "the \"TOP_UP_WALLET\" permission grant the permission to top up merchant wallet balance",
                "the \"WITHDRAWAL\" permission grant the permission to withdraw merchant money",
                "the \"GET_WALLET_TRANSACTIONS\" permission grant the permission to get merchant Wallet Transactions",
                "the \"GET_WALLET_TRANSACTIONS_BY_ID\" permission grant the permission to get merchant Wallet Transaction by Id",
                "the \"GET_WALLET\" permission grant the permission to get merchant Wallet",
                "the \"GET-ORGANIZATION-NOTIFICATIONS\" permission grant the permission to get an organization Notifications",
                "the \"DELETE-ORGANIZATION-NOTIFICATIONS\" permission grant the permission to delete an organization Notifications",
                "the \"MERCHANT_GET_AUDIT_LOGS\" permission grant the permission to get merchant audit logs",
                "the \"GET-DAILY-REPORTS\" permission grant the permission to get daily reports",
                "the \"GET-GRAPH-REPORTS\" permission grant the permission to get graph reports",
                "the \"SUBMIT_DISPUTE\" permission grant the permission to submit a dispute",
                "the \"CHECK_DISPUTE_STATUS\" permission grant the permission to check a dispute status",
                "the \"VALIDATE_CUSTOMER_METERNO\" permission grant the permission to validate customer MeterNumber",
                "the \"CAN_GET_SERVICE_PROVIDERS\" permission grant the permission to fetch a service provider by the categoryId"

        ));
        Set<PermissionEntity> permissionEntitySet = permissionService.createListOfPermissionIfNotFound(names, descriptions);
        List<PermissionEntity> permissionEntityList = new ArrayList<>(permissionEntitySet);
        log.info("creating permissions for organization admin");
        assignPermissionsToUserType(permissionEntityList);

    }

    private void assignPermissionsToUserType(List<PermissionEntity> permissionEntityList) {
        List<UserEntity> users =  userRepository.findByOrganizationAdmin();
        users.forEach(user -> {
            user.getRoleEntities().forEach(role -> {
                createRoleAndAssignPermissions(role.getRoleName(), permissionEntityList);
            });
        });


    }



    private void createRedtechAdminPermissions() {
        List<String> names = new ArrayList<>(Arrays.asList(
                "ADMIN_ADD_ACCOUNT_NUMBER",
                "ADMIN_GET_ACCOUNT_NUMBER",
                "ADMIN_UPDATE_ACCOUNT_NUMBER",
                "ADMIN_DELETE_ACCOUNT_NUMBER",
                "ADMIN_VIEW_TOP_UP_TRANSACTION",
                "ADMIN_GET_AUDIT_LOGS",
                "CREATE_TEST_APIKEY",
                "CREATE_LIVE_APIKEY",
                "GET_COMMISSION",
                "CAN_RE_QUERY",
                "CAN_CHECK_SERVICE_PROVIDER_STATUS",
                "CAN_GET_SERVICE_CATEGORIES",
                "CAN_GENERATE_ORDER_ID",
                "CAN_GET_PRICE_LIST_DATA",
                "CAN_GET_PRICE_LIST_TV",
                "GET_COMMISSION_WALLET_BY_ID",
                "GET_COMMISSION_WALLET_BY_WALLET_ID",
                "GET_COMMISSION_WALLET_TRANSACTIONS",
                "GET_COMMISSION_WALLET_TRANSACTION_BY_ID",
                "ADMIN_GET_REDTECH_WALLET",
                "ADMIN_GET_REDTECH_COMMISSION_TRANSACTIONS",
                "ADMIN_ACCEPT_PAYMENT",
                "ADMIN_CONFIRM_PAYMENT",
                "ADMIN_DECLINE_PAYMENT",
                "ADMIN_SET_MERCHANT_GLOBAL_COMMISSION",
                "ADMIN_SET_MERCHANT_SPECIFIC_COMMISSION",
                "ADMIN_ACCEPT_KYC",
                "ADMIN_VERIFY_KYC",
                "ADMIN_REJECT_KYC",
                "ADMIN_GET_MERCHANTS_KYC",
                "ADMIN_GET_KYC_STATS",
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
                "UPDATE_COMPANY_ADDRESS",
                "UPDATE_COMPANY_CONTACTS",
                "UPDATE_COMPANY_IDENTITY",
                "UPDATE_COMPANY_ACCOUNT",
                "GET_ORGANIZATION_BY_USERID",
                "TOP_UP_WALLET",
                "WITHDRAWAL",
                "GET_WALLET_TRANSACTIONS",
                "GET_WALLET_TRANSACTIONS_BY_ID",
                "GET_WALLET",
                "GET-ORGANIZATION-NOTIFICATIONS",
                "SEND_NOTIFICATIONS",
                "DELETE-ORGANIZATION-NOTIFICATIONS",
                "ADMIN_DELETE_ACCOUNT_TRANSACTION",
                "GET-DAILY-REPORTS",
                "GET-GRAPH-REPORTS",
                "GET_MERCHANT_WALLETS",
                "GET_TOTAL_WALLETS_BALANCE",
                "SUSPEND_MERCHANT_WALLET",
                "ACTIVATE_MERCHANT_WALLET",
                "GET_WALLET_STATS",
                "SUBMIT_DISPUTE",
                "CHECK_DISPUTE_STATUS",
                "ADMIN_ATTAIN_TO_DISPUTE",
                "ADMIN_GET_DISPUTES",
                "VALIDATE_CUSTOMER_METERNO", "CAN_GET_SERVICE_PROVIDERS"
        ));

        List<String> descriptions = new ArrayList<>(Arrays.asList(
                "the \"ADMIN_ADD_ACCOUNT_NUMBER\" permission grant the permission to add an account number",
                "the \"ADMIN_GET_ACCOUNT_NUMBER\" permission grant the permission to get an account number",
                "the \"ADMIN_UPDATE_ACCOUNT_NUMBER\" permission grant the permission to update an account number",
                "the \"ADMIN_DELETE_ACCOUNT_NUMBER\" permission grant the permission to delete an account number",
                "the \"ADMIN_VIEW_TOP_UP_TRANSACTION\" permission grant the permission to view top up Transactions",
                "the \"ADMIN_GET_AUDIT_LOGS\" permission grant the permission to get audit logs",
                "the \"CREATE_TEST_APIKEY\" permission grant the permission to create test ApiKey",
                "the \"CREATE_LIVE_APIKEY\" permission grant the permission to create live ApiKey",
                "the \"GET_COMMISSION\" permission grant the permission to Get a merchant Commission",
                "the \"CAN_RE_QUERY\" permission grant the permission to make re-query",
                "the \"CAN_CHECK_SERVICE_PROVIDER_STATUS\" permission grant the permission to check service provider status",
                "the \"CAN_GET_SERVICE_CATEGORIES\" permission grant the permission to get service categories",
                "the \"CAN_GENERATE_ORDER_ID\" permission grant the permission to generate orderId",
                "the \"CAN_GET_PRICE_LIST_DATA\" permission grant the permission to get price list data",
                "the \"CAN_GET_PRICE_LIST_TV\" permission grant the permission to get price list tv",
                "the \"GET_COMMISSION_WALLET_BY_ID\" permission grant the permission to get commissionWallet by id",
                "the \"GET_COMMISSION_WALLET_BY_WALLET_ID\" permission grant the permission to get commissionWallet by walletId",
                "the \"GET_COMMISSION_WALLET_TRANSACTIONS\" permission grant the permission to get commissionWallet transactions",
                "the \"GET_COMMISSION_WALLET_TRANSACTION_BY_ID\" permission grant the permission to get commissionWallet transactions by TransactionId",
                "the \"ADMIN_GET_REDTECH_WALLET\" permission grant the permission to get redtechCommissionWallet",
                "the \"ADMIN_GET_REDTECH_COMMISSION_TRANSACTIONS\" permission grant the permission to get redtechCommissionWallet transactions",
                "the \"ADMIN_ACCEPT_PAYMENT\" permission grant the permission to get accept payment from merchant",
                "the \"ADMIN_CONFIRM_PAYMENT\" permission grant the permission to get confirm payment from merchant",
                "the \"ADMIN_DECLINE_PAYMENT\" permission grant the permission to get decline payment from merchant",
                "the \"ADMIN_SET_MERCHANT_GLOBAL_COMMISSION\" permission grant the permission to set merchant global commission",
                "the \"ADMIN_SET_MERCHANT_SPECIFIC_COMMISSION\" permission grant the permission to set merchant specific commission",
                "the \"ADMIN_ACCEPT_KYC\" permission grant the permission to accept kyc from merchant",
                "the \"ADMIN_VERIFY_KYC\" permission grant the permission to verify kyc from merchant",
                "the \"ADMIN_REJECT_KYC\" permission grant the permission to reject kyc from merchant",
                "the \"ADMIN_GET_MERCHANTS_KYC\" permission grant the permission to fetch all kyc in the system",
                "the \"ADMIN_GET_KYC_STATS\" permission grant the permission to fetch all kyc stats the system",
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
                "the \"UPDATE_COMPANY_ADDRESS\" permission grant the permission to update company address",
                "the \"UPDATE_COMPANY_CONTACTS\" permission grant the permission to update company contacts",
                "the \"UPDATE_COMPANY_IDENTITY\" permission grant the permission to update company identity",
                "the \"UPDATE_COMPANY_ACCOUNT\" permission grant the permission to update company account",
                "the \"GET_ORGANIZATION_BY_USERID\" permission grant the permission to get organization by userId",
                "the \"TOP_UP_WALLET\" permission grant the permission to top up merchant wallet balance",
                "the \"WITHDRAWAL\" permission grant the permission to withdraw merchant money",
                "the \"GET_WALLET_TRANSACTIONS\" permission grant the permission to get merchant Wallet Transactions",
                "the \"GET_WALLET_TRANSACTIONS_BY_ID\" permission grant the permission to get merchant Wallet Transaction by Id",
                "the \"GET_WALLET\" permission grant the permission to get merchant Wallet",
                "the \"GET-ORGANIZATION-NOTIFICATIONS\" permission grant the permission to get an organization Notifications",
                "the \"SEND_NOTIFICATIONS\" permission grant the permission to send out notifications to admins",
                "the \"DELETE-ORGANIZATION-NOTIFICATIONS\" permission grant the permission to delete an organization Notifications",
                "the \"ADMIN_DELETE_ACCOUNT_TRANSACTION\" permission grant the permission to delete account Transactions",
                "the \"GET-DAILY-REPORTS\" permission grant the permission to get daily reports",
                "the \"GET-GRAPH-REPORTS\" permission grant the permission to get graph reports",
                "the \"GET_MERCHANT_WALLETS\" permission grant the permission to get all merchant wallets",
                "the \"GET_TOTAL_WALLETS_BALANCE\" permission grant the permission to get all merchant total wallet balance",
                "the \"SUSPEND_MERCHANT_WALLET\" permission grant the permission to suspend a merchant wallet",
                "the \"ACTIVATE_MERCHANT_WALLET\" permission grant the permission to activate a merchant wallet",
                "the \"GET_WALLET_STATS\" permission grant the permission to get merchants wallet stats",
                "the \"SUBMIT_DISPUTE\" permission grant the permission to submit a dispute",
                "the \"CHECK_DISPUTE_STATUS\" permission grant the permission to check a dispute status",
                "the \"ADMIN_ATTAIN_TO_DISPUTE\" permission grant the permission to Attain to disputes",
                "the \"ADMIN_GET_DISPUTES\" permission grant the permission to get all disputes",
                "the \"VALIDATE_CUSTOMER_METERNO\" permission grant the permission to validate customer MeterNumber",
                "the \"CAN_GET_SERVICE_PROVIDERS\" permission grant the permission to fetch a service provider by the categoryId"

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

            UserEntity savedUser = userRepository.saveAndFlush(user);
            OrganizationEntity organizationEntity = OrganizationEntity.builder()
                    .organizationName(request.getOrganizationName())
                    .merchantAdminId(savedUser.getId())
                    .build();

            OrganizationEntity savedOrg = organizationRepository.save(organizationEntity);
           organizationRepository.save(savedOrg);

        }
        else if (optionalUser.get().getUserName() == null){
           var user = optionalUser.get();
           user.setUserName(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
           userRepository.save(user);
        }
    }


    public Map<String, List<String>>allocatePermissionsToRoles() {
        try {
            // Map role names to associated permissions
            Map<String, List<String>> rolePermissionsMap = new HashMap<>();
            rolePermissionsMap.put("ROLE_SUPER_ADMIN", Arrays.asList("CREATE_USER", "READ_USER", "UPDATE_USER", "DELETE_USER"));
            rolePermissionsMap.put("ROLE_ORGANIZATION_ADMIN", Arrays.asList("CREATE_USER", "READ_USER", "UPDATE_USER"));
            rolePermissionsMap.put("ROLE_REDTECH_STAFF", Arrays.asList("CREATE_USER", "READ_USER", "UPDATE_USER"));
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
               role =  roleRepository.save(role);
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
                if(!newPermissions.isEmpty()){
                    role.getPermissions().addAll(newPermissions);
                    role = roleRepository.save(role);
                }

            }
            log.info("returning role. {}", role.getRoleName());
            return  role;
        } catch (Exception e) {
            // log the exception
            e.printStackTrace();
            throw new InternalServerException("Error ocurred while trying to create roles and assign permissions");

        }


    }




}


