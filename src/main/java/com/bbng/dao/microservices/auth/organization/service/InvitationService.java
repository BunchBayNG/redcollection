package com.bbng.dao.microservices.auth.organization.service;



import com.bbng.dao.microservices.auth.organization.dto.request.AssignRoleRequestDto;
import com.bbng.dao.microservices.auth.organization.dto.request.InviteRequestDto;
import com.bbng.dao.microservices.auth.organization.dto.request.OnboardOrgDto;
import com.bbng.dao.microservices.auth.passport.dto.response.UserResponseDto;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.DataInitializerServiceImpl;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface InvitationService {

    ResponseDto<String> inviteUser(InviteRequestDto inviteRequestDto);

    ResponseDto<List<UserResponseDto>> getAllStaff(String merchantAdminId);

    ResponseDto<String> disableStaff(String merchantAdminId, String staffId);
    ResponseDto<String> onboardOrg(OnboardOrgDto onboardOrgDto);

    ResponseDto<String> assignPermissionsToRole(AssignRoleRequestDto assignRoleRequestDto, DataInitializerServiceImpl dataInitializerService);

    ResponseDto<String> createRoleWithPermissions(AssignRoleRequestDto assignRoleRequestDto, DataInitializerServiceImpl dataInitializerService);

    ResponseDto<String> disablePermission(String username, String merchantAdminId, Long permissionId);
}
