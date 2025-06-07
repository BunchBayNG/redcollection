package com.bbng.dao.microservices.auth.organization.impl;


import com.bbng.dao.microservices.auth.organization.dto.request.*;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.organization.service.OrganizationService;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;

//    private final KycRepository kycRepository;
//    private final KycDocRepository kycDocRepository;
//    private final FileAndImageUploadService fileAndImageUploadService;
//    private final EmailVerificationService emailService;
//    private  final UserRepository userRepository;

    @Override
    public ResponseDto<String> updateBusinessDetails(UpdateOrgDto updateRequestDto) {

        OrganizationEntity organizationEntity = organizationRepository.findById(updateRequestDto.getOrganizationId()).orElseThrow(() -> new ResourceNotFoundException(String.format("Organization with id: %s not found", updateRequestDto.getOrganizationId())));


        organizationEntity.setOrganizationName(updateRequestDto.getOrganizationName());
        organizationEntity.setContactName(updateRequestDto.getContactName());
        organizationEntity.setContactEmail(updateRequestDto.getContactEmail());
        ///organizationEntity.setOrgStatus(updateRequestDto.getStatus().name());
        organizationEntity.setProductPrefix(updateRequestDto.getProductPrefix());

         organizationRepository.save(organizationEntity);

        return ResponseDto.<String>builder()
                .statusCode(200)
                .status(true)
                .message("Organization onboarding successful")
                .data(String.format("Organization with id: %s has been updated.", updateRequestDto.getOrganizationId()))
                .build();

    }

//
//    @Override
//    public ResponseDto<String> updateCompanyDetails(CompanyDetailsRequestDto companyDetailsRequestDto, MultipartFile businessLogoUrl) {
//        OrganizationEntity organizationEntity = organizationRepository.findById(companyDetailsRequestDto.getOrganizationId()).orElseThrow(() -> new ResourceNotFoundException(String.format("Organization with id: %s not found", companyDetailsRequestDto.getOrganizationId())));
//
//        organizationEntity.setBusinessLogoUrl(businessLogoUrl == null ? organizationEntity.getBusinessLogoUrl() : fileAndImageUploadService.uploadFile(businessLogoUrl, "images"));
//        organizationEntity.setOrganizationName(companyDetailsRequestDto.getOrganizationName() == null ? organizationEntity.getOrganizationName()
//                 : companyDetailsRequestDto.getOrganizationName());
//        organizationEntity.setDescription(companyDetailsRequestDto.getDescription() == null ? organizationEntity.getDescription() :
//                companyDetailsRequestDto.getDescription());
//        organizationEntity.setBusinessCategory(String.valueOf(companyDetailsRequestDto.getBusinessCategory() == null ? organizationEntity.getBusinessCategory() :
//                companyDetailsRequestDto.getBusinessCategory()));
//
//        organizationRepository.save(organizationEntity);
//
//        return ResponseDto.<String>builder()
//                .statusCode(200)
//                .status(true)
//                .message("Organization update successful")
//                .data(String.format("Organization with id: %s has been updated.", companyDetailsRequestDto.getOrganizationId()))
//                .build();
//
//    }

//    @Override
//    public ResponseDto<String> updateCompanyAddress(AddressDto addressDto) {
//        OrganizationEntity organizationEntity = organizationRepository.findById(addressDto.getOrganizationId()).orElseThrow(() -> new ResourceNotFoundException(String.format("Organization with id: %s not found", addressDto.getOrganizationId())));
//
//        AddressType addressType =
//                AddressType.builder()
//                        .houseNumber(addressDto.getHouseNumber())
//                        .street(addressDto.getStreet())
//                        .city(addressDto.getCity())
//                        .localGovernmentArea(addressDto.getLocalGovernmentArea())
//                        .state(addressDto.getState())
//                        .country(addressDto.getCountry())
//                        .postalCode(addressDto.getPostalCode())
//                        .build();
//
//        organizationEntity.setAddressType(addressType);
//
//        organizationRepository.save(organizationEntity);
//
//        return ResponseDto.<String>builder()
//                .statusCode(200)
//                .status(true)
//                .message("Organization update successful")
//                .data(String.format("Organization with id: %s has been updated.", addressDto.getOrganizationId()))
//                .build();
//    }
//
//    @Override
//    public ResponseDto<String> updateCompanyContact(CompanyContactDto contactDto) {
//        OrganizationEntity organizationEntity = organizationRepository.findById(contactDto.getOrganizationId()).orElseThrow(() -> new ResourceNotFoundException(String.format("Organization with id: %s not found", contactDto.getOrganizationId())));
//
//        organizationEntity.setContactEmail(contactDto.getBusinessEmail());
//        organizationEntity.setPhoneNumber(contactDto.getBusinessPhoneNumber());
//        organizationEntity.setSupportEmail(contactDto.getSupportEmail());
//
//        organizationRepository.save(organizationEntity);
//
//        return ResponseDto.<String>builder()
//                .statusCode(200)
//                .status(true)
//                .message("Organization update successful")
//                .data(String.format("Organization with id: %s has been updated.", contactDto.getOrganizationId()))
//                .build();
//    }

