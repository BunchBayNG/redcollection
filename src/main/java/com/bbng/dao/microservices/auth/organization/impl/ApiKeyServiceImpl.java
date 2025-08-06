package com.bbng.dao.microservices.auth.organization.impl;


import com.bbng.dao.microservices.auth.auditlog.Events;
import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogRequestDto;
import com.bbng.dao.microservices.auth.auditlog.service.AuditLogService;
import com.bbng.dao.microservices.auth.organization.dto.request.CreateLiveApiKeyRequestDto;
import com.bbng.dao.microservices.auth.organization.dto.request.CreateTestApiKeyRequestDto;
import com.bbng.dao.microservices.auth.organization.entity.ApiKeyEntity;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.APIKeyRepository;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.microservices.auth.passport.service.ApiKeyService;
import com.bbng.dao.microservices.auth.passport.utils.SecurityConstants;
import com.bbng.dao.util.exceptions.customExceptions.ForbiddenException;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.exceptions.customExceptions.UserNotFoundException;
import com.bbng.dao.util.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {
    private final UserRepository userRepo;
    private final APIKeyRepository apiKeyRepository;
    private final OrganizationRepository orgRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    @Override
    public ResponseDto<String> createTestKey(CreateTestApiKeyRequestDto testApiKeyRequestDto, String username) {
        // we want to check that his username is correct and his password is correct
       var user = userRepo.findByEmail(testApiKeyRequestDto.getEmail()).orElseThrow(() ->
                new UserNotFoundException("No User Found with the provided email: "+ testApiKeyRequestDto.getEmail()));

       if(!username.equals(user.getUserName())){
           throw new ForbiddenException("You can only generate api keys for yourself");
       }
       if(!passwordEncoder.matches(testApiKeyRequestDto.getPassword(), user.getPassword())){
           throw new BadCredentialsException("Password is incorrect!!!: "+ testApiKeyRequestDto.getPassword());
       }

        OrganizationEntity org = orgRepo.findOrganizationByMerchantAdminId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Can't find the organization By the merchantAdmin Id. " +
                "make sure its the admin of this organization has this email"));

       //retrieve all the api keys that belong to this user id and delete it
      apiKeyRepository.deleteAll(apiKeyRepository.findByUserId(user.getId()));
        //if correct, we will create a new test key with his username, password, and today'sDate
        String testKey;
        ApiKeyEntity apiKey = null;
        try {
             testKey = generateHmacApiKey(testApiKeyRequestDto.getEmail(), testApiKeyRequestDto.getPassword(), "");

            //store in the key table
            apiKey = apiKeyRepository.save(ApiKeyEntity.builder()
                    .testKey( "TEST-"+testKey)
                            .userId(user.getId())
                    .dateCreated(LocalDate.now().toString()).build());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
           log.error("Error generating apiTestKey", e);
        }
        assert apiKey != null;
        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .event(Events.CREATED_TEST_KEY.name())
                .isDeleted(false)
                .description("Created a new test api key")
                .merchantName(org.getOrganizationName())
                .merchantId(org.getId())
                .build());
        return ResponseDto.<String>builder()
                .statusCode(201)
                .status(true)
                .message("Test Api Key Created for the user: "+ user.getUserName() +"!!!")
                .data(apiKey.getTestKey() )
                .build();

    }



    @Override
    public ResponseDto<String> createLiveKey(CreateLiveApiKeyRequestDto createLiveApiKey, String username) {
        // we want to check that his username is correct and his password is correct
        UserEntity user = userRepo.findByEmail(createLiveApiKey.getEmail()).orElseThrow(() ->
                new UserNotFoundException("No User Found with the provided email: "+ createLiveApiKey.getEmail()));

        if(!username.equals(user.getUserName())){
            throw new ForbiddenException("You can only generate ApiKeys for yourself");
        }
        if(!passwordEncoder.matches(createLiveApiKey.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Password is incorrect!!!: "+ createLiveApiKey.getPassword());
        }
        OrganizationEntity org = orgRepo.findOrganizationByMerchantAdminId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Can't find the organization By the merchantAdmin Id. " +
                "make sure its the admin of this organization has this email"));
        boolean exist = orgRepo.existsByOrganizationName((createLiveApiKey.getBusinessName()));
        if (!(exist && createLiveApiKey.getBusinessName().equals(org.getOrganizationName()))){
            throw new ResourceNotFoundException("No Business exists with the given business Name: "+ createLiveApiKey.getBusinessName());
        }
        //retrieve all the api keys that belong to this user id and delete it
        apiKeyRepository.deleteAll(apiKeyRepository.findByUserId(user.getId()));
        String liveKey;
        ApiKeyEntity apiKey = null;
        try {
            liveKey = generateHmacApiKey(createLiveApiKey.getEmail(), createLiveApiKey.getPassword(), createLiveApiKey.getBusinessName());

            //store in the key table
          apiKey =  apiKeyRepository.save(ApiKeyEntity.builder()
                    .liveKey("LIVE-"+liveKey)
                    .userId(user.getId())
                    .dateCreated(LocalDate.now().toString()).build());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Error generating apiLiveKey", e);
        }
        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .event(Events.CREATED_LIVE_KEY.name())
                .isDeleted(false)
                .description("Created a new live api key")
                .merchantName(org.getOrganizationName())
                .merchantId(org.getId())
                .build());

        assert apiKey != null;
        return ResponseDto.<String>builder()
                .statusCode(201)
                .status(true)
                .message("Live Api Key Created for the user: "+ user.getUserName() +"!!!")
                .data(apiKey.getLiveKey())
                .build();

    }



    private String generateHmacApiKey(String userName, String password, String businessName) throws NoSuchAlgorithmException, InvalidKeyException {
        //get local date

        LocalDate today = LocalDate.now();
        String date = today.toString(); //format date as string

        //combine username, password and date;
        String dataToHash = userName + password + businessName + date;

        // Create an HMAC-SHA256 instance with the secret key
        String HMAC_SHA256_ALGORITHM = "HmacSHA256";
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        SecretKeySpec secretKeySpec =  new SecretKeySpec(SecurityConstants.JWT_SECRET.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);

        mac.init(secretKeySpec);

        byte[] hmacBytes = mac.doFinal(dataToHash.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
