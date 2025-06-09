package com.bbng.dao.microservices.auth.passport.controller;



import com.bbng.dao.microservices.auth.passport.config.LogoutService;
import com.bbng.dao.microservices.auth.passport.dto.request.ChangePasswordDto;
import com.bbng.dao.microservices.auth.passport.dto.request.LoginDto;
import com.bbng.dao.microservices.auth.passport.dto.response.LoginResponseDto;
import com.bbng.dao.microservices.auth.passport.dto.response.MfaDto;
import com.bbng.dao.microservices.auth.passport.service.UserService;
import com.bbng.dao.util.email.dto.response.EmailResponseDto;
import com.bbng.dao.util.email.service.EmailVerificationService;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/redtech/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PassportController {

    private final UserService userService;
    private final LogoutService logoutService;
    private final EmailVerificationService emailService;

//    @PostMapping("signup")
//    public ResponseEntity<ResponseDto<String>> userSignup(@RequestBody @Valid SignUpDto signUpDto) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(signUpDto));
//    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDto<MfaDto>> userLogin(@RequestBody @Valid LoginDto loginDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(loginDto));
    }

    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.refreshToken(request, response);
    }

    @GetMapping("logout")
    public ResponseEntity<ResponseDto<String>> logout(HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            Authentication authentication) {
        logoutService.logout(request, response, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.<String>builder()
                .statusCode(200)
                .status(true)
                .message("Logout successful")
                .data("User has been logged out. ")
                .build());
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseDto<String>> changePassword(
            @RequestBody @Valid ChangePasswordDto changePasswordDto) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.changePassword(changePasswordDto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDto<String>> resetPassword(@RequestParam(name = "newPassword") String newPassword,
                                                             @RequestParam(name = "confirmPassword") String confirmPassword,
                                                             @RequestParam(name = "token") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.resetPassword(token, newPassword, confirmPassword));
    }



    @PostMapping("/reset-password-mail")
    public EmailResponseDto[] sendForgotPasswordMail(@RequestParam(name = "email") String toEmail) {
        return emailService.sendForgotPasswordMail(toEmail);

    }

    @PostMapping("/resend-email")
    public EmailResponseDto[] sendForgotPasswordMail(@RequestParam(name = "email") String toEmail, HttpServletRequest request) {
        return emailService.sendVerificationEmail(toEmail);
    }


    @GetMapping("/verify-email")
    public ResponseEntity<ResponseDto<String>> verifyEmail(@RequestParam("token") String verificationToken) {
        return ResponseEntity.status(HttpStatus.OK).body(emailService.verifyEmail(verificationToken));
    }


    @GetMapping("/verify-login-otp")
    public ResponseEntity<ResponseDto<LoginResponseDto>>  verifyLoginOtp(@RequestParam("otp") String otpToken) {
        return ResponseEntity.status(HttpStatus.OK).body(emailService.verify2faEmail(otpToken));
    }

//
//
//    @PutMapping("/{email}")
//    public ResponseEntity<UserResponseDto> editUser(@PathVariable String email,
//                                                    @RequestBody UserRequestDto userRequestDto) {
//        UserResponseDto updatedUser = userService.editUser(email, userRequestDto);
//        return ResponseEntity.ok(updatedUser);
//    }
}