//    @Override
//    public ResponseDto<String> updateCompanyIdentity(CompanyIdentityDto identityDto) {
//        OrganizationEntity organizationEntity = organizationRepository.findById(identityDto.getOrganizationId()).orElseThrow(() -> new ResourceNotFoundException(String.format("Organization with id: %s not found", identityDto.getOrganizationId())));
//        log.info("trying to get kyc using organization");
//        Optional<KycEntity> kycEntity = kycRepository.findByOrganizationId(identityDto.getOrganizationId());
//        log.info("Kyc entity {}", kycEntity);
//        if (kycEntity.isEmpty()) {
//            log.info("KYC Entity is null, building new kyc");
//            KycEntity kycEntityCreated =
//                    KycEntity
//                            .builder()
//                            .merchantAdminId(organizationEntity.getMerchantAdminId())
//                            .organizationId(identityDto.getOrganizationId())
//                            .rcNumber(identityDto.getRcNumber())
//                            .taxIdentificationNumber(identityDto.getTaxIdentificationNumber())
//                            .rcVerificationStatus(KycVerificationStatus.AWAITING_REVIEW)
//                            .tinVerificationStatus(KycVerificationStatus.AWAITING_REVIEW)
//                            .build();
//            KycEntity savedKycEntity = kycRepository.save(kycEntityCreated);
//
//            KycDocumentEntity kycDocumentEntity = KycDocumentEntity.builder()
//                    .businessLicenseOrPermitUrl(fileAndImageUploadService.uploadFile(identityDto.getBusinessLicenseOrPermitUrl(), "documents"))
//                    .certificateOfIncorporationUrl(fileAndImageUploadService.uploadFile(identityDto.getCertificateOfIncorporationUrl(), "documents"))
//                    .formsCac7Url(fileAndImageUploadService.uploadFile(identityDto.getFormsCac7Url(), "documents"))
//                    .formsCac2Url(fileAndImageUploadService.uploadFile(identityDto.getFormsCac2Url(), "documents"))
//                    .taxIdentificationNumberCertificateUrl(fileAndImageUploadService.uploadFile(identityDto.getTaxIdentificationNumberCertificateUrl(), "documents"))
//                    .registeredAddressUrl(identityDto.getRegisteredAddressUrl())
//                    .businessLicenseVerificationStatus(KycVerificationStatus.AWAITING_REVIEW)
//                    .certificateOfIncorporationVerificationStatus(KycVerificationStatus.AWAITING_REVIEW)
//                    .formsCac2VerificationStatus(KycVerificationStatus.AWAITING_REVIEW)
//                    .formsCac7VerificationStatus(KycVerificationStatus.AWAITING_REVIEW)
//                    .registeredAddressVerificationStatus(KycVerificationStatus.AWAITING_REVIEW)
//                    .kycId(savedKycEntity.getId())
//                   .build();
//
//            log.info("Saving kyc");
//            kycDocRepository.save(kycDocumentEntity);
//        }else {
//            log.info("Its not null, updating kyc");
//            kycEntity.get().setRcNumber(identityDto.getRcNumber() == null ? kycEntity.get().getRcNumber() :
//                    identityDto.getRcNumber());
//            kycEntity.get().setTaxIdentificationNumber(identityDto.getTaxIdentificationNumber() == null ? kycEntity.get().getTaxIdentificationNumber():
//                    identityDto.getTaxIdentificationNumber());
//            kycEntity.get().setRcVerificationStatus(KycVerificationStatus.AWAITING_REVIEW);
//            kycEntity.get().setTinVerificationStatus(KycVerificationStatus.AWAITING_REVIEW);
//
//            kycEntity.get().setRcVerificationStatus(KycVerificationStatus.UPDATED_AWAITING_REVIEW);
//            kycEntity.get().setTinVerificationStatus(KycVerificationStatus.UPDATED_AWAITING_REVIEW);
//
//            KycDocumentEntity kycDocumentEntity = kycDocRepository.findByKycId(kycEntity.get().getId()).orElseThrow(() -> new ResourceNotFoundException("Kyc document entity does not exist for Organization id: "+ identityDto.getOrganizationId()));
//
//            kycDocumentEntity.setBusinessLicenseOrPermitUrl(fileAndImageUploadService.uploadFile(identityDto.getBusinessLicenseOrPermitUrl(), "documents"));
//            kycDocumentEntity.setCertificateOfIncorporationUrl(fileAndImageUploadService.uploadFile(identityDto.getCertificateOfIncorporationUrl(), "documents"));
//            kycDocumentEntity.setFormsCac2Url(fileAndImageUploadService.uploadFile(identityDto.getFormsCac2Url(), "documents"));
//            kycDocumentEntity.setFormsCac7Url(fileAndImageUploadService.uploadFile(identityDto.getFormsCac7Url(), "documents"));
//            kycDocumentEntity.setTaxIdentificationNumberCertificateUrl(fileAndImageUploadService.uploadFile(identityDto.getTaxIdentificationNumberCertificateUrl(), "documents"));
//            kycDocumentEntity.setRegisteredAddressUrl(identityDto.getRegisteredAddressUrl() == null ? kycDocumentEntity.getRegisteredAddressUrl() :
//                    identityDto.getRegisteredAddressUrl());
//            kycDocumentEntity.setBusinessLicenseVerificationStatus(KycVerificationStatus.UPDATED_AWAITING_REVIEW);
//            kycDocumentEntity.setCertificateOfIncorporationVerificationStatus(KycVerificationStatus.UPDATED_AWAITING_REVIEW);
//            kycDocumentEntity.setFormsCac2VerificationStatus(KycVerificationStatus.UPDATED_AWAITING_REVIEW);
//            kycDocumentEntity.setFormsCac7VerificationStatus(KycVerificationStatus.UPDATED_AWAITING_REVIEW);
//            kycDocumentEntity.setRegisteredAddressVerificationStatus(KycVerificationStatus.UPDATED_AWAITING_REVIEW);
//
//            log.info("saving kycDocument and KycEntity");
//            kycDocRepository.save(kycDocumentEntity);
//            kycRepository.save(kycEntity.get());
//        }
//
//        log.info("sending email");
//        UserEntity user = getUserById(organizationEntity.getMerchantAdminId());
//        emailService.sendKycEmail(user.getEmail(), organizationEntity.getOrganizationName());
//
//        return ResponseDto.<String>builder()
//                .statusCode(200)
//                .status(true)
//                .message("Kyc Documents Updated Successfully")
//                .data(String.format("Organization with id: %s has submitted kyc details.", identityDto.getOrganizationId()))
//                .build();
//    }

