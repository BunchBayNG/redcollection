package com.bbng.dao.util.email.service;




import com.bbng.dao.util.email.dto.response.EmailResponseDto;
import com.bbng.dao.util.response.ResponseDto;

import java.math.BigDecimal;
import java.time.Instant;


public interface EmailVerificationService {
    EmailResponseDto[] sendVerificationEmail(String toEmail);
    EmailResponseDto[] sendForgotPasswordMail(String toEmail);
    ResponseDto<String> verifyEmail(String verificationToken);
    EmailResponseDto[] sendInvitationEmail(String toEmail, String fromOrganization, String generatedPassword);

    EmailResponseDto[] sendWalletTopUpEmail(String toEmail, String transactionId, String organizationName, String userName, String userEmail, String userId, BigDecimal amount);

    EmailResponseDto[] sendWalletWithdrawalEmail(String toEmail, String transactionId, String organizationName, String userName, String userEmail, String userId, BigDecimal amount);

    EmailResponseDto[] sendWalletTopUpVerificationEmail(String email, String userId, String walletTransactionId,
                                                        String organizationName, String userName, String email1,
                                                        String accountNumber, String accountName, BigDecimal amount, Instant transactionDate);

    EmailResponseDto[] sendWalletTopUpConfirmEamilToAdmin(String messageType, String email, String userId, String transactionId, String organizationId, String organizationName, BigDecimal amount,
                                            String accountNumber, String accountName, String userName, Instant isConfirmedDate);

    EmailResponseDto[] sendWalletTopUpConfirmEamilToOrganization(String messageType, String contactEmail,String userId, String transactionId, String organizationId, String organizationName, BigDecimal amount,
                                                                 String accountNumber, String accountName, String userName, Instant isConfirmedDate);

    EmailResponseDto[] sendDebitAlertEmail(String toEmail, String userId, String organizationName, String organizationId, String customerName, BigDecimal amount,
                                           String transactionDate, String billType, String transactionId, BigDecimal currentBal);

    EmailResponseDto[] sendCreditAlertEmail(String toEmail, String userId, String organizationName, String organizationId, String customerName, BigDecimal amount,
                                           String transactionDate, String billType, String transactionId, BigDecimal currentBal,
                                            BigDecimal billAmount, String message);
    void sendWelcomeEmail(String to, String fullName);

    void sendKycEmail(String contactEmail, String organizationName);

    void sendKycVerificationMail(String email, String customerName);

    void sendKycRejectionMail(String email, String customer);
}

