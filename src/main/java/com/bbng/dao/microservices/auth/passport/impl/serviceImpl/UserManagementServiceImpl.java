package com.bbng.dao.microservices.auth.passport.impl.serviceImpl;

import com.bbng.dao.microservices.auth.organization.repository.OrgStaffRepository;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.passport.dto.request.UserProfileUpdateRequestDto;
import com.bbng.dao.microservices.auth.passport.dto.response.UserProfileDto;
import com.bbng.dao.microservices.auth.passport.dto.response.UserResponseDto;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.microservices.auth.passport.service.UserManagementService;
import com.bbng.dao.util.exceptions.customExceptions.BadRequestException;
import com.bbng.dao.util.exceptions.customExceptions.ResourceNotFoundException;
import com.bbng.dao.util.exceptions.customExceptions.UserNotFoundException;
import com.bbng.dao.util.fileUpload.services.FileAndImageUploadService;
import com.bbng.dao.util.response.PagedResponseDto;
import com.bbng.dao.util.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;
    private final UserFilterSpec userFilterSpec;
    private final OrganizationRepository organizationRepository;
    private final OrgStaffRepository orgStaffRepository;
    private FileAndImageUploadService fileAndImageUploadService;

    @Override
    public ResponseDto<String> updateUserProfile(UserProfileUpdateRequestDto userProfileUpdateRequestDto) {
        UserEntity userEntity = userRepository.findById(userProfileUpdateRequestDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException(String.format("User with Id: %s not found", userProfileUpdateRequestDto.getUserId())));
        userEntity.setLogoUrl(fileAndImageUploadService.uploadFile(userProfileUpdateRequestDto.getLogoUrl(), "profile_images"));
        userRepository.save(userEntity);

        return ResponseDto.<String>builder()
                .statusCode(200)
                .status(true)
                .message("User update successful")
                .data(String.format("User with id: %s has been updated.", userProfileUpdateRequestDto.getUserId()))
                .build();
    }

    @Override
    public PagedResponseDto<UserResponseDto> getAllUsers(String id, String email, String firstName, String lastName, String userType, String dateBegin, String dateEnd, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.ASC, "createdAt"));

        Page<UserEntity> responsePage = userRepository.findAll(userFilterSpec.filterTransactionData(
                Optional.ofNullable((dateBegin == null) ? null : validateDate(dateBegin)),
                Optional.ofNullable((dateEnd == null) ? null : validateDate(dateBegin)),
                Optional.ofNullable(id),
                Optional.ofNullable(email),
                Optional.ofNullable(firstName),
                Optional.ofNullable(lastName),
                Optional.ofNullable(userType)
        ), pageable);


        Page<UserResponseDto> responses = new PageImpl<>(
                responsePage.getContent().stream().map(this::mapToUserResponseDto).toList(), pageable, responsePage.getTotalElements());

        return PagedResponseDto.<UserResponseDto>builder()
                .statusCode(200)
                .status(true)
                .message("Fetch all users")
                .currentPage(responsePage.getNumber())
                .itemsPerPage(responses.getSize())
                .totalItems(responsePage.getTotalElements())
                .totalPages(responses.getTotalPages())
                .items(responses.getContent())
                .build();
    }

    @Override
    public ResponseDto<UserProfileDto> getUserProfile(String userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No User Found with the given userId: " + userId));
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setEmail(user.getEmail());
        //getBusinessLogo
//        OrganizationEntity ogr = organizationRepository.findOrganizationByMerchantAdminId(user.getId()).orElse(null);
//
//        if (ogr == null){
//            OrganizationEntity og = organizationRepository.findByOrganizationId(orgStaffRepository.findOrganizationIdByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("No Organization Found for this Staff merchant"))).orElseThrow(() -> new ResourceNotFoundException("No Organization Found for this merchant"));
//            userProfileDto.setBusinessLogo(og.getBusinessLogoUrl());
//        }else {
//            userProfileDto.setBusinessLogo(ogr.getBusinessLogoUrl());
//        }

        return ResponseDto.<UserProfileDto>builder()
                .status(true)
                .statusCode(200)
                .message("User Profile successfully retrieved!")
                .data(userProfileDto).build();


    }

    private Instant validateDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new BadRequestException("Date cannot be null");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(date, formatter);

            // Convert LocalDate to Instant at the start of the day in the system default time zone
            return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        } catch (Exception e) {
            throw new BadRequestException("Date format is incorrect. Format should be yyyy-MM-dd");
        }
    }

    UserResponseDto mapToUserResponseDto(UserEntity userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .username(userEntity.getUserName())
                .userType(String.valueOf(userEntity.getUsertype()))
                .phoneNumber(userEntity.getPhoneNumber())
                .isEnabled(userEntity.getIsEnabled())
                .logoUrl(userEntity.getLogoUrl())
                .isInvitedUser(userEntity.isInvitedUser())
                .build();
    }
}
