package com.bbng.dao.util.email.impl;


import com.bbng.dao.microservices.auth.organization.entity.SystemConfigEntity;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.organization.repository.SystemConfigRepository;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.util.email.dto.request.EmailRequestDTO;
import com.bbng.dao.util.email.dto.request.MailStructure;
import com.bbng.dao.util.email.dto.request.Message;
import com.bbng.dao.util.email.dto.request.To;
import com.bbng.dao.util.email.dto.response.EmailResponseDto;
import com.bbng.dao.util.email.entity.VerificationToken;
import com.bbng.dao.util.email.repository.VerificationTokenRepository;
import com.bbng.dao.util.email.service.EmailService;
import com.bbng.dao.util.email.service.EmailVerificationService;
import com.bbng.dao.util.email.service.JavaMailService;
import com.bbng.dao.util.email.utils.Utils;
import com.bbng.dao.util.exceptions.customExceptions.BadRequestException;
import com.bbng.dao.util.fileUpload.entity.HeaderLogoEntity;
import com.bbng.dao.util.fileUpload.repository.HeaderLogoRepository;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static com.bbng.dao.util.email.utils.Utils.*;


@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  OrganizationRepository organizationRepository;
    @Autowired
    private  JavaMailService javaMailService;
    @Autowired
    private  SystemConfigRepository systemConfigRepository;
    @Autowired
    private HeaderLogoRepository headerLogoRepository;


    @Value("${base.url}")
    private String baseUrl;

    @Value("${redtech.url}")
    private String redtechUrl;

    @Override
    public EmailResponseDto[] sendVerificationEmail(String toEmail) {
        UserEntity userEntity = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new BadRequestException("User with email: " + toEmail + " not found"));

        if (userEntity != null && userEntity.getIsEnabled()) {
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(userEntity.getEmail())
                    .status("User had been verified already, proceed to login")
                    .userId(userEntity.getId())
                    .rejectReason("User had been verified already")
                    .queuedReason(null).build()};
        }

        if (userEntity == null) {
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("User does not exist. Verification email not sent.")
                    .userId(null)
                    .rejectReason("User does not exist")
                    .queuedReason(null).build()};
        }

        VerificationToken token = generateVerificationToken();
        log.info("Generated verification token: {}", token.getVerificationToken());
        token.setEmail(toEmail);
        verificationTokenRepository.save(token);
        //https://app.redtechlimited.com/reset-password?token=d9718062-10ad-4234-a56f-2245d2b92dd5
        String verificationLink = redtechUrl + "register/verify-email?token="  + token.getVerificationToken() + "&email=" + toEmail;
//        EmailRequestDTO emailRequest = buildVerificationEmailRequest(toEmail, verificationLink, userEntity.getFirstName());
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        MailStructure mailStructure = MailStructure.builder()
                .subject("Email Verification")
                .htmlContent(verifyEmailHtmlContent(userEntity.getFirstName(), verificationLink, logo.getUrl()))
                .build();
        ResponseEntity<EmailResponseDto[]> responseEntity = null;
        try {
            javaMailService.sendGridHtmlContent(userEntity.getEmail(), mailStructure, null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending verification email.")
                    .userId(null)
                    .rejectReason("Email sending error")
                    .queuedReason(null).build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Verification Email Sent.")
                .userId(userEntity.getId())
                .build()};
    }

    @Override
    public EmailResponseDto[] sendForgotPasswordMail(String toEmail) {

        UserEntity userEntity = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new BadRequestException("User with email: " + toEmail + " not found"));


        VerificationToken token = generateVerificationToken();
        log.info("verification token: {}", token.getVerificationToken());
        token.setEmail(toEmail);
        verificationTokenRepository.save(token);
        String verificationLink = redtechUrl + "reset-password?token=" + token.getVerificationToken();
