package com.bbng.dao.microservices.auth.organization.impl;


import com.bbng.dao.microservices.auth.organization.dto.request.UpdateOrgDto;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.organization.service.OrganizationService;
import com.bbng.dao.microservices.report.config.OrganizationSpecification;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.response.ResponseDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

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
        organizationEntity.setContactFirstName(updateRequestDto.getContactFirstName());
        organizationEntity.setContactLastName(updateRequestDto.getContactLastName());
        organizationEntity.setContactEmail(updateRequestDto.getContactEmail());
        organizationEntity.setOrgStatus(updateRequestDto.getStatus());
        organizationEntity.setProductPrefix(updateRequestDto.getProductPrefix());

        organizationRepository.save(organizationEntity);

        return ResponseDto.<String>builder()
                .statusCode(200)
                .status(true)
                .message("Organization onboarding successful")
                .data(String.format("Organization with id: %s has been updated.", updateRequestDto.getOrganizationId()))
                .build();

    }

    @Override
    public ResponseDto<Page<OrganizationEntity>>  getAllOrg(String search, String status,
                                                            String sortBy, String sortOrder, LocalDate startDate,
                                                            LocalDate endDate, int page, int size) {
        Specification<OrganizationEntity> spec =
                OrganizationSpecification.getOrganizations(search, status, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);

        Page<OrganizationEntity> response = organizationRepository.findAll(spec, pageable);

        return ResponseDto.<Page<OrganizationEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Merchants fetched successfully")
                .data(response)
                .build();
    }

    @Override
    public ResponseDto<Long> getNewMerchantCount(LocalDateTime startDate, LocalDateTime endDate) {
        Long response =  organizationRepository.countByCreatedAtBetween(startDate, endDate);

        return ResponseDto.<Long>builder()
                .statusCode(200)
                .status(true)
                .message("Total Merchants fetched successfully")
                .data(response)
                .build();
    }

    @Override
    public ResponseDto<Long> getTotalMerchantCount() {
        Long response =  organizationRepository.count();

        return ResponseDto.<Long>builder()
                .statusCode(200)
                .status(true)
                .message("Total Merchants fetched successfully")
                .data(response)
                .build();
    }



    private Pageable getPageable(String sortBy, String sortOrder, int page, int size) {
        String  defaultSortBy = sortBy != null ? sortBy : "createdAt";
        String defaultSortOrder = sortOrder != null ? sortOrder.toUpperCase() : "DESC";

        Sort sort = switch (defaultSortOrder) {
            case "ASC" -> Sort.by(Sort.Direction.ASC, defaultSortBy);
            case "DESC" -> Sort.by(Sort.Direction.DESC, defaultSortBy);
            case "ACTIVE_FIRST" -> Sort.by(Sort.Order.desc("status"));
            case "INACTIVE_FIRST" -> Sort.by(Sort.Order.asc("status"));
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };

        return PageRequest.of(page, size, sort);
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
                .data(organizationRepository.findByOrganizationId(orgId).orElseThrow(() -> new ResourceNotFoundException("No Organization found for this user with the userId: " + orgId)))
                .build();
    }


    @Override
    public byte[] exportToCsv( LocalDate startDate, LocalDate endDate) {


        Specification<OrganizationEntity> spec = OrganizationSpecification.getOrganizations("",  "", startDate, endDate);


        List<OrganizationEntity> organizations = organizationRepository.findAll(spec);
        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append( "Organization Name,Contact First Name,Contact Last Name,Contact Email Address,Contact Phone Number, Registered BVN,Organization Status,Product Prefix,Settlement Acct Name,Settlement Acct No, Settlement Bank Name,Settlement Acct Status,Timestamp\n");



        // Data rows
        for (OrganizationEntity org : organizations) {
            csv.append(org.getOrganizationName()).append(",");
            csv.append(org.getContactFirstName()).append(",");
            csv.append(org.getContactLastName()).append(",");
            csv.append(org.getContactEmail()).append(",");
            csv.append(org.getContactPhoneNumber()).append(",");
            csv.append(org.getRegisteredBVN()).append(",");
            csv.append(org.getBusinessLogoUrl()).append(",");
            csv.append(org.getProductPrefix()).append(",");
            csv.append(org.getSettlementAccountName()).append(",");
            csv.append(org.getSettlementAccountNumber()).append(",");
            csv.append(org.getSettlementBankName()).append(",");
            csv.append(org.getSettlementAccountStatus()).append(",");
            csv.append(org.getOrgStatus().name()).append(",");
            csv.append(org.getCreatedAt()).append("\n");

        }

        return csv.toString().getBytes();
    }

    @Override
    public byte[] exportToPdf( LocalDate startDate,LocalDate endDate) throws Exception {

        Specification<OrganizationEntity> spec = OrganizationSpecification.getOrganizations("",  "", startDate, endDate);


        List<OrganizationEntity> organizations = organizationRepository.findAll(spec);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA);

        document.add(new Paragraph("Organizations Report", headerFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{3, 4, 2, 2, 4});

        // Headers
        Stream.of("Organization Name", "Contact First Name", "Contact Last Name", "Contact Email Address", "Contact Phone Number",
                        "Registered BVN","Organization Status","Product Prefix","Settlement Acct Name", "Settlement Acct No",
                        "Settlement Bank Name","Settlement Acct Status","Timestamp")
                .forEach(title -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(title, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        // Data rows
        for (OrganizationEntity org : organizations) {
            table.addCell(new PdfPCell(new Phrase(org.getOrganizationName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getContactFirstName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getContactLastName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getContactEmail(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getContactPhoneNumber(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getRegisteredBVN(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getBusinessLogoUrl(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getProductPrefix(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getSettlementAccountName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getSettlementAccountNumber(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getSettlementBankName(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getSettlementAccountStatus(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getOrgStatus().name(), bodyFont)));
            table.addCell(new PdfPCell(new Phrase(org.getCreatedAt().toString(), bodyFont)));
        }


        document.add(table);
        document.close();
        return out.toByteArray();
    }


    @Override
    public byte[] exportToExcel( LocalDate startDate, LocalDate endDate) throws Exception {

        Specification<OrganizationEntity> spec = OrganizationSpecification.getOrganizations("",  "", startDate, endDate);


        List<OrganizationEntity> organizations = organizationRepository.findAll(spec);


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Organizations");

        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] headers = {"Organization Name", "Contact First Name", "Contact Last Name", "Contact Email Address", "Contact Phone Number",
                "Registered BVN","Organization Status","Product Prefix","Settlement Acct Name", "Settlement Acct No",
                "Settlement Bank Name","Settlement Acct Status","Timestamp"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;
        for (OrganizationEntity org : organizations) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(org.getOrganizationName());
            row.createCell(1).setCellValue(org.getContactFirstName());
            row.createCell(2).setCellValue(org.getContactLastName());
            row.createCell(3).setCellValue(org.getContactEmail());
            row.createCell(4).setCellValue(org.getContactPhoneNumber());
            row.createCell(5).setCellValue(org.getRegisteredBVN());
            row.createCell(6).setCellValue(org.getOrgStatus().name());
            row.createCell(7).setCellValue(org.getProductPrefix());
            row.createCell(8).setCellValue(org.getSettlementAccountName());
            row.createCell(9).setCellValue(org.getSettlementAccountNumber());
            row.createCell(10).setCellValue(org.getSettlementBankName());
            row.createCell(11).setCellValue(org.getSettlementAccountStatus());
            row.createCell(12).setCellValue(org.getCreatedAt().toString());

        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }



}
