package com.bbng.dao.microservices.auth.passport.controller;

import com.bbng.dao.microservices.auth.passport.dto.request.UserProfileUpdateRequestDto;
import com.bbng.dao.microservices.auth.passport.dto.response.UserProfileDto;
import com.bbng.dao.microservices.auth.passport.dto.response.UserResponseDto;
import com.bbng.dao.microservices.auth.passport.service.UserManagementService;
import com.bbng.dao.util.response.PagedResponseDto;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController("api/v1/redtech/user-management")
@Slf4j
public class UserManagementController {

    private final UserManagementService userManagementService;

    @PutMapping(value = "update-user", consumes = "multipart/form-data")
    ResponseEntity<ResponseDto<String>> updateUserProfile(
            @RequestParam String userId,
            @RequestParam(required = false) MultipartFile logoUrl
    ) {
        var userProfileUpdateRequestDto = UserProfileUpdateRequestDto.builder()
                .userId(userId)
                .logoUrl(logoUrl).build();
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.updateUserProfile(userProfileUpdateRequestDto));
    }

    @GetMapping("get-users")
    public ResponseEntity<PagedResponseDto<UserResponseDto>> getUsers(
            @RequestParam(required = false, defaultValue = "1", value = "pageNo")
            @Min(value = 1, message = "Minimum page no is 1") int pageNo,
            @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,

            @RequestParam(required = false) String id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String userType,
            @RequestParam(required = false) String dateBegin,
            @RequestParam(required = false) String dateEnd
    ) {
        log.info("fetching all users");
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.getAllUsers(id, email, firstName, lastName, userType, dateBegin, dateEnd, pageNo, pageSize));
    }

    @GetMapping("get-user-profile")
    public ResponseEntity<ResponseDto<UserProfileDto>> getUserProfile(@RequestParam String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.getUserProfile(userId));
    }

}