//        EmailRequestDTO emailRequest = buildEmailRequest(userEntity, fromName, verificationLink);
//        ResponseEntity<EmailResponseDto[]> responseEntity = null;
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        MailStructure mailStructure = MailStructure.builder()
                .subject("Forgot Password Email")
                .htmlContent(resetPasswordHtmlContent(userEntity.getFirstName(), verificationLink, logo.getUrl()))
                .build();
        try {
            javaMailService.sendGridHtmlContent(userEntity.getEmail(), mailStructure, null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending forgot password email.")
                    .rejectReason("Email sending error")
                    .build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Forgot password Email Sent.")
                .userId(userEntity.getId())
                .build()};
    }

    public ResponseDto<String> verifyEmail(String verificationToken) {
        if (verificationToken == null) {
            throw new BadRequestException("Invalid verification token");
        }
        VerificationToken token = verificationTokenRepository
                .findByVerificationTokenAndExpired(verificationToken, false)
                .orElseThrow(() -> new BadRequestException("Token expired"));

        if (isTokenExpired(token.getVerificationToken(), token.getExpirationTime())) {
            token.setExpired(true);
            throw new BadRequestException("Token expired. Resend email");
        }

        UserEntity userEntity = userRepository.findByEmail(token.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));


        userEntity.setIsEnabled(true);
        token.setExpired(true);
        verificationTokenRepository.save(token);
        UserEntity user = userRepository.save(userEntity);

        systemConfigRepository.save(SystemConfigEntity.builder()
                .is2FA(false)
                .isEmailSetUp(true)
                .isSmsSetUp(false)
                .userId(userEntity.getId())
                .build());

        return ResponseDto.<String>builder()
                .statusCode(200)
                .status(true)
                .message("Email Verified successfully")
                .data(String.format("Email verified for User with Id: %s", userEntity.getId()))
                .build();
    }

    @Override
    public EmailResponseDto[] sendInvitationEmail(String toEmail, String fromOrganization, String password) {
        UserEntity userEntity = userRepository.findByEmail(toEmail)
                .orElseThrow(() -> new BadRequestException("User with email: " + toEmail + " not found"));

        if (userEntity != null && userEntity.getIsEnabled()) {
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(userEntity.getEmail())
                    .status("User had been verified already, proceed to login")
                    .userId(userEntity.getId())
                    .rejectReason("User had been verified already")
                    .queuedReason(null).build()};
        }

        if (userEntity == null) {
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("User does not exist. Invitation email not sent.")
                    .userId(null)
                    .rejectReason("User does not exist")
                    .queuedReason(null).build()};
        }
        VerificationToken token = generateVerificationToken();
        log.info("Generated verification token: {}", token.getVerificationToken());
        token.setEmail(toEmail);
        verificationTokenRepository.save(token);
        String verificationLink = redtechUrl + "api/v1/redtech/verify-email?token=" + token.getVerificationToken();

        MailStructure mailStructure = MailStructure.builder()
                .subject("Invitation mail")
                .htmlContent(invitationEmailHtmlContent(userEntity.getFirstName(), fromOrganization, verificationLink, password))
                .build();

        ResponseEntity<EmailResponseDto[]> responseEntity = null;

        try {
            javaMailService.sendGridHtmlContent(userEntity.getEmail(), mailStructure, null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending verification email.")
                    .userId(null)
                    .rejectReason("Email sending error")
                    .queuedReason(null).build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Verification Email Sent.")
                .userId(userEntity.getId())
                .build()};
    }


    @Override
    public EmailResponseDto[] sendWalletTopUpEmail(String toEmail, String transactionId, String organizationName,
                                                   String userName, String userEmail, String userId, BigDecimal amount) {


        MailStructure mailStructure = MailStructure.builder()
                .subject("Wallet Deposit notification")
                .htmlContent(actionWalletTopupHtmlContent(transactionId, userId, userName, organizationName, amount))
                .build();

        try {
            javaMailService.sendGridHtmlContent(toEmail, mailStructure, null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending Deposit Notification email.")
                    .userId(null)
                    .rejectReason("Email sending error")
                    .queuedReason(null).build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Deposit Notification Email Sent.")
                .userId(userId)
                .build()};

    }

    @Override
    public EmailResponseDto[] sendWalletWithdrawalEmail(String toEmail, String transactionId, String organizationName, String userName, String userEmail, String userId, BigDecimal amount) {
        MailStructure mailStructure = MailStructure.builder()
                .subject("Wallet Withdrawal notification")
                .htmlContent(actionWalletWithdrawalHtmlContent(transactionId, userId, userName, organizationName, amount))
                .build();

        try {
            javaMailService.sendGridHtmlContent(toEmail, mailStructure, null);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending Withdrawal Notification email.")
                    .userId(null)
                    .rejectReason("Email sending error")
                    .queuedReason(null).build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Withdrawal Notification Email Sent.")
                .userId(userId)
                .build()};
    }

    @Override
    public EmailResponseDto[] sendWalletTopUpVerificationEmail(String toEmail, String userId, String walletTransactionId, String organizationName,
                                                               String userName, String businessEmail, String accountNumber,
                                                               String accountName, BigDecimal amount, Instant transactionDate) {

        MailStructure mailStructure = MailStructure.builder()
                .subject("Wallet Deposit notification")
                .htmlContent(actionAdminWalletTopupHtmlContent(walletTransactionId, organizationName, businessEmail, userName,
                        accountNumber, accountName, amount, transactionDate))
                .build();

        try {
            javaMailService.sendGridHtmlContent(toEmail, mailStructure, null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending Deposit Notification email.")
                    .userId(null)
                    .rejectReason("Email sending error")
                    .queuedReason(null).build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Deposit Notification Email Sent.")
                .userId(userId)
                .build()};

    }

    @Override
    public EmailResponseDto[] sendWalletTopUpConfirmEamilToAdmin(String messageType, String toEmail, String userId, String transactionId, String organizationId,
                                                                 String organizationName, BigDecimal amount, String accountNumber,
                                                                 String accountName, String userName, Instant isConfirmedDate) {

        MailStructure mailStructure = MailStructure.builder()
                .subject("Wallet Deposit notification")
                .htmlContent(confirmationAdminWalletTopupHtmlContent(messageType, transactionId, organizationId, organizationName,
                           amount, accountNumber, accountName, userName, isConfirmedDate))
                .build();

        try {
            javaMailService.sendGridHtmlContent(toEmail, mailStructure, null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending Deposit Confirmation Notification email.")
                    .userId(null)
                    .rejectReason("Email sending error")
                    .queuedReason(null).build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Deposit Confirmation Notification Email Sent.")
                .userId(userId)
                .build()};

    }

    @Override
    public EmailResponseDto[] sendWalletTopUpConfirmEamilToOrganization(String messageType, String toEmail, String userId, String transactionId,
                                                                        String organizationId, String organizationName,
                                                                        BigDecimal amount, String accountNumber, String accountName,
                                                                        String userName, Instant isConfirmedDate) {
        MailStructure mailStructure = MailStructure.builder()
                .subject("Wallet Deposit notification")
                .htmlContent(confirmationOrganizationWalletTopupHtmlContent(messageType, transactionId, organizationId, organizationName,
                        amount, accountNumber, accountName, userName, isConfirmedDate))
                .build();

        try {
            javaMailService.sendGridHtmlContent(toEmail, mailStructure, null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending Deposit Confirmation Notification email.")
                    .userId(null)
                    .rejectReason("Email sending error")
                    .queuedReason(null).build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Deposit Confirmation Notification Email Sent.")
                .userId(userId)
                .build()};
    }

    @Override
    public EmailResponseDto[] sendDebitAlertEmail(String toEmail, String userId, String organizationName,
                                                  String organizationId, String customerName,
                                                  BigDecimal amount, String transactionDate,
                                                  String billType, String transactionId,
                                                  BigDecimal currentBal) {
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        MailStructure mailStructure = MailStructure.builder()
                .subject("Wallet Debit notification")
                .htmlContent(debitAlertHtmlContent(organizationName,
                        organizationId, customerName,
                        amount, transactionDate,
                        billType, transactionId,
                        currentBal, logo.getUrl()))
                .build();

        try {
            javaMailService.sendGridHtmlContent(toEmail, mailStructure, null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending Debit  Notification email.")
                    .userId(null)
                    .rejectReason("Email sending error")
                    .queuedReason(null).build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Deposit Confirmation Notification Email Sent.")
                .userId(userId)
                .build()};
    }

    @Override
    public EmailResponseDto[] sendCreditAlertEmail(String toEmail, String userId, String organizationName, String organizationId,
                                                   String customerName, BigDecimal amount, String transactionDate, String billType,
                                                   String transactionId, BigDecimal currentBal,BigDecimal billAmount, String message) {
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        MailStructure mailStructure = MailStructure.builder()
                .subject("Wallet Credit notification")
                .htmlContent(creditAlertHtmlContent(organizationName, organizationId,
                         customerName,  amount, transactionDate, billType,
                         transactionId, currentBal, billAmount,  message, logo.getUrl()))
                .build();

        try {
            javaMailService.sendGridHtmlContent(toEmail, mailStructure, null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending mail {}", e.getMessage());
            return new EmailResponseDto[]{EmailResponseDto.builder()
                    .email(toEmail)
                    .status("Error sending Credit Notification email.")
                    .userId(null)
                    .rejectReason("Email sending error")
                    .queuedReason(null).build()};
        }

        return new EmailResponseDto[]{EmailResponseDto.builder()
                .email(toEmail)
                .status("Deposit Confirmation Notification Email Sent.")
                .userId(userId)
                .build()};
    }

    @Override
    public void sendWelcomeEmail(String to, String fullName) {
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        String htmlContent = Utils.welcomeEmailHtml(fullName, logo.getUrl());
        try {
            javaMailService.sendGridHtmlContent(to, MailStructure.builder()
                    .htmlContent(htmlContent)
                    .subject("Welcome Email")
                    .build(), null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending welcoming Mail {}", e.getMessage());

        }

    }

    @Override
    public void sendKycEmail(String contactEmail, String organizationName) {
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        String headerLogo = logo.getUrl();
        String htmlContent = Utils.buildKycRequestReceivedEmail(headerLogo, organizationName);
        try {
            javaMailService.sendGridHtmlContent(contactEmail, MailStructure.builder()
                    .htmlContent(htmlContent)
                    .subject("Kyc Verification Request")
                    .build(), null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending  Kyc Verification Request{}", e.getMessage());

        }
    }

    @Override
    public void sendKycVerificationMail(String email, String customerName) {
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
       String htmlContent = Utils.buildKycSuccessfulmessage(logo.getUrl(), customerName);
        try {
            javaMailService.sendGridHtmlContent(email, MailStructure.builder()
                    .htmlContent(htmlContent)
                    .subject("Kyc Successful verification request")
                    .build(), null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending  Kyc Successful verification  Request{}", e.getMessage());

        }

    }

    @Override
    public void sendKycRejectionMail(String email, String customer) {
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        String htmlContent = Utils.buildKycRejectionmessage(logo.getUrl(), customer);
        try {
            javaMailService.sendGridHtmlContent(email, MailStructure.builder()
                    .htmlContent(htmlContent)
                    .subject("Kyc Successful verification request")
                    .build(), null);
//            responseEntity = emailService.sendSimpleMail(emailRequest);
        } catch (Exception e) {
            log.info("Error sending  Kyc Rejection  Request{}", e.getMessage());

        }
    }

    private VerificationToken generateVerificationToken() {
        Instant creationTime = Instant.now();
        Instant expirationTime = creationTime.plusSeconds(600);
        String verificationToken = UUID.randomUUID().toString();
        return verificationTokenRepository.save(VerificationToken.builder()
                .creationTime(creationTime)
                .expirationTime(expirationTime)
                .verificationToken(verificationToken)
                .expired(false)
                .build());
    }

    private EmailRequestDTO buildVerificationEmailRequest(String toEmail, String verificationLink, String firstName) {
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        String htmlContent = verifyEmailHtmlContent(firstName, verificationLink, logo.getUrl());

        return EmailRequestDTO.builder()
                .emailType("verification")
                .async("false")
                .message(Message.builder()
                        .fromName("i-Apply")
                        .to(Collections.singletonList(To.builder().email(toEmail).name("Recipient").type("to").build()))
                        .subject("Email Verification")
                        .text("Email Verification")
                        .html(htmlContent)
                        .build())
                .build();
    }

    private boolean isTokenExpired(String token, Instant expirationTime) {
        return expirationTime.isBefore(Instant.now());
    }

    private EmailRequestDTO buildEmailRequest(UserEntity user, String fromName, String verificationLink) {
        return EmailRequestDTO.builder()
                .emailType("verification")
                .async("false")
                .message(message(user.getEmail(), verificationLink, fromName, user.getFirstName()))
                .build();
    }

    private Message message(String toEmail, String verificationLink, String fromName, String toName) {
        log.info("verfiication link: {}", verificationLink);
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        String htmlContent = Utils.resetPasswordHtmlContent(toName, verificationLink, logo.getUrl());
        return Message.builder()
                .fromName(fromName)
                .to(Collections.singletonList(To.builder().email(toEmail).name(toName).type("to").build()))
                .subject("Reset Password")
                .text("Click the following link to reset your password: " + verificationLink)
                .html(htmlContent)
                .build();
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
