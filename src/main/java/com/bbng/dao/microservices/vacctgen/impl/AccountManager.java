package com.bbng.dao.microservices.vacctgen.impl;


import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.APIKeyRepository;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.organization.utils.GetUserFromToken;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.microservices.vacctgen.config.ApplicationProperties;
import com.bbng.dao.microservices.vacctgen.config.MerchantSearchFilter;
import com.bbng.dao.microservices.vacctgen.config.SearchFilter;
import com.bbng.dao.microservices.vacctgen.dto.request.ActivationOperation;
import com.bbng.dao.microservices.vacctgen.dto.request.MerchantStatusRequest;
import com.bbng.dao.microservices.vacctgen.dto.request.ProvisionRequest;
import com.bbng.dao.microservices.vacctgen.dto.request.StatusRequest;
import com.bbng.dao.microservices.vacctgen.dto.response.ConfirmLookupResult;
import com.bbng.dao.microservices.vacctgen.dto.response.LookUpResult;
import com.bbng.dao.microservices.vacctgen.dto.response.ProvisionResult;
import com.bbng.dao.microservices.vacctgen.entity.Account;
import com.bbng.dao.microservices.vacctgen.entity.AccountMetadata;
import com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount;
import com.bbng.dao.microservices.vacctgen.repository.AccountMetadataRepository;
import com.bbng.dao.microservices.vacctgen.repository.AccountRepository;
import com.bbng.dao.microservices.vacctgen.repository.ProvisionedAccountRepository;
import com.bbng.dao.microservices.vacctgen.value.GenerateValue;
import com.bbng.dao.microservices.vacctgen.value.MerchantProvisionValue;
import com.bbng.dao.util.exceptions.customExceptions.AccountException;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.gen.GeneratorUtil;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

import static com.bbng.dao.microservices.vacctgen.entity.Account.Status.FREE;
import static com.bbng.dao.microservices.vacctgen.entity.Account.Status.PROVISIONED;
import static com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount.Status.ACTIVE;
import static com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount.Status.INACTIVE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;



@Component
@Service
public class AccountManager {
    private static final String PREFIX = "99";
    private static final Pattern fourIdenticalPattern = Pattern.compile("(\\d)\\1\\1\\1");

    private static final Logger log = LoggerFactory.getLogger(AccountManager.class);

    private final ProvisionedAccountRepository provisionedAccountRepository;
    private final AccountRepository accountRepository;
    private final AccountMetadataRepository accountMetadataRepository;
    private final ApplicationProperties properties;
    private final Random secureRandom;
    private final HttpServletRequest httpRequest;
    private final UserRepository userRepository;
    private final APIKeyRepository apiKeyRepository;
    private final OrganizationRepository organizationRepository;


    public AccountManager(
            ProvisionedAccountRepository provisionedAccountRepository,
            AccountRepository accountRepository,
            AccountMetadataRepository accountMetadataRepository,
            ApplicationProperties properties, HttpServletRequest httpRequest, UserRepository userRepository, APIKeyRepository apiKeyRepository,
            OrganizationRepository organizationRepository) {
        this.provisionedAccountRepository = provisionedAccountRepository;
        this.accountRepository = accountRepository;
        this.accountMetadataRepository = accountMetadataRepository;
        this.properties = properties;
        this.httpRequest = httpRequest;
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
        secureRandom = new SecureRandom(Instant.now().toString().getBytes(StandardCharsets.UTF_8));
        this.organizationRepository = organizationRepository;
    }

