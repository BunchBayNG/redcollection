package com.bbng.dao.microservices.auth.organization.controller;


import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.service.OrganizationService;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.PermissionService;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/redtech/merchant/org-management")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final PermissionService permissionService;
    private final JWTService jwtService;
    private final InvitationController controller;
    private final HttpServletRequest request;
//    @PutMapping("merchant/org-management/update-business-on-signup")
//    public ResponseEntity<ResponseDto<String>> updateBusinessDetails(@RequestBody UpdateBusinessRequestDto updateRequestDto){
//
//
//
//
//        permissionService.checkPermission(request, "UPDATE_BUSINESS_ON_SIGNUP", jwtService);
//
//        return ResponseEntity.status(HttpStatus.OK).body(organizationService.updateBusinessDetails(updateRequestDto));
//    }

//    @PutMapping(value = "merchant/org-management/update-company-details", consumes = "multipart/form-data")
//    public ResponseEntity<ResponseDto<String>> updateCompanyDetails(
//            @Parameter(description = "Business logo file") @RequestParam(value = "businessLogoUrl", required = false) MultipartFile businessLogoUrl,
//            @RequestParam String organizationId,
//            @RequestParam(required = false) String organizationName,
//            @RequestParam(required = false)  String description,
//            @RequestParam @ValidEnum(enumClass = BusinessCategoryType.class, message = "Valid types are MANUFACTURING, SERVICE, CONSULTANCY, SOLE_BUSINESS, TECHNOLOGY")
//            BusinessCategoryType businessCategory){
//        var companyDetailsRequestDto = CompanyDetailsRequestDto.builder()
//                .organizationId(organizationId)
//                .organizationName(organizationName)
//                .businessCategory(businessCategory)
//                .description(description).build();
//
//
//        log.info("assigning permissions to update Company Details");
//
//
//        permissionService.checkPermission(request, "UPDATE_COMPANY_DETAILS", jwtService);
//        return ResponseEntity.status(HttpStatus.OK).body(organizationService.updateCompanyDetails(companyDetailsRequestDto, businessLogoUrl));
//    }
//
//    @PutMapping("merchant/org-management/update-company-address")
//    public ResponseEntity<ResponseDto<String>> updateCompanyAddress(@RequestBody AddressDto addressDto){
//
//        log.info("assigning permissions for updating company address");
//
//        permissionService.checkPermission(request, "UPDATE_COMPANY_ADDRESS", jwtService);
//        return ResponseEntity.status(HttpStatus.OK).body(organizationService.updateCompanyAddress(addressDto));
//    }

//    @PutMapping("merchant/org-management/update-company-contacts")
//    public ResponseEntity<ResponseDto<String>> updateCompanyContacts(@RequestBody CompanyContactDto contactDto){
//        log.info("assigning permissions for updating company Contacts");
//
//
//        permissionService.checkPermission(request, "UPDATE_COMPANY_CONTACTS", jwtService);
//        return ResponseEntity.status(HttpStatus.OK).body(organizationService.updateCompanyContact(contactDto));
//    }
//
//    @PutMapping(value = "merchant/org-management/update-company-identity", consumes = "multipart/form-data")
//    public ResponseEntity<ResponseDto<String>> updateCompanyIdentity(
//            @RequestPart(required = false) MultipartFile businessLicenseOrPermitUrl,
//
//            @RequestPart(required = false) MultipartFile certificateOfIncorporationUrl,
//
//            @RequestPart(required = false) MultipartFile formsCac2Url,
//
//            @RequestPart(required = false) MultipartFile formsCac7Url,
//
//            @RequestPart(required = false) MultipartFile taxIdentificationNumberCertificateUrl,
//            @RequestParam String organizationId,
//    @RequestParam(required = false) String rcNumber,
//    @RequestParam(required = false) String taxIdentificationNumber,
//    @RequestParam(required = false) String registeredAddressUrl
//    ){
//        var identityDto = CompanyIdentityDto.builder()
//                .taxIdentificationNumber(taxIdentificationNumber)
//                .businessLicenseOrPermitUrl(businessLicenseOrPermitUrl)
//                .formsCac7Url(formsCac7Url)
//                .formsCac2Url(formsCac2Url)
//                .certificateOfIncorporationUrl(certificateOfIncorporationUrl)
//                .taxIdentificationNumberCertificateUrl(taxIdentificationNumberCertificateUrl)
//                .organizationId(organizationId)
//                .rcNumber(rcNumber)
//                .registeredAddressUrl(registeredAddressUrl).build();
//        log.info("assigning permissions in update company details");
//
//        permissionService.checkPermission(request, "UPDATE_COMPANY_IDENTITY", jwtService);
//
//        return ResponseEntity.status(HttpStatus.OK).body(organizationService.updateCompanyIdentity(identityDto));
//    }

//    @PutMapping("merchant/org-management/update-company-account")
//    public ResponseEntity<ResponseDto<String>> updateCompanyAccount(@RequestBody CompanyAccountDto accountDto){
//
//        permissionService.checkPermission(request,"UPDATE_COMPANY_ACCOUNT", jwtService);
//        return ResponseEntity.status(HttpStatus.OK).body(organizationService.updateCompanyAccount(accountDto));
//    }

    @GetMapping("merchant/org-management/get-organizationBy-orgId")
    public ResponseEntity<ResponseDto<OrganizationEntity>> getOrganizationByUserId(@RequestParam String orgId) {
        log.info("assigning permissions for get organisation by userId");

        permissionService.checkPermission(request, "GET_ORGANIZATION_BY_USERID", jwtService);
        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getOrganizationByUserId(orgId));
    }


//    @GetMapping("admin/org-management/get-all-kyc")
//    public  ResponseEntity<ResponseDto<List<KycResponseDto>>> fetchAllKyc(){
//        log.info("assigning permissions for get All Kyc");
//
//        permissionService.checkPermission(request,"ADMIN_GET_MERCHANTS_KYC", jwtService);
//        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getKycs());
//    }
//
//    @GetMapping("admin/org-management/get-kyc-stats")
//    public  ResponseEntity<ResponseDto<KycStatsResponseDto>> getKycStats(){
//        log.info("assigning permissions for get All Kyc Stats");
//
//        permissionService.checkPermission(request,"ADMIN_GET_KYC_STATS", jwtService);
//        return ResponseEntity.status(HttpStatus.OK).body(organizationService.getKycStats());
//    }


}