//    private UserEntity getUserById(String merchantAdminId) {
//     return userRepository.findById(merchantAdminId).orElse(null);
//    }

//    @Override
//    public ResponseDto<String> updateCompanyAccount(CompanyAccountDto accountDto) {
//        OrganizationEntity organizationEntity = organizationRepository.findById(accountDto.getOrganizationId()).orElseThrow(() -> new ResourceNotFoundException(String.format("Organization with id: %s not found", accountDto.getOrganizationId())));
//
//
//        try {
//            Long.parseLong(accountDto.getAccountNumber());
//        } catch (Exception e){
//            throw new IllegalArgumentException("Account number is not valid");
//        }
//        organizationEntity.setSettlementAccountName(accountDto.getAccountName());
//        organizationEntity.setSettlementAccountNumber(accountDto.getAccountNumber());
//        organizationEntity.setSettlementBankName(accountDto.getAccountName());
//        organizationEntity.setSettlementAccountStatus("ACTIVE");
//
//        organizationRepository.save(organizationEntity);
//        return ResponseDto.<String>builder()
//                .statusCode(200)
//                .status(true)
//                .message("Contact account updated Successfully")
//                .data(String.format("Organization with id: %s has been updated.", accountDto.getOrganizationId()))
//                .build();
//    }

    @Override
    public ResponseDto<OrganizationEntity> getOrganizationByUserId(String orgId) {
        return ResponseDto.<OrganizationEntity>builder()
                .statusCode(200)
                .status(true)
                .message("Organization fetched Successfully!")
                .data(organizationRepository.findByOrganizationId(orgId).orElseThrow(() -> new ResourceNotFoundException("No Organization found for this user with the userId: "+ orgId)))
                .build();
    }


}
