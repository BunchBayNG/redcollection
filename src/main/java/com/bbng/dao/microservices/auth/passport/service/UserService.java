package com.bbng.dao.microservices.auth.passport.service;

import com.bbng.dao.microservices.auth.passport.dto.request.ChangePasswordDto;
import com.bbng.dao.microservices.auth.passport.dto.request.LoginDto;
import com.bbng.dao.microservices.auth.passport.dto.response.MfaDto;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserService {


    ResponseDto<MfaDto> login(LoginDto loginDto);

    ResponseDto<String> changePassword(ChangePasswordDto changePasswordDto);

    ResponseDto<String> resetPassword(String token, String newPassword, String confirmPassword);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;


}
