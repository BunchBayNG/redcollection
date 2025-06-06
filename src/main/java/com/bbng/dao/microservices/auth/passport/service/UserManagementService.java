package com.bbng.dao.microservices.auth.passport.service;


import com.bbng.dao.microservices.auth.passport.dto.request.UserProfileUpdateRequestDto;
import com.bbng.dao.microservices.auth.passport.dto.response.UserProfileDto;
import com.bbng.dao.microservices.auth.passport.dto.response.UserResponseDto;
import com.bbng.dao.util.response.PagedResponseDto;
import com.bbng.dao.util.response.ResponseDto;

public interface UserManagementService {

    ResponseDto<String> updateUserProfile(UserProfileUpdateRequestDto userProfileUpdateRequestDto);

    PagedResponseDto<UserResponseDto> getAllUsers(String id, String email, String firstName, String lastName, String userType, String dateBegin, String dateEnd, int pageNo, int pageSize);

    ResponseDto<UserProfileDto> getUserProfile(String userId);
}
