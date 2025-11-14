
package com.bbng.dao.microservices.auth.passport.impl.serviceImpl;

import com.bbng.dao.microservices.auth.auditlog.Events;
import com.bbng.dao.microservices.auth.auditlog.dto.request.AuditLogRequestDto;
import com.bbng.dao.microservices.auth.auditlog.service.AuditLogService;
import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.config.PassportUserDetailsService;
import com.bbng.dao.microservices.auth.passport.dto.request.ChangePasswordDto;
import com.bbng.dao.microservices.auth.passport.dto.request.LoginDto;
import com.bbng.dao.microservices.auth.passport.dto.response.Authentication;
import com.bbng.dao.microservices.auth.passport.dto.response.AuthenticationResponseDto;
import com.bbng.dao.microservices.auth.passport.dto.response.MfaDto;
import com.bbng.dao.microservices.auth.passport.entity.PermissionEntity;
import com.bbng.dao.microservices.auth.passport.entity.RoleEntity;
import com.bbng.dao.microservices.auth.passport.entity.TokenEntity;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.enums.TokenType;
import com.bbng.dao.microservices.auth.passport.enums.UserType;
import com.bbng.dao.microservices.auth.passport.repository.TokenRepository;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.microservices.auth.passport.service.UserService;
import com.bbng.dao.util.email.entity.VerificationToken;
import com.bbng.dao.util.email.repository.VerificationTokenRepository;
import com.bbng.dao.util.email.service.EmailVerificationService;
import com.bbng.dao.util.exceptions.customExceptions.BadRequestException;
import com.bbng.dao.util.exceptions.customExceptions.ForbiddenException;
import com.bbng.dao.util.response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserServiceImpl implements UserService {

    // Constants
    private static final String WRONG_PASSWORD_FORMAT = """
                        Please pass at least one digit.
                        At least one lowercase letter.
                        At least one uppercase letter.
                        At least one special character from the specified set (@, #, $, %, ^, &, +, =, !, -, _)
                        No whitespace characters.
                        The overall length of the password must be between 8 and 20 characters.")
            """;
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!-_])(?=\\S+$).{8,20}$";
    private static final String BAD_CREDENTIALS_MESSAGE = "Bad credentials, please check your username and password";
    private static final String LOGIN_SUCCESS_MESSAGE = "Login successful, Enter MFA code to verify your account";
    private static final String REFRESH_TOKEN_SUCCESS_MESSAGE = "Refresh token used to generate access token successfully";
    private static final String PASSWORD_CHANGED_MESSAGE = "Password changed successfully, you can now login";
    private static final String PASSWORD_RESET_MESSAGE = "Password reset successfully";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int STATUS_CODE_SUCCESS = 200;
    private static final int BEARER_TOKEN_START_INDEX = 7;

    @Value("${orgName}")
    private String organizationName;
    @Value("${orgId}")
    private String organizationId;

    private final PassportUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final TokenRepository tokenRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuditLogService auditLogService;

    @Override
    public ResponseDto<MfaDto> login(LoginDto loginDto) {
        UserEntity user = authenticateUser(loginDto);
        String token = generateAccessToken(loginDto.getEmail(), user.getUserName());

        revokeValidTokens(user);
        TokenEntity savedToken = saveToken(user, token);
        emailVerificationService.send2faEmail(loginDto.getEmail());

        return buildLoginResponse(savedToken.getToken());
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return;
        }

        String refreshToken = authHeader.substring(BEARER_TOKEN_START_INDEX);
        String username = jwtService.getUsername(refreshToken);

        if (username != null) {
            processRefreshToken(refreshToken, username, response);
        }
    }

    @Override
    public ResponseDto<String> changePassword(ChangePasswordDto changePasswordDto) {
        UserEntity user = findUserByEmail(changePasswordDto.getEmail());
        validateNewPassword(changePasswordDto.getNewPassword(), user.getPassword());

        updateUserPassword(user, changePasswordDto.getNewPassword());
        logAuditEvent(user, Events.CHANGE_PASSWORD);

        return buildPasswordChangeResponse(user.getId());
    }

    @Override
    public ResponseDto<String> resetPassword(String token, String newPassword, String confirmPassword) {
        VerificationToken verificationToken = validateResetToken(token);
        UserEntity user = findUserByEmail(verificationToken.getEmail());

        validatePasswordReset(newPassword, confirmPassword, user.getPassword());

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setIsEnabled(true);
        userRepository.save(user);

        logAuditEvent(user, Events.RESET_PASSWORD);

        return buildPasswordResetResponse(user.getId());
    }

    private UserEntity authenticateUser(LoginDto loginDto) {
        try {
            org.springframework.security.core.Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return userRepository.findByUsernameOrEmail(loginDto.getEmail(), loginDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("User with %s not found", loginDto.getEmail())));
        } catch (Exception e) {
            log.info("Authentication failed for user: {}", loginDto.getEmail());
            throw new ForbiddenException(BAD_CREDENTIALS_MESSAGE);
        }
    }

    private String generateAccessToken(String email, String username) {
        Map<String, Object> claims = generateUserClaims(email);
        return jwtService.generateTokenWithClaims(username, claims);
    }

    private TokenEntity saveToken(UserEntity user, String token) {
        TokenEntity tokenEntity = TokenEntity.builder()
                .userEntity(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        return tokenRepository.save(tokenEntity);
    }

    private ResponseDto<MfaDto> buildLoginResponse(String token) {
        return ResponseDto.<MfaDto>builder()
                .statusCode(STATUS_CODE_SUCCESS)
                .status(true)
                .message(LOGIN_SUCCESS_MESSAGE)
                .data(MfaDto.builder()
                        .isLogin(true)
                        .token(token)
                        .build())
                .build();
    }

    private void processRefreshToken(String refreshToken, String username, HttpServletResponse response) throws IOException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserEntity user = findUserByUsername(userDetails.getUsername());

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            String accessToken = generateAccessToken(user.getEmail(), user.getUserName());
            revokeValidTokens(user);
            saveToken(user, accessToken);

            AuthenticationResponseDto authResponse = buildRefreshTokenResponse(user, accessToken, refreshToken);
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }

    private AuthenticationResponseDto buildRefreshTokenResponse(UserEntity user, String accessToken, String refreshToken) {
        return AuthenticationResponseDto.builder()
                .message(REFRESH_TOKEN_SUCCESS_MESSAGE)
                .userId(user.getId())
                .authentication(Authentication.REFRESH_TOKEN)
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with %s not found", email)));
    }

    private UserEntity findUserByUsername(String username) {
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Show yourself if you are the problem"));
    }

    private void validateNewPassword(String newPassword, String currentPasswordHash) {
        if (passwordEncoder.matches(newPassword, currentPasswordHash)) {
            throw new BadRequestException("Can't use old password");
        }
    }

    private void updateUserPassword(UserEntity user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private VerificationToken validateResetToken(String token) {
        return verificationTokenRepository.findByVerificationTokenAndExpired(token, false)
                .orElseThrow(() -> new BadRequestException("Token expired. Resend email"));
    }

    private void validatePasswordReset(String newPassword, String confirmPassword, String currentPasswordHash) {
        if (!isInputValid(newPassword)) {
            throw new BadRequestException(WRONG_PASSWORD_FORMAT);
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new BadRequestException("Both passwords do not match");
        }
        if (passwordEncoder.matches(newPassword, currentPasswordHash)) {
            throw new BadRequestException("Can't use your old password");
        }
    }

    private ResponseDto<String> buildPasswordChangeResponse(String userId) {
        return ResponseDto.<String>builder()
                .statusCode(STATUS_CODE_SUCCESS)
                .status(true)
                .message(PASSWORD_CHANGED_MESSAGE)
                .data(String.format("Password changed for User with Id: %s", userId))
                .build();
    }

    private ResponseDto<String> buildPasswordResetResponse(String userId) {
        return ResponseDto.<String>builder()
                .statusCode(STATUS_CODE_SUCCESS)
                .status(true)
                .message(PASSWORD_RESET_MESSAGE)
                .data(String.format("Password reset for User with Id: %s", userId))
                .build();
    }

    private void logAuditEvent(UserEntity user, Events event) {
        auditLogService.registerLogToAudit(AuditLogRequestDto.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .merchantId(null)
                .merchantName(null)
                .userType(String.valueOf(user.getUsertype()))
                .event(event.name())
                .build());
    }

    private void revokeValidTokens(UserEntity user) {
        List<TokenEntity> tokenEntities = tokenRepository.findAllValidTokensByUserId(user.getId());
        if (tokenEntities.isEmpty()) return;

        tokenEntities.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(tokenEntities);
    }

    private boolean isInputValid(String input) {
        return Pattern.compile(PASSWORD_REGEX).matcher(input).matches();
    }

    private Map<String, Object> generateUserClaims(String username) {
        UserEntity user = findUserByUsername(username);
        List<String> userRoles = extractUserRoles(user);
        List<String> userPermissions = extractUserPermissions(user);
        Optional<OrganizationEntity> organizationEntity = organizationRepository.findOrganizationByMerchantAdminId(user.getId());

        Map<String, Object> claims = new HashMap<>();

        if (isSuperAdminOrRedtechStaff(user)) {
            populateSuperAdminClaims(claims, user, userRoles, userPermissions);
        } else {
            assert  organizationEntity.isPresent();
            populateRegularUserClaims(claims, user, userRoles, userPermissions, organizationEntity.get());
        }

        return claims;
    }

    private List<String> extractUserRoles(UserEntity user) {
        return user.getRoleEntities().stream()
                .map(RoleEntity::getRoleName)
                .toList();
    }

    private List<String> extractUserPermissions(UserEntity user) {
        return user.getRoleEntities().stream()
                .flatMap(roleEntity -> roleEntity.getPermissions().stream())
                .map(PermissionEntity::getName)
                .toList();
    }

    private boolean isSuperAdminOrRedtechStaff(UserEntity user) {
        return user.getUsertype().equals(UserType.SUPER_ADMIN) ||
                user.getUsertype().equals(UserType.REDTECH_STAFF);
    }

    private void populateSuperAdminClaims(Map<String, Object> claims, UserEntity user,
                                          List<String> userRoles, List<String> userPermissions) {
        populateBasicClaims(claims, user, userRoles, userPermissions);
        claims.put("merchantPrefix", organizationId);
        claims.put("organization", organizationName);
        claims.put("organizationId", organizationId);
    }

    private void populateRegularUserClaims(Map<String, Object> claims, UserEntity user,
                                           List<String> userRoles, List<String> userPermissions,
                                           OrganizationEntity organization) {
        populateBasicClaims(claims, user, userRoles, userPermissions);
        claims.put("merchantPrefix", organization.getProductPrefix());
        claims.put("organization", organization.getOrganizationName());
        claims.put("organizationId", organization.getId());
    }

    private void populateBasicClaims(Map<String, Object> claims, UserEntity user,
                                     List<String> userRoles, List<String> userPermissions) {
        claims.put("roles", userRoles);
        claims.put("userName", user.getUserName());
        claims.put("userType", String.valueOf(user.getUsertype()));
        claims.put("permissions", userPermissions);
        claims.put("email", user.getEmail());
    }
}