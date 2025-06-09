package com.bbng.dao.microservices.auth.passport.impl.serviceImpl;

import com.bbng.dao.microservices.auth.auditlog.Events;
import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogRequestDto;
import com.bbng.dao.microservices.auth.auditlog.repository.AuditLogRepository;
import com.bbng.dao.microservices.auth.auditlog.service.AuditLogService;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.OrgStaffRepository;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.config.PassportUserDetailsService;
import com.bbng.dao.microservices.auth.passport.dto.request.ChangePasswordDto;
import com.bbng.dao.microservices.auth.passport.dto.request.LoginDto;
import com.bbng.dao.microservices.auth.passport.dto.response.Authentication;
import com.bbng.dao.microservices.auth.passport.dto.response.AuthenticationResponseDto;
import com.bbng.dao.microservices.auth.passport.dto.response.LoginResponseDto;
import com.bbng.dao.microservices.auth.passport.dto.response.MfaDto;
import com.bbng.dao.microservices.auth.passport.entity.PermissionEntity;
import com.bbng.dao.microservices.auth.passport.entity.RoleEntity;
import com.bbng.dao.microservices.auth.passport.entity.TokenEntity;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.enums.TokenType;
import com.bbng.dao.microservices.auth.passport.repository.RoleRepository;
import com.bbng.dao.microservices.auth.passport.repository.TokenRepository;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.microservices.auth.passport.service.UserService;
import com.bbng.dao.util.email.entity.VerificationToken;
import com.bbng.dao.util.email.repository.VerificationTokenRepository;
import com.bbng.dao.util.email.service.EmailService;
import com.bbng.dao.util.email.service.EmailVerificationService;
import com.bbng.dao.util.email.service.JavaMailService;
import com.bbng.dao.util.exceptions.customExceptions.*;
import com.bbng.dao.util.response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Service
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserServiceImpl implements UserService {
    private final AuditLogRepository auditLogRepository;

    public final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!-_])(?=\\S+$).{8,20}$";

    private final PassportUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final TokenRepository tokenRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailVerificationService emailVerificationService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JavaMailService javaMailService;
    private final AuditLogService auditLogService;
    private final OrgStaffRepository orgStaffRepository;



    @Override
    public ResponseDto<MfaDto> login(LoginDto loginDto) {
        String token ="";
        String refreshToken = "";
        UserEntity user;
       try {
           org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
           SecurityContextHolder.getContext().setAuthentication(authentication);
           log.info("authentication name {}", authentication.getName());
           log.info("authentication principal {}", authentication.getPrincipal());
           user = userRepository.findByUsernameOrEmail(loginDto.getEmail(), loginDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException(String.format("User with %s not found", loginDto.getEmail())));
           log.info("user email {}", user.getEmail());

           Map<String, Object> claims = generateHashMap(loginDto.getEmail());
            token = jwtService.generateTokenWithClaims(authentication.getName(), claims);
          refreshToken = jwtService.generateRefreshToken(claims, authentication.getName());

          log.info("token {}", token);
          log.info("refreshToken {}", refreshToken);

       } catch (Exception e) {
           log.info("An exception Occurred");
           throw new ForbiddenException(e.toString());
       }


        revokeValidTokens(user);

        TokenEntity tokenEntity = TokenEntity.builder()
                .userEntity(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(tokenEntity);

        emailVerificationService.send2faEmail(loginDto.getEmail());

                return ResponseDto.<MfaDto>builder()
                .statusCode(200)
                .status(true)
                .message("Login successful, Enter MFA code to verify your account")
                .data(MfaDto
                        .builder()
                        .isLogin(true)
                        .build())
                .build();

        //var t = tokenRepository.save(tokenEntity);

//       OrganizationEntity organizationEntity = organizationRepository.findOrganizationByMerchantAdminId(user.getId()).orElse(organizationRepository.findByOrganizationId(orgStaffRepository.findOrganizationIdByUserId(user.getId()).get()).get());
//
//       if (organizationEntity == null){
//           throw new ForbiddenException("User is not linked with any organization");
//       }
//        Optional<OrganizationEntity> org = organizationRepository.findOrganizationByMerchantAdminId(user.getId());  // current user
//
//
//        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
//                .userId(user.getId())
//                .userName(user.getUserName())
//                .merchantId(null)
//                .merchantName(null)
//                .userType(String.valueOf(user.getUsertype()))
//                .event(Events.LOGIN.name())
//                .dateTimeStamp(Instant.now())
//                .isDeleted(false)
//                .succeeded(true)
//                .build());
//
//        return ResponseDto.<LoginResponseDto>builder()
//                .statusCode(200)
//                .status(true)
//                .message("Login successful")
//                .data(LoginResponseDto
//                        .builder()
//                        .accessToken(token)
//                        .refreshToken(refreshToken)
//                        .acctStatus(user.getAcctStatus().name())
//                        .userId(user.getId())
//                        .organizationId(org.get().getId())
//                        .isEmailVerified(user.getIsEnabled())
//                        .build())
//                .build();
    }


    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String refreshToken = authHeader.substring(7);
        String username = jwtService.getUsername(refreshToken);

        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UserEntity user = this.userRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Show yourself if you are the problem"));

            if (jwtService.isTokenValid(refreshToken, userDetails)) {

                Map<String, Object> claims = generateHashMap(user.getEmail());

                var accessToken = jwtService.generateTokenWithClaims(user.getUserName(), claims);
                revokeValidTokens(user);
                TokenEntity tokenEntity = TokenEntity.builder()
                        .userEntity(user)
                        .token(accessToken)
                        .tokenType(TokenType.BEARER)
                        .expired(false)
                        .revoked(false)
                        .build();

                var t = tokenRepository.save(tokenEntity);
                var authResponse = AuthenticationResponseDto.builder()
                        .message("Refresh token used to generate access token successfully")
                        .userId(user.getId())
                        .authentication(Authentication.REFRESH_TOKEN)
                        .token(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public ResponseDto<String> changePassword(ChangePasswordDto changePasswordDto) {

        UserEntity user = userRepository.findByEmail(changePasswordDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with %s not found", changePasswordDto.getEmail())));

        // Check if the old password matches the user's current password
        if (passwordEncoder.matches(changePasswordDto.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("Can't use old password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);

        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .merchantId(null)
                .merchantName(null)
                .userType(String.valueOf(user.getUsertype()))
                .event(Events.CHANGE_PASSWORD.name())
                .dateTimeStamp(Instant.now())
                .isDeleted(false)
                .succeeded(true)
                .build());

        return ResponseDto.<String>builder()
                .statusCode(200)
                .status(true)
                .message("Password changed successfully, you can now login")
                .data(String.format("Password changed for User with Id: %s", user.getId()))
                .build();
    }


    @Override
    public ResponseDto<String> resetPassword(String token, String newPassword, String confirmPassword) {

        VerificationToken verificationToken = verificationTokenRepository
                .findByVerificationTokenAndExpired(token, false)
                .orElseThrow(() -> new BadRequestException("Token expired. Resend email"));
        UserEntity user = userRepository.findByEmail(verificationToken.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));
        if (!isInputValid(newPassword, passwordRegex)) {
            throw new BadRequestException(WRONG_PASSWORD_FORMAT);
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("Both passwords do not match");
        }

        if(passwordEncoder.matches(newPassword, user.getPassword())){
            throw new BadRequestException("Can't use your old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setIsEnabled(true);
        userRepository.save(user);

        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .merchantId(null)
                .merchantName(null)
                .userType(String.valueOf(user.getUsertype()))
                .event(Events.RESET_PASSWORD.name())
                .dateTimeStamp(Instant.now())
                .isDeleted(false)
                .succeeded(true)
                .build());

        return ResponseDto.<String>builder()
                .statusCode(200)
                .status(true)
                .message("Password reset successfully")
                .data(String.format("Password reset for User with Id: %s", user.getId()))
                .build();

    }

//
//    @Override
//    public UserResponseDto editUser(String email, UserRequestDto userRequestDTO) {
//        UserEntity userEntity = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        UsersMapper.mapToUserEntity(userRequestDTO, userEntity);
//        userRepository.save(userEntity);
//        UserResponseDto userResponseDto = new UserResponseDto();
//
//        return UsersMapper.mapToUserResponseDto(userEntity, userResponseDto);
//
//    }

    private void revokeValidTokens(UserEntity user) {

        List<TokenEntity> tokenEntities = tokenRepository.findAllValidTokensByUserId(user.getId());

        if (tokenEntities.isEmpty())
            return;
        tokenEntities.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(tokenEntities);
    }


    private boolean isInputValid(String input, String regex) {
        return Pattern.compile(regex)
                .matcher(input)
                .matches();
    }

    private final static String WRONG_PASSWORD_FORMAT = """
                        Please pass at least one digit.
                        At least one lowercase letter.
                        At least one uppercase letter.
                        At least one special character from the specified set (@, #, $, %, ^, &, +, =, !, -, _)
                        No whitespace characters.
                        The overall length of the password must be between 8 and 20 characters.")
            """;

    private Map<String, Object> generateHashMap(String username) {


        UserEntity user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        List<String> userRoles = user.getRoleEntities().stream().map(RoleEntity::getRoleName).toList();

        List<String> userPermissions = user.getRoleEntities().stream().flatMap(roleEntity -> roleEntity.getPermissions().stream()).map(PermissionEntity::getName).toList();

        OrganizationEntity organizationEntity = organizationRepository.findOrganizationByMerchantAdminId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userRoles);
        claims.put("userName", user.getUserName());
        claims.put("userType", String.valueOf(user.getUsertype()));
        claims.put("organization", organizationEntity.getOrganizationName());
        claims.put("organizationId", organizationEntity.getId());
        claims.put("permissions", userPermissions);
        claims.put("email", user.getEmail()); // Assuming getUsername() returns the email
        return claims;
    }

    public static String generateRandomDigits() {
        int randomDigits = 1000 + new SecureRandom().nextInt(9000);
        return String.valueOf(randomDigits);
    }

}