    /**
     * Generate virtual account numbers from given prefix. The algorithm will mark numbers with
     * four identical sequential digits as invalid.
     *
     * @param generateValue
     */
    @Transactional
    public int updatePool(GenerateValue generateValue) {
        log.debug(
                "Initializing virtual account generation with details: {}",
                generateValue.toString()
        );
        log.info("Generating virtual account with prefix {}", generateValue.getPrefix());

        // TODO: validate generate request values
        final Integer size = generateValue.getSize() == null
                ? properties.getMaxPoolSize() : generateValue.getSize();

        if (size > properties.getMaxPoolSize()) {
            throw new AccountException(format(
                    "%d is too large. You can only generate at most %d new accounts.",
                    size, properties.getMaxPoolSize()));
        }
        final Date createdDate = new Date();
        int collisions = 0;
        int suffixLength = 10 - generateValue.getPrefix().length();
        int generated = 0;

        while (generated < size) {
            final int suffix = secureRandom.nextInt(getBound(10 - generateValue.getPrefix().length()));
            String paddedSuffix = GeneratorUtil.ensureLength(Integer.toString(suffix), suffixLength, '0');
            final String formedVirtualAccount = generateValue.getPrefix() + paddedSuffix;
            if (!fourIdenticalPattern.matcher(formedVirtualAccount).find()) {
                Account va = new Account(
                        null, formedVirtualAccount, createdDate, FREE);
                try {
                    accountRepository.save(va);
                } catch (DataIntegrityViolationException e) {
                    collisions++;
                    log.debug("Collision recorded. {} already exists", formedVirtualAccount);
                }
                generated++;
            }
        }

        final AccountMetadata metadata = new AccountMetadata(
                generateValue.getPrefix(), createdDate, generated, collisions);
        accountMetadataRepository.save(metadata);
        log.info(
                "Virtual account generation report for prefix {}: Generated: {}, Collisions: {}, " +
                        "on: {}",
                generateValue.getPrefix(), generated, collisions, createdDate
        );
        return generated;
    }

    private int getBound(int suffixLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < suffixLength; i++) {
            sb.append('9');
        }
        return Integer.parseInt(sb.toString()) + 1;
    }

//    public List<ProvisionedAccount> search(AssignedAccountFilter filter) {
//        final ProvisionedAccount probe = ProvisionedAccount.builder()
//                .status(filter.getStatus())
//                .build();
//
//        return provisionedAccountRepository.findAll(Example.of(probe));
//    }

//    private BooleanExpression dateFilterHelper(
//            final String startDate, final String endDate,
//            BiFunction<Date, Date, BooleanExpression> dateFilterFunction
//    ) {
//        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//
//        Date startDate2 = null;
//        Date endDate2 = null;
//        try {
//            startDate2 = startDate == null ? null : sdf.parse(startDate.trim() + " 00:00:00");
//            endDate2 = endDate == null ? null : sdf.parse(endDate.trim() + " 23:59:59");
//        } catch (ParseException e) {
//            // Do nothing
//        }
//        return dateFilterFunction.apply(startDate2, endDate2);
//    }

//    public void groupOperation(GroupRequest request, GroupRequest.Action action) {
//        switch (action) {
//            case GROUP:
//                final String batchRef = request.getBatchRef();
//                if (batchRef != null) {
//                    provisionedAccountRepository.countBatchGroupable(request.getPartnerId(),
//                            request.getBatchRef());
//                }
//        }
//    }

    public int activationOperation(ActivationOperation request, ActivationOperation.Operation operation) {
        switch (operation) {
            case ACTIVATE:
                // Activate by BatchRef first
//                if (assignedAccountRepository.exi(
//                        request.getBatchRef(), request.getPartnerId())) {


                return provisionedAccountRepository.activateByBatchRef(
                        request.getBatchRef(),
                        request.getPartnerId(),
                        ACTIVE
                );
//                }
            // Activate by list
//                if (request.getAccountNumbers() != null
//                        && request.getAccountNumbers().size() > 0
//                        && assignedAccountRepository.existsAllByAccountNoIn(
//                                request.getAccountNumbers())
//                ) {
//                    return assignedAccountRepository.activateByAccountNumbers(
//                            request.getAccountNumbers(),
//                            request.getPartnerId(),
//                            ProvisionedAccount.Status.ACTIVE
//                    );
//                    return 0;
//                }
            // Activate single account
//                if (request.getAccountNumber() != null
//                        && assignedAccountRepository.existsAllByAccountNo(request.getAccountNumber())
//                ) {
//                    return assignedAccountRepository.activateByAccountNumber(
//                            request.getAccountNumber(),
//                            request.getPartnerId(),
//                            ProvisionedAccount.Status.ACTIVE
//                    );
//                }
//                throw new VirtualAccountException("Request is invalid");
            case DEACTIVATE:
                // Deactivate by BatchRef first
//                if (assignedAccountRepository.exi(
//                        request.getBatchRef(), request.getPartnerId())) {

                return provisionedAccountRepository.activateByBatchRef(
                        request.getBatchRef(), request.getPartnerId(), INACTIVE
                );
//                }
            // Deactivate by list
//                if (request.getAccountNumbers() != null
//                        && request.getAccountNumbers().size() > 0
//                        && assignedAccountRepository.existsAllByAccountNoIn(
//                        request.getAccountNumbers())
//                ) {
//                    return assignedAccountRepository.activateByAccountNumbers(
//                            request.getAccountNumbers(), request.getPartnerId(), INACTIVE
//                    );
//                }
//                // Deactivate single account
//                if (request.getAccountNumber() != null
//                        && assignedAccountRepository.existsAllByAccountNo(request.getAccountNumber())
//                ) {
//                    return assignedAccountRepository.activateByAccountNumber(
//                            request.getAccountNumber(), request.getPartnerId(), INACTIVE
//                    );
//                }
//                throw new VirtualAccountException("Request is invalid");
            default:
                throw new AccountException("Request is invalid");
        }
    }


    @Transactional
    public ResponseDto<ProvisionResult> provisionSingle( ProvisionRequest request) {
        Account selectedAccount = accountRepository.findFirstByStatus(FREE).orElseGet(() -> {
            // populate pool and try again, throw error otherwise
            // The pool will be increased by
            updatePool(new GenerateValue(
                    properties.getDefaultPrefix(), properties.getProvisionSingleUpdatePoolSize()));
            return accountRepository.findFirstByStatus(FREE).orElseThrow(() ->
                    new AccountException(
                            "Attempt to provision account failed. Please retry after some time."));
        });
        Date now = new Date();

        OrganizationEntity org = getOrgForApiKey(httpRequest);


        ProvisionedAccount accountToProvision = new ProvisionedAccount(
                null,
                selectedAccount.getValue(),
                org.getId(),
                org.getOrganizationName(),
                request.getAccountName(),
                request.getAccountMsisdn(),
                request.getAccountEmail(),
                request.getInitiatorRef(),
                BigDecimal.ZERO,
                null,
                now,
                ACTIVE,
                ProvisionedAccount.Mode.OPEN
        );
        accountRepository.markAccountAsProvisioned(selectedAccount.getId(), PROVISIONED);
        ProvisionedAccount pa = provisionedAccountRepository.save(accountToProvision);
        ProvisionResult result =  new ProvisionResult(
                pa.getAccountNo(),
                pa.getAccountName(),
                pa.getAccountEmail(),
                pa.getInitiatorRef());

        return ResponseDto.<ProvisionResult>builder()
                .statusCode(200)
                .status(true)
                .message("Provisioned Account fetched successfully")
                .data(result)
                .build();



    }

    @Transactional
    public ResponseDto<List<ProvisionResult>> provisionMultiple( List<ProvisionRequest> request) {
        List<Account> accounts = accountRepository.findAllByStatus(FREE, PageRequest.of(0, request.size()));

        // TODO We can use count to check if we have enough accounts
        if (accounts.size() != request.size()) {
            updatePool(new GenerateValue(properties.getDefaultPrefix(), request.size()));
            accounts = accountRepository.findAllByStatus(FREE, PageRequest.of(0, request.size()));
        }
        Date now = new Date();
        ArrayList<ProvisionedAccount> provisionedAccounts = new ArrayList<>(request.size());
        OrganizationEntity org = getOrgForApiKey(httpRequest);

        for (int i = 0; i < request.size(); i++) {
            provisionedAccounts.add(i,
                    new ProvisionedAccount(null,
                            accounts.get(i).getValue(),
                            org.getId(),
                            org.getOrganizationName(),
                            request.get(i).getAccountName(),
                            request.get(i).getAccountMsisdn(),
                            request.get(i).getAccountEmail(),
                            request.get(i).getInitiatorRef(),
                            BigDecimal.ZERO,
                            null,
                            now,
                            ACTIVE,
                            ProvisionedAccount.Mode.OPEN)
            );
        }
        accountRepository.markAllAsProvisioned(accounts, PROVISIONED);
        List<ProvisionedAccount> paWithUpdatedStatus
                = provisionedAccountRepository.saveAll(provisionedAccounts);
        List<ProvisionResult> response =  paWithUpdatedStatus.stream().map(pa -> new ProvisionResult(pa.getAccountNo(),
                pa.getAccountName(),
                pa.getAccountEmail(),
                pa.getInitiatorRef())).collect(toList());

        return ResponseDto.<List<ProvisionResult>>builder()
                .statusCode(200)
                .status(true)
                .message("Provisioned Multiple Account fetched successfully")
                .data(response)
                .build();

    }


    @Transactional
    public ResponseDto<LookUpResult> doLookup(String accountNo) {

//        ProvisionedAccount account = provisionedAccountRepository.findByAccountNo(accountNo)
//                .orElseThrow(() -> new AccountException("Account number does not exist"));
        ProvisionedAccount account = provisionedAccountRepository.findByAccountNoAndStatus(accountNo, ACTIVE)
                .orElseThrow(() -> new AccountException("Account number does not exist"));

        if (account.getStatus().compareTo(ACTIVE) != 0) {
            throw new AccountException("Account number is not currently active");
        }
        LookUpResult result =  new LookUpResult(account.getAccountNo(), account.getAccountName(), account.getClientId(), account.getWalletNo());


        return ResponseDto.<LookUpResult>builder()
                .statusCode(200)
                .status(true)
                .message("Result looked up successfully")
                .data(result)
                .build();

    }

    @Transactional
    public ResponseDto<ConfirmLookupResult>  doConfirmLookup(String accountNo, String amount) {
        boolean status = false;
//        ProvisionedAccount account = provisionedAccountRepository.findByAccountNo(accountNo)
//                .orElseThrow(() -> new AccountException("Account number does not exist"));
        ProvisionedAccount account = provisionedAccountRepository.findByAccountNoAndStatus(accountNo, ACTIVE)
                .orElseThrow(() -> new AccountException("Account number does not exist"));

        if (!account.getStatus().equals(ACTIVE)) {
            throw new AccountException("Account number is not currently active");
        }
        if (account.getAmount().compareTo(new BigDecimal(amount)) == 0
                && account.getMode() == ProvisionedAccount.Mode.CLOSED) {
            status = true;
        }
        if (account.getMode() == ProvisionedAccount.Mode.OPEN) {
            status = true;
        }
        ConfirmLookupResult result = new ConfirmLookupResult(account.getAccountNo(), account.getAccountName(), account.getWalletNo(), status);

        return ResponseDto.<ConfirmLookupResult>builder()
                .statusCode(200)
                .status(true)
                .message("Result looked up confirmed successfully")
                .data(result)
                .build();

    }

    @Transactional
    public  ResponseDto<ConfirmLookupResult>  doConfirmLookupAllStatus(String accountNo, String amount) {
        boolean status = false;
//        ProvisionedAccount account = provisionedAccountRepository.findByAccountNo(accountNo)
//                .orElseThrow(() -> new AccountException("Account number does not exist"));
        ProvisionedAccount account = provisionedAccountRepository.findByAccountNo(accountNo)
                .orElseThrow(() -> new AccountException("Account number does not exist"));
        //I hope you know what you are doing
        if (account.getAmount().compareTo(new BigDecimal(amount)) == 0
                && account.getMode() == ProvisionedAccount.Mode.CLOSED) {
            status = true;
        }
        if (account.getMode() == ProvisionedAccount.Mode.OPEN) {
            status = true;
        }
        ConfirmLookupResult result = new ConfirmLookupResult(account.getAccountNo(), account.getAccountName(), account.getWalletNo(), status);

        return ResponseDto.<ConfirmLookupResult>builder()
                .statusCode(200)
                .status(true)
                .message("Result looked up confirmed successfully")
                .data(result)
                .build();
    }

    @Transactional
    public ResponseDto<ProvisionedAccount> provisionForMerchant(MerchantProvisionValue request) {
        Account selectedAccount = accountRepository.findFirstByStatus(FREE).orElseGet(() -> {
            // populate pool and try again, throw error otherwise
            // The pool will be increased by TODO Complete comment
            updatePool(new GenerateValue(
                    properties.getDefaultPrefix(), properties.getProvisionSingleUpdatePoolSize()));
            return accountRepository.findFirstByStatus(FREE).orElseThrow(() ->
                    new AccountException(
                            "Attempt to provision account failed. Please retry after some time."));
        });
        
        OrganizationEntity org = getOrgForApiKey(httpRequest);
        
        ProvisionedAccount accountToProvision = ProvisionedAccount.builder()
                .accountNo(selectedAccount.getValue())
                .accountName(request.getAccountName())
                .status(ACTIVE)
                .clientId(org.getId())
                .clientName(org.getOrganizationName())
                .initiatorRef(request.getInitiatorRef())
                .amount(request.getAmount())
                .walletNo(request.getWalletNo())
                .mode(ProvisionedAccount.Mode.CLOSED)
                .provisionDate(new Date())
                .build();

        accountRepository.markAccountAsProvisioned(selectedAccount.getId(), PROVISIONED);
        ProvisionedAccount provisionedAccount = provisionedAccountRepository.save(accountToProvision);


        return ResponseDto.<ProvisionedAccount>builder()
                .statusCode(200)
                .status(true)
                .message("Provisioned Account fetched successfully")
                .data(provisionedAccount)
                .build();
    }

    @Transactional
    public ResponseDto<ProvisionedAccount> provisionStaticForMerchant(MerchantProvisionValue request) {
        //find provisioned account tied to wallet in request, if not provision a new account
        Optional<ProvisionedAccount> existingAccount = provisionedAccountRepository.findByWalletNoAndMode(request.getWalletNo(), ProvisionedAccount.Mode.OPEN);
        if (existingAccount.isPresent()){

            return ResponseDto.<ProvisionedAccount>builder()
                    .statusCode(200)
                    .status(true)
                    .message("Provisioned Account fetched successfully")
                    .data(existingAccount.get())
                    .build();
        }
        Account selectedAccount = accountRepository.findFirstByStatus(FREE).orElseGet(() -> {
            // populate pool and try again, throw error otherwise
            // The pool will be increased by TODO Complete comment
            updatePool(new GenerateValue(
                    properties.getDefaultPrefix(), properties.getProvisionSingleUpdatePoolSize()));
            return accountRepository.findFirstByStatus(FREE).orElseThrow(() ->
                    new AccountException(
                            "Attempt to provision account failed. Please retry after some time."));
        });


        OrganizationEntity org = getOrgForApiKey(httpRequest);
        ProvisionedAccount accountToProvision = ProvisionedAccount.builder()
                .accountNo(selectedAccount.getValue())
                .accountName(request.getAccountName())
                .status(ACTIVE)
                .clientId(org.getId())
                .clientName(org.getOrganizationName())
                .initiatorRef(request.getInitiatorRef())
                .amount(request.getAmount())
                .walletNo(request.getWalletNo())
                .mode(ProvisionedAccount.Mode.OPEN)
                .provisionDate(new Date())
                .build();

        accountRepository.markAccountAsProvisioned(selectedAccount.getId(), PROVISIONED);
        ProvisionedAccount provisionedAccount = provisionedAccountRepository.save(accountToProvision);

        return ResponseDto.<ProvisionedAccount>builder()
                .statusCode(200)
                .status(true)
                .message("Provisioned Account fetched successfully")
                .data(provisionedAccount)
                .build();
    }

    public Optional<ProvisionedAccount> findAccount(String accountNo) {
        return provisionedAccountRepository.findByAccountNo(accountNo);
    }

    public Optional<ProvisionedAccount> findAccountByNoAndStatus(String accountNo, ProvisionedAccount.Status status){
        return provisionedAccountRepository.findByAccountNoAndStatus(accountNo, status);
    }

    @Transactional(readOnly = true)
    public ResponseDto<ProvisionedAccount>  getProvisionedAccountStatus(StatusRequest statusRequest) {

        String walletNo = null;
        String initiatorRef = null;
        if (statusRequest instanceof MerchantStatusRequest) {
            walletNo = ((MerchantStatusRequest) statusRequest).getWalletNo();
            initiatorRef = ((MerchantStatusRequest) statusRequest).getInitiatorRef();
        }

        OrganizationEntity org = getOrgForApiKey(httpRequest);
        
        ProvisionedAccount account = ProvisionedAccount.builder()
                .accountNo(statusRequest.getAccountNo())
                .clientId(org.getId())
                .initiatorRef(initiatorRef)
                .walletNo(walletNo)
                .build();

        ProvisionedAccount provisionedAccount =  provisionedAccountRepository.findOne(Example.of(account))
                .orElseThrow(() -> new AccountException("No account matches the supplied details"));

        return ResponseDto.<ProvisionedAccount>builder()
                .statusCode(200)
                .status(true)
                .message("Provisioned Account fetched successfully")
                .data(provisionedAccount)
                .build();

    }

    public ResponseDto<List<ProvisionedAccount>> search(SearchFilter filter) {
        String walletNo = filter instanceof MerchantSearchFilter
                ? ((MerchantSearchFilter) filter).getWalletNo() : null;

        OrganizationEntity org = getOrgForApiKey(httpRequest);
        
        ProvisionedAccount account = ProvisionedAccount.builder()
                .clientId(org.getId())
                .accountNo(filter.getAccountNo())
                .walletNo(walletNo)
                .initiatorRef(filter.getInitiatorRef())
                .accountName(filter.getAccountName())
                .build();

        ExampleMatcher accountMatcher = ExampleMatcher.matching()
                .withMatcher("accountName", matcher -> matcher.ignoreCase(true).exact());

        Example<ProvisionedAccount> accountExample = Example.of(account, accountMatcher);

        List<ProvisionedAccount>  list =  provisionedAccountRepository.findAll((root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (filter.getFromDate() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("provisionDate"), filter.getFromDate()));
            }
            if (filter.getToDate() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("provisionDate"), filter.getToDate()));
            }
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, accountExample));

            return builder.and(predicates.toArray(new Predicate[0]));

        });

        return ResponseDto.<List<ProvisionedAccount>>builder()
                    .statusCode(200)
                    .status(true)
                    .message("Search result fetched successfully")
                    .data(list)
                    .build();
    }

    public void updateProvisionedAccountStatus(ProvisionedAccount provisionedAccount, ProvisionedAccount.Status status) {
        if (provisionedAccount.getMode().equals(ProvisionedAccount.Mode.CLOSED)) {
            provisionedAccount.setStatus(status);
            provisionedAccountRepository.save(provisionedAccount);
            log.info("account is set to inactive - {}", provisionedAccount);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 23 * * ?")
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void returnInactiveAccountToPool(){
        log.info("scheduler running at every 5 min to return inactive account to pool");
        try {
            int isUpdated = provisionedAccountRepository.updateAccountStatusByMode(INACTIVE, ProvisionedAccount.Mode.CLOSED);
            log.info("update accounts to pool --- status - {} ", isUpdated);
        }catch (Exception e){
            log.info("unable to update accounts to pool --- "+e.getMessage());
        }
        //return account to pool, and flag as FREE
        try{
            int isUpdated = accountRepository.updateAccountStatusByProvisionedAccountModeAndStatus(FREE, INACTIVE, ProvisionedAccount.Mode.CLOSED);
            log.info("update accounts to FREE --- status - {} ", isUpdated);
        }catch (Exception e){
            log.info("unable to update account to FREE in pool --- "+e.getMessage());
        }
    }

    @Transactional
    public ResponseDto<ProvisionedAccount> provisionStaticForReseller(MerchantProvisionValue request) {
        //find provisioned account tied to wallet in request, if not provision a new account
//        Optional<ProvisionedAccount> existingAccount = provisionedAccountRepository.findByWalletNoAndMode(request.getWalletNo(), ProvisionedAccount.Mode.OPEN);
//        if (existingAccount.isPresent()){
//            return existingAccount.get();
//        }
        Account selectedAccount = accountRepository.findFirstByStatus(FREE).orElseGet(() -> {
            // populate pool and try again, throw error otherwise
            // The pool will be increased by TODO Complete comment
            updatePool(new GenerateValue(
                    properties.getDefaultPrefix(), properties.getProvisionSingleUpdatePoolSize()));
            return accountRepository.findFirstByStatus(FREE).orElseThrow(() ->
                    new AccountException(
                            "Attempt to provision account failed. Please retry after some time."));
        });

        OrganizationEntity org = getOrgForApiKey(httpRequest);
        
        ProvisionedAccount accountToProvision = ProvisionedAccount.builder()
                .accountNo(selectedAccount.getValue())
                .accountName(request.getAccountName())
                .status(ACTIVE)
                .clientId(org.getId())
                .clientName(org.getOrganizationName())
                .initiatorRef(request.getInitiatorRef())
                .amount(request.getAmount())
                .walletNo(request.getWalletNo())
                .mode(ProvisionedAccount.Mode.OPEN)
                .provisionDate(new Date())
                .build();

        accountRepository.markAccountAsProvisioned(selectedAccount.getId(), PROVISIONED);
        ProvisionedAccount provisionedAccount = provisionedAccountRepository.save(accountToProvision);

        return ResponseDto.<ProvisionedAccount>builder()
                .statusCode(200)
                .status(true)
                .message("Provisioned Account fetched successfully")
                .data(provisionedAccount)
                .build();
    }

    @Transactional
    public  ResponseDto<ProvisionedAccount>  provisionStaticForResellerUniqueInitiatorRef(MerchantProvisionValue request) {
        //find provisioned account tied to initiator ref in request, if not provision a new account
        Optional<ProvisionedAccount> existingAccount = provisionedAccountRepository.findByInitiatorRefAndMode(request.getInitiatorRef(), ProvisionedAccount.Mode.OPEN);
        if (existingAccount.isPresent()){
            return ResponseDto.<ProvisionedAccount>builder()
                    .statusCode(200)
                    .status(true)
                    .message("Provisioned Account fetched successfully")
                    .data(existingAccount.get())
                    .build();
        }
        Account selectedAccount = accountRepository.findFirstByStatus(FREE).orElseGet(() -> {
            // populate pool and try again, throw error otherwise
            // The pool will be increased by TODO Complete comment
            updatePool(new GenerateValue(
                    properties.getDefaultPrefix(), properties.getProvisionSingleUpdatePoolSize()));
            return accountRepository.findFirstByStatus(FREE).orElseThrow(() ->
                    new AccountException(
                            "Attempt to provision account failed. Please retry after some time."));
        });

        OrganizationEntity org = getOrgForApiKey(httpRequest);

        ProvisionedAccount accountToProvision = ProvisionedAccount.builder()
                .accountNo(selectedAccount.getValue())
                .accountName(request.getAccountName())
                .status(ACTIVE)
                .clientId(org.getId())
                .clientName(org.getOrganizationName())
                .initiatorRef(request.getInitiatorRef())
                .amount(request.getAmount())
                .walletNo(request.getWalletNo())
                .mode(ProvisionedAccount.Mode.OPEN)
                .provisionDate(new Date())
                .build();

        accountRepository.markAccountAsProvisioned(selectedAccount.getId(), PROVISIONED);
        ProvisionedAccount provisionedAccount = provisionedAccountRepository.save(accountToProvision);

        return ResponseDto.<ProvisionedAccount>builder()
                .statusCode(200)
                .status(true)
                .message("Provisioned Account fetched successfully")
                .data(provisionedAccount)
                .build();
    }


    public OrganizationEntity getOrgForApiKey(HttpServletRequest request) {


      //  String email = GetUserFromToken.extractUserFromApiKey(request, apiKeyRepository, userRepository);

        return organizationRepository.findByContactEmail("email").orElseThrow(() ->
                new ResourceNotFoundException("Can't find Org with the username extracted from token. Is user a redtech user?"));
    }
}
