package com.bbng.dao.util.email.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class Utils {
    public static String resetPasswordHtmlContent(String toName, String verificationLink, String headerLogo) {

        String img = "<img src=\"" + headerLogo + "\" alt=\"redtech Logo\" style=\"width: 150px;\">";

        String htmlContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>Password Reset Email</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #F4F7FA; margin: 0; padding: 0; }" +
                ".email-container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; " +
                "border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; }" +
                ".header { padding: 20px; background-color: #E8F4FF; " +
                "background-image: url('background-pattern.png'); background-repeat: no-repeat; " +
                "background-size: cover; text-align: center; }" +
                ".header img { height: 50px; }" +
                ".content { padding: 20px; }" +
                ".content h2 { color: #1a73e8; font-size: 18px; }" +
                ".content p { color: #555; line-height: 1.6; }" +
                ".content a { color: #0073E6; text-decoration: none; }" +
                ".content a:hover { text-decoration: underline; }" +
                ".reset-button { display: inline-block; padding: 10px 20px; margin-top: 20px; color: white; " +
                "background-color: #0073E6; color: #ffffff; font-weight: bold; text-decoration: none; " +
                "border-radius: 5px; }" +
                ".footer { text-align: center; padding: 20px; background-color: #F4F7FA; " +
                "font-size: 12px; color: #888; }" +
                ".footer img { width: 24px; margin: 0 5px; vertical-align: middle; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +

                // Header Section
                "<div class=\"header\">" +
                img + // Replace with your logo URL
                "</div>" +

                // Content Section
                "<div class=\"content\">" +
                "<p>Hi " + toName + ",</p>" +
                "<h2>Recalling passwords can be stressful</h2>" +
                "<p>You have been sent this email because we received a request to reset the password to your account.</p>" +
                "<p>To do this, please click the button below to reset your password.</p>" +
                "<a href=\"" + verificationLink + "\" class=\"reset-button\">Reset Password</a>" +
                "<p>If you did not request a password reset, please report this email to redtech.support@bunchbay.com.</p>" +
                "</div>" +

                // Footer Section
                "<div class=\"footer\">" +
                "<p>&copy; redtech 2025. All rights reserved</p>" +
                "<a href=\"#\"><img src=\"icon-whatsapp.png\" alt=\"WhatsApp\"></a>" +
                "<a href=\"#\"><img src=\"icon-linkedin.png\" alt=\"LinkedIn\"></a>" +
                "<a href=\"#\"><img src=\"icon-facebook.png\" alt=\"Facebook\"></a>" +
                "<a href=\"#\"><img src=\"icon-instagram.png\" alt=\"Instagram\"></a>" +
                "<a href=\"#\"><img src=\"icon-email.png\" alt=\"Email\"></a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return htmlContent;

    }

    public static String verifyEmailHtmlContent(String firstName, String verificationLink, String headerLogo) {

        String emailContent = "<div>" +
// Header Section
                "<div style=\"background: url('https://i.ibb.co/9ZYtMGv/olena-bohovyk-d-IMJWLx1-Yb-E-unsplash-1-1.png') no-repeat center / cover; " +
                "width: 100%; max-width: 59rem; margin: auto; min-height: 200px;\">" +
                "<img style=\"padding-left: 50px; padding-top: 20px;\" src=\"https://your-website.com/assets/images/Group.svg\" alt=\"\">" +
                "<h1 style=\"color: #fff; font-weight: 700; font-size: 50px; text-align: center; padding-top: 30px\">" +
                "Hello " + firstName + "</h1>" +
                "</div>" +

// Main Content
                "<div style=\"text-align: center;\">" +
                "<p style=\"width: fit-content; margin: auto; max-width: 29.5rem; text-align: center; font-weight: 400; " +
                "font-size: 18px; line-height: 25px; font-family: Inter, sans-serif; color: black;\">" +
                "We have received a request to verify your account. To verify your account, please click on the link below." +
                "</p>" +

// Verification Button
                "<div style=\"margin-top: 20px;\">" +
                "<a href=\"" + verificationLink + "\" " +
                "style=\"display: inline-block; background-color: #1A56DB; color: white; text-decoration: none; " +
                "padding: 12px 20px; border-radius: 8px; font-size: 16px;\">" +
                "Click to Verify Email" +
                "</a>" +
                "</div>" +

// Alternative Link
                "<p style=\"width: fit-content; margin: auto; max-width: 29.5rem; text-align: center; font-weight: 400; " +
                "font-size: 18px; line-height: 25px; font-family: Inter, sans-serif; color: black;\">" +
                "Alternatively, copy this link and paste it in your browser:<br>" +
                "<a href=\"" + verificationLink + "\" style=\"color: #1A56DB;\">" + verificationLink + "</a>" +
                "</p>" +

// Expiry Notice
                "<p style=\"width: fit-content; margin: auto; margin-top: 14px; max-width: 29.5rem; text-align: center; " +
                "font-weight: 400; font-size: 18px; line-height: 25px; font-family: Inter, sans-serif; color: black;\">" +
                "Please note that this link is only valid for 10 minutes. If you do not verify your account within this time frame, you will need to submit another request." +
                "</p>" +

// Security Notice
                "<p style=\"width: fit-content; margin: auto; max-width: 29.5rem; margin-top: 14px; text-align: center; " +
                "font-weight: 400; font-size: 18px; line-height: 25px; font-family: Inter, sans-serif; color: black;\">" +
                "If you did not initiate this request, please disregard this email and contact us immediately. We take the security of our users very seriously and want to ensure that your account remains safe." +
                "</p>" +
                "</div>" +

// Footer Section
                "<div style=\"background: rgba(217, 217, 217, 0.50); display: flex; margin-bottom: 40px; flex-direction: column; " +
                "gap: 20px; justify-content: center; align-items: center; width: 100%; max-width: 59rem; min-height: 152px; " +
                "margin: auto; margin-top: 2rem;\">" +

// Social Media Icons
                "<div style=\"display: flex; align-items: center; gap: 10px; padding-top: 30px;\">" +
                "<img src=\"https://your-website.com/assets/images/mdi_instagram.svg\" alt=\"Instagram\" style=\"width: 24px;\">" +
                "<img src=\"https://your-website.com/assets/images/ri_twitter-line.svg\" alt=\"Twitter\" style=\"width: 24px;\">" +
                "<img src=\"https://your-website.com/assets/images/mingcute_linkedin-line.svg\" alt=\"LinkedIn\" style=\"width: 24px;\">" +
                "</div>" +

// Footer Text
                "<div style=\"display: flex; align-items: center; gap: 10px; line-height: 0px; margin-bottom: 0px;\">" +
                "<img style=\"width: 15px;\" src=\"https://your-website.com/assets/images/Group1000005194.svg\" alt=\"Logo\">" +
                "<p style=\"color: rgba(31, 31, 31, 0.50);\">Registration Portal 2023</p>" +
                "</div>" +
                "</div>" +
                "</div>";

        return emailContent;
    }

    public static String invitationEmailHtmlContent(String inviteeName, String organizationName, String verificationLink, String generatedPassword) {
        return "<div>\n" +
                "    <div style=\"background: url('https://i.ibb.co/9ZYtMGv/olena-bohovyk-d-IMJWLx1-Yb-E-unsplash-1-1.png') no-repeat center / cover; width: 100%; max-width: 59rem; margin: auto; min-height: 200px\">\n" +
                "        <img style=\"padding-left: 50px; padding-top: 20px;\" src=\"../../assets/images/Group.svg\" alt=\"\">\n" +
                "        <h1 style=\"color: #fff; font-weight: 700; font-size: 50px; text-align: center; padding-top: 30px\">\n" +
                "            Hello " + inviteeName + "\n" +
                "        </h1>\n" +
                "    </div>\n" +
                "    <div style=\"text-align: center;\">\n" +
                "        <div style=\"padding-top: 20px; width: fit-content; margin: auto;\">\n" +
                "            <img src=\"../../assets/images/Key Folder.svg\" alt=\"\">\n" +
                "        </div>\n" +
                "        <div style=\"padding: 10px 0; width: fit-content; margin: auto\">\n" +
                "            <img src=\"../../assets/images/Screenshot 2023-11-15 at 09.13 1.svg\" alt=\"\">\n" +
                "        </div>\n" +
                "        <p style=\"width: fit-content; margin: auto; max-width: 29.5rem; text-align: center; font-weight: 400; font-size: 18px; line-height: 25px; font-family: inter; color: black;\">\n" +
                "            You have been invited by " + organizationName + " to join our platform. Below are your login details:\n" +
                "        </p>\n" +
                "        <p style=\"width: fit-content; margin: auto; max-width: 29.5rem; text-align: center; font-weight: 400; font-size: 18px; line-height: 25px; font-family: inter; color: black;\">\n" +
                "            <strong>Generated Password:</strong> " + generatedPassword + "\n" +
                "        </p>\n" +
                "        <p style=\"width: fit-content; margin: auto; max-width: 29.5rem; text-align: center; font-weight: 400; font-size: 18px; line-height: 25px; font-family: inter; color: black;\">\n" +
                "            To verify your email and complete the registration, please click on the verification link below:\n" +
                "        </p>\n" +
                "        <a href=\"" + verificationLink + "\" style=\"border: 1px solid #1A56DB; background: #1A56DB; color: white; text-decoration: none; margin-top: 14px; border-radius: 8px; padding: 12px 20px; display: block;\">\n" +
                "            Verify Email\n" +
                "        </a>\n" +
                "        <p style=\"width: fit-content; margin: auto; max-width: 29.5rem; text-align: center; font-weight: 400; font-size: 18px; line-height: 25px; font-family: inter; color: black;\">\n" +
                "            Alternatively, copy and paste this link into your browser:\n" +
                "            <br><a href=\"" + verificationLink + "\">" + verificationLink + "</a>\n" +
                "        </p>\n" +
                "        <p style=\"width: fit-content; margin: auto; margin-top: 14px; max-width: 29.5rem; text-align: center; font-weight: 400; font-size: 18px; line-height: 25px; font-family: inter; color: black;\">\n" +
                "            Please note that this link is only valid for 10 minutes. If you do not verify your account within this time frame, you will need to submit another request.\n" +
                "        </p>\n" +
                "        <p style=\"width: fit-content; margin: auto; max-width: 29.5rem; margin-top: 14px; text-align: center; font-weight: 400; font-size: 18px; line-height: 25px; font-family: inter; color: black;\">\n" +
                "            If you did not initiate this request, please disregard this email and contact us immediately. We take the security of our users very seriously and want to ensure that your account remains safe.\n" +
                "        </p>\n" +
                "    </div>\n" +
                "    <div style=\"background: rgba(217, 217, 217, 0.50); display: flex; margin-bottom: 40px; flex-direction: column; gap: 20px; justify-content: center; align-items: center; width: 100%; max-width: 59rem; min-height: 152px; margin: auto; margin-top: 2rem;\">\n" +
                "        <div style=\"display: flex; align-items: center; gap: 10px; padding-top: 30px;\">\n" +
                "            <img src=\"../../assets/images/mdi_instagram.svg\" alt=\"\">\n" +
                "            <img src=\"../../assets/images/ri_twitter-line.svg\" alt=\"\">\n" +
                "            <img src=\"../../assets/images/mingcute_linkedin-line.svg\" alt=\"\">\n" +
                "        </div>\n" +
                "        <div style=\"display: flex; align-items: center; gap: 10px; line-height: 0px; margin-bottom: 0px;\">\n" +
                "            <img style=\"width: 15px;\" src=\"../../assets/images/Group 1000005194.svg\" alt=\"\">\n" +
                "            <p style=\"color: rgba(31, 31, 31, 0.50);\">iApply 2023</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
    }

    public static String actionWalletTopupHtmlContent(String transactionId, String customerId, String customerName, String organizationName,
                                                      BigDecimal amount) {
        return "<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" +
                "  <h2>Wallet Top Up</h2>\n" +
                "  <p>A top up transaction has been initiated for your wallet by <b>" + organizationName + "</b>.</p>\n" +
                "  <ul>\n" +
                "    <li>TransactionEntity ID: " + transactionId + "</li>\n" +
                "    <li>Customer ID: " + customerId + "</li>\n" +
                "    <li>Customer Name: " + customerName + "</li>\n" +
                "    <li>Organization Name: " + organizationName + "</li>\n" +
                "    <li>Amount: &#8358;" + amount.toString() + "</li>\n" +
                "    <li>Payment Status:    PENDING" + "</li>\n" +
                "  </ul>\n" +
                "</div>";
    }

    public static String confirmationAdminWalletTopupHtmlContent(String messageType, String walletTransactionId, String organizationId,
                                                                 String organizationName,
                                                                 BigDecimal amount,
                                                                 String accountNumber, String accountName, String userName, Instant confirmDate) {


        return String.format("<div style=\"background-color: #F5F5F5; padding: 20px; font-family: Arial, sans-serif;\">\n" +
                        "  <h2 style=\"color: #333;\">Wallet Top-Up Notification</h2>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">Hello Dear,</p>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">\n" +
                        "    A payment Deposit from <b>%s</b> has been %s by the Admin <b>%s</b> in our system. Please review the transaction details below:\n" +
                        "  </p>\n" +
                        "  <ul style=\"font-size: 16px; color: #555; list-style-type: none; padding: 0;\">\n" +
                        "    <li><strong>TransactionEntity ID:</strong> %s</li>\n" +
                        "    <li><strong>organization ID:</strong> %s</li>\n" +
                        "    <li><strong>Business Name:</strong> %s</li>\n" +
                        "    <li><strong>Confirmed By:</strong> %s</li>\n" +
                        "    <li><strong>Deposited To:</strong> %s</li>\n" +
                        "    <li><strong>Account Number:</strong> %s</li>\n" +
                        "    <li><strong>Amount:</strong> &#8358;%s</li>\n" +
                        "    <li><strong>Confirmation Date:</strong> &#8358;%s</li>\n" +
                        "  </ul>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">If you have any questions, feel free to reach out for assistance.</p>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">Best regards,<br>redtech<br>Head Administrator</p>\n" +
                        "</div>\n", organizationName, messageType, userName, walletTransactionId, organizationId, organizationName, userName,
                accountName, accountNumber, amount, confirmDate.toString());
    }


    public static String confirmationOrganizationWalletTopupHtmlContent(String messageType, String transactionId, String organizationId, String organizationName,
                                                                        BigDecimal amount, String accountNumber, String accountName, String userName,
                                                                        Instant isConfirmedDate) {


        return "<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" +
                "  <h2>Wallet Top Up</h2>\n" +
                "  <p>A top up transaction has been " + messageType + " for your wallet!!!.</p>\n" +
                "  <ul>\n" +
                "    <li>TransactionEntity ID: " + transactionId + "</li>\n" +
                "    <li>Organization ID: " + organizationId + "</li>\n" +
                "    <li>Customer Name: " + userName + "</li>\n" +
                "    <li>Organization Name: " + organizationName + "</li>\n" +
                "    <li>Amount: &#8358;" + amount.toString() + "</li>\n" +
                "    <li>Account Name:" + accountName + "</li>\n" +
                "    <li>Account Number:" + accountNumber + "</li>\n" +
                "    <li>Payment Status:" + messageType + "</li>\n" +
                "    <li>Confirmation Date:" + isConfirmedDate.toString() + "</li>\n" +
                "  </ul>\n" +
                "</div>";
    }


    public static String actionAdminWalletTopupHtmlContent(String walletTransactionId, String organizationName,
                                                           String businessEmail, String userName,
                                                           String accountNumber, String accountName, BigDecimal amount, Instant transactionDate) {
        return String.format("<div style=\"background-color: #F5F5F5; padding: 20px; font-family: Arial, sans-serif;\">\n" +
                        "  <h2 style=\"color: #333;\">Wallet Top-Up Notification</h2>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">Hello Dear,</p>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">\n" +
                        "    A payment Deposit has been initiated by the merchant <b>%s</b> in our system. Please review the transaction details below:\n" +
                        "  </p>\n" +
                        "  <ul style=\"font-size: 16px; color: #555; list-style-type: none; padding: 0;\">\n" +
                        "    <li><strong>TransactionEntity ID:</strong> %s</li>\n" +
                        "    <li><strong>Business Name:</strong> %s</li>\n" +
                        "    <li><strong>Business email:</strong> %s</li>\n" +
                        "    <li><strong>Customer Username:</strong> %s</li>\n" +
                        "    <li><strong>Transfer To:</strong> %s</li>\n" +
                        "    <li><strong>Account Number:</strong> %s</li>\n" +
                        "    <li><strong>Amount:</strong> &#8358;%s</li>\n" +
                        "    <li><strong>TransactionEntity Date:</strong> &#8358;%s</li>\n" +
                        "  </ul>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">\n" +
                        "    Please check your dashboard to verify and confirm the payment\n" +
                        "  </p>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">If you have any questions, feel free to reach out for assistance.</p>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">Best regards,<br>redtech<br>Head Administrator</p>\n" +
                        "</div>\n", userName, walletTransactionId, organizationName, businessEmail, userName, accountName,
                accountNumber, amount, transactionDate.toString());
    }

    public static String actionWalletWithdrawalHtmlContent(String transactionId, String customerId, String customerName,
                                                           String organizationName, BigDecimal amount) {
        return "<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" +
                "  <h2>Wallet Withdrawal</h2>\n" +
                "  <p>A withdrawal transaction has been initiated from your wallet by <b>" + organizationName + "</b>.</p>\n" +
                "  <ul>\n" +
                "    <li>TransactionEntity ID: " + transactionId + "</li>\n" +
                "    <li>Customer ID: " + customerId + "</li>\n" +
                "    <li>Customer Name: " + customerName + "</li>\n" +
                "    <li>Organization Name: " + organizationName + "</li>\n" +
                "    <li>Amount: &#8358;" + amount.toString() + "</li>\n" +
                "  </ul>\n" +
                "</div>";
    }

    public static String creditAlertHtmlContent(String organizationName, String organizationId,
                                                String customerName, BigDecimal amount, String transactionDate, String billType,
                                                String transactionId, BigDecimal currentBal,
                                                BigDecimal billAmount, String message, String headerLogo) {


        String img = "<img src=" + headerLogo + " alt=\"redtech Logo\">";

        String htmlContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>KYC Request Received</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #F4F7FA; margin: 0; padding: 0; }" +
                ".email-container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; " +
                "border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; }" +
                ".header { padding: 20px; background-color: #E8F4FF; " +
                "background-image: url('background-pattern.png'); background-repeat: no-repeat; " +
                "background-size: cover; text-align: center; }" +
                ".header img { height: 50px; }" +
                ".content { padding: 20px; }" +
                ".content p { color: #555; line-height: 1.6; }" +
                ".content h2 { color: #1a73e8; font-size: 18px; }" +
                ".footer { text-align: center; padding: 20px; background-color: #F4F7FA; " +
                "font-size: 12px; color: #888; }" +
                ".footer img { width: 24px; margin: 0 5px; vertical-align: middle; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +

                // Header Section
                "<div class=\"header\">" +
                img + // Replace with your logo URL
                "</div>" +

                // Content Section
                "<div class=\"content\">" +
                "<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" + "  <h2> " + message + " Credit Notification</h2>\n" + "  <p>A Credit transaction has been perform on your " + message + " .</p>\n" + "  <ul>\n" + "    <li>TransactionEntity ID: " + transactionId + "</li>\n" + "    <li>Organization ID: " + organizationId + "</li>\n" + "    <li>Customer Name: " + customerName + "</li>\n" + "    <li>Organization Name: " + organizationName + "</li>\n" + "    <li>Amount: &#8358;" + amount.toString() + "</li>\n" + "    <li>Bill Type: " + billType + "</li>\n" + "    <li>Bill Amount:  &#8358;" + billAmount.toString() + "</li>\n" + "    <li>Current Balance:  &#8358; " + currentBal.toString() + "</li>\n" + "    <li>TransactionEntity Date: " + transactionDate + "</li>\n" + "  </ul>\n" + "</div>" +
                "<p>Thank You,<br>redtech Team</p>" +
                "</div>" +

                // Footer Section
                "<div class=\"footer\">" +
                "<p>&copy; redtech 2024. All rights reserved</p>" +
                "<a href=\"#\"><img src=\"icon-whatsapp.png\" alt=\"WhatsApp\"></a>" +
                "<a href=\"#\"><img src=\"icon-linkedin.png\" alt=\"LinkedIn\"></a>" +
                "<a href=\"#\"><img src=\"icon-facebook.png\" alt=\"Facebook\"></a>" +
                "<a href=\"#\"><img src=\"icon-instagram.png\" alt=\"Instagram\"></a>" +
                "<a href=\"#\"><img src=\"icon-email.png\" alt=\"Email\"></a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return htmlContent;

    }

    public static String debitAlertHtmlContent(String organizationName,
                                               String organizationId, String customerName,
                                               BigDecimal amount, String transactionDate,
                                               String billType, String transactionId,
                                               BigDecimal currentBal, String headerLogo) {

        String img = "<img src=" + headerLogo + " alt=\"redtech Logo\">";

        String htmlContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>KYC Request Received</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #F4F7FA; margin: 0; padding: 0; }" +
                ".email-container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; " +
                "border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; }" +
                ".header { padding: 20px; background-color: #E8F4FF; " +
                "background-image: url('background-pattern.png'); background-repeat: no-repeat; " +
                "background-size: cover; text-align: center; }" +
                ".header img { height: 50px; }" +
                ".content { padding: 20px; }" +
                ".content p { color: #555; line-height: 1.6; }" +
                ".content h2 { color: #1a73e8; font-size: 18px; }" +
                ".footer { text-align: center; padding: 20px; background-color: #F4F7FA; " +
                "font-size: 12px; color: #888; }" +
                ".footer img { width: 24px; margin: 0 5px; vertical-align: middle; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +

                // Header Section
                "<div class=\"header\">" +
                img + // Replace with your logo URL
                "</div>" +

                // Content Section
                "<div class=\"content\">" +
                "<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" +
                "  <h2>Wallet Debit Notification!</h2>\n" +
                "  <p>A Debit transaction has been initiated from your wallet by <b>" + customerName + "</b>.</p>\n" +
                "  <ul>\n" +
                "    <li>TransactionEntity ID: " + transactionId + "</li>\n" +
                "    <li>Organization ID: " + organizationId + "</li>\n" +
                "    <li>Customer Name: " + customerName + "</li>\n" +
                "    <li>Organization Name: " + organizationName + "</li>\n" +
                "    <li>Amount: &#8358;" + amount.toString() + "</li>\n" +
                "    <li>Bill Type: " + billType + "</li>\n" +
                "    <li>Current Balance: &#8358;" + currentBal.toString() + "</li>\n" +
                "    <li>TransactionEntity Date: " + transactionDate + "</li>\n" +
                "  </ul>\n" +
                "</div>" +
                "<p>Thank You,<br>redtech Team</p>" +
                "</div>" +

                // Footer Section
                "<div class=\"footer\">" +
                "<p>&copy; redtech 2024. All rights reserved</p>" +
                "<a href=\"#\"><img src=\"icon-whatsapp.png\" alt=\"WhatsApp\"></a>" +
                "<a href=\"#\"><img src=\"icon-linkedin.png\" alt=\"LinkedIn\"></a>" +
                "<a href=\"#\"><img src=\"icon-facebook.png\" alt=\"Facebook\"></a>" +
                "<a href=\"#\"><img src=\"icon-instagram.png\" alt=\"Instagram\"></a>" +
                "<a href=\"#\"><img src=\"icon-email.png\" alt=\"Email\"></a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return htmlContent;
    }

    public static String generateHtmlContent(String userName, String reportDate, long totalTransactions,
                                             BigDecimal totalCommissionEarns, BigDecimal totalWalletSpending, double totalCommissionPercen,
                                             double successfulTransactionPercentage, double failedTransactionPercentage,
                                             String recipientEmail, String headerLogo) {

        String img = "<img src=" + headerLogo + " alt=\"redtech Logo\" style=\"display: block;\">";


        String emailContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>Commission Report</title>" +
                "</head>" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">" +

                // Main container
                "<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"background-color: #ffffff; border-collapse: collapse; margin-top: 20px;\">" +

                // Header section
                // "<img src=image se
                "<tr><td align=\"center\" style=\"padding: 20px 0;\">" +
                img +
                "</td></tr>" +

                // Greeting section
                "<tr><td style=\"padding: 0 30px;\">" +
                "<p style=\"font-size: 18px; color: #333;\">Hi " + userName + ",</p>" +
                "<p style=\"font-size: 16px; color: #333;\">Here is your Commission Report for Today:</p>" +
                "</td></tr>" +

                // Report table
                "<tr><td style=\"padding: 20px 30px;\">" +
                "<table cellpadding=\"5\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse; font-size: 16px; color: #333;\">" +

                // Date/Time
                "<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Date/Time</td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + "</td></tr>" +

                // Merchant Name
                "<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Merchant Name</td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + userName.substring(0, userName.length() - 4) + "</td></tr>" +

                // Total Transactions
                "<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Total Transactions</td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + totalTransactions + "</td></tr>" +

                // Amount Generated
                "<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Amount Generated</td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">NGN " + String.format("%,.2f", totalWalletSpending) + "</td></tr>" +

                // Commission Earned
                "<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Commission Earned (" + totalCommissionPercen + "%)</td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">NGN " + String.format("%,.2f", totalCommissionEarns) + "</td></tr>" +

                //Successful transactions percentage
                "<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Total Successful Transactions</td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">NGN " + String.format("%,.2f", successfulTransactionPercentage) + "%</td></tr>" +

                //Failed transactions percentage
                "<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Total Failed Transactions</td>" +
                "<td style=\"border: 1px solid #ddd; padding: 8px;\">NGN " + String.format("%,.2f", failedTransactionPercentage) + "%</td></tr>" +
                "</table>" +
                "</td></tr>" +


                // Closing section
                "<tr><td style=\"padding: 20px 30px;\">" +
                "<p style=\"font-size: 16px; color: #333;\">Thank You,</p>" +
                "<p style=\"font-size: 16px; color: #333;\">redtech Team</p>" +
                "</td></tr>" +

                // Footer section
                "<tr><td style=\"padding: 20px 30px; background-color: #f9f9f9; font-size: 12px; color: #666; text-align: center;\">" +
                "<p>This email was sent to " + recipientEmail + ". For enquiries on redtech’s products and services, please send an email to <a href=\"mailto:redtech.support@bunchbay.com\" style=\"color: #007bff; text-decoration: none;\">redtech.support@bunchbay.com</a></p>" +
                "<p style=\"margin-top: 10px;\">&copy; redtech 2024. All rights reserved</p>" +
                "</td></tr>" +

                // Social Media Icons
                "<tr><td align=\"center\" style=\"padding: 10px 0; background-color: #f9f9f9;\">" +
                "<a href=\"https://whatsapp.com\"><img src=\"https://yourimagepath.com/whatsapp-icon.png\" alt=\"WhatsApp\" width=\"24\" style=\"margin: 0 5px;\"></a>" +
                "<a href=\"https://linkedin.com\"><img src=\"https://yourimagepath.com/linkedin-icon.png\" alt=\"LinkedIn\" width=\"24\" style=\"margin: 0 5px;\"></a>" +
                "<a href=\"https://facebook.com\"><img src=\"https://yourimagepath.com/facebook-icon.png\" alt=\"Facebook\" width=\"24\" style=\"margin: 0 5px;\"></a>" +
                "<a href=\"https://instagram.com\"><img src=\"https://yourimagepath.com/instagram-icon.png\" alt=\"Instagram\" width=\"24\" style=\"margin: 0 5px;\"></a>" +
                "<a href=\"mailto:redtech.support@bunchbay.com\"><img src=\"https://yourimagepath.com/email-icon.png\" alt=\"Email\" width=\"24\" style=\"margin: 0 5px;\"></a>" +
                "</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";
        return emailContent;
    }

    public static String welcomeEmailHtml(String name, String headerLogo) {

        String img = "<img src=" + headerLogo + " alt=\"redtech Logo\">";

        // Header Section
        // Replace with your logo URL
        // Content Section
        // Footer Section

        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>Welcome Email</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #F4F7FA; margin: 0; padding: 0; }" +
                ".email-container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; " +
                "border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; }" +
                ".header { padding: 20px; background-color: #E8F4FF; " +
                "background-image: url('background-pattern.png'); background-repeat: no-repeat; " +
                "background-size: cover; text-align: center; }" +
                ".header img { height: 50px; }" +
                ".content { padding: 20px; }" +
                ".content h2 { color: #333; }" +
                ".content p { color: #555; line-height: 1.6; }" +
                ".content a { color: #0073E6; text-decoration: none; }" +
                ".content a:hover { text-decoration: underline; }" +
                ".footer { text-align: center; padding: 20px; background-color: #F4F7FA; " +
                "font-size: 12px; color: #888; }" +
                ".footer img { width: 24px; margin: 0 5px; vertical-align: middle; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +

                // Header Section
                "<div class=\"header\">" +
                img + // Replace with your logo URL
                "</div>" +

                // Content Section
                "<div class=\"content\">" +
                "<h2>Hi " + name + ",</h2>" +
                "<p>Thank you for signing up on <a href=\"#\">Paypay</a>.</p>" +
                "<p>Congratulations on joining thousands of people who save valuable time by using redtech to pay their bills. " +
                "We’re really glad to have you on board.</p>" +
                "<p>In a bid to serve you better, kindly reach us via <a href=\"mailto:redtech.support@bunchbay.com\">redtech.support@bunchbay.com</a> " +
                "for suggestions, reports or enquiries.</p>" +
                "<p>Once again,<br>Thanks & Welcome;<br>redtech Team</p>" +
                "</div>" +

                // Footer Section
                "<div class=\"footer\">" +
                "<p>&copy; redtech 2024. All rights reserved</p>" +
                "<a href=\"#\"><img src=\"icon-whatsapp.png\" alt=\"WhatsApp\"></a>" +
                "<a href=\"#\"><img src=\"icon-linkedin.png\" alt=\"LinkedIn\"></a>" +
                "<a href=\"#\"><img src=\"icon-facebook.png\" alt=\"Facebook\"></a>" +
                "<a href=\"#\"><img src=\"icon-instagram.png\" alt=\"Instagram\"></a>" +
                "<a href=\"#\"><img src=\"icon-email.png\" alt=\"Email\"></a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    public static String buildKycRequestReceivedEmail(String headerLogo, String organizationName) {

        String img = "<img src=" + headerLogo + " alt=\"redtech Logo\">";

        String htmlContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>KYC Request Received</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #F4F7FA; margin: 0; padding: 0; }" +
                ".email-container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; " +
                "border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; }" +
                ".header { padding: 20px; background-color: #E8F4FF; " +
                "background-image: url('background-pattern.png'); background-repeat: no-repeat; " +
                "background-size: cover; text-align: center; }" +
                ".header img { height: 50px; }" +
                ".content { padding: 20px; }" +
                ".content p { color: #555; line-height: 1.6; }" +
                ".content h2 { color: #1a73e8; font-size: 18px; }" +
                ".footer { text-align: center; padding: 20px; background-color: #F4F7FA; " +
                "font-size: 12px; color: #888; }" +
                ".footer img { width: 24px; margin: 0 5px; vertical-align: middle; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +

                // Header Section
                "<div class=\"header\">" +
                img + // Replace with your logo URL
                "</div>" +

                // Content Section
                "<div class=\"content\">" +
                "<p>Hi " + organizationName + ",</p>" +
                "<p>Your KYC request has been successfully received and approval is pending. " +
                "We will notify you once verification is completed and it is approved or if something is wrong.</p>" +
                "<br>" +
                "<p>Thank You,<br>redtech Team</p>" +
                "</div>" +

                // Footer Section
                "<div class=\"footer\">" +
                "<p>&copy; redtech 2024. All rights reserved</p>" +
                "<a href=\"#\"><img src=\"icon-whatsapp.png\" alt=\"WhatsApp\"></a>" +
                "<a href=\"#\"><img src=\"icon-linkedin.png\" alt=\"LinkedIn\"></a>" +
                "<a href=\"#\"><img src=\"icon-facebook.png\" alt=\"Facebook\"></a>" +
                "<a href=\"#\"><img src=\"icon-instagram.png\" alt=\"Instagram\"></a>" +
                "<a href=\"#\"><img src=\"icon-email.png\" alt=\"Email\"></a>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return htmlContent;

    }

    public static String buildKycSuccessfulmessage(String url, String customerName) {

        String img = "<img src=\"" + url + "\" alt=\"redtech Logo\" style=\"width: 150px;\">";

        String emailContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>KYC Verification Success</title>" +
                "</head>" +
                "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #F5F7FA;\">" +
                "<div style=\"max-width: 600px; margin: 0 auto; background-color: white; padding: 20px;\">" +

// Header
                "<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">" +
                img +
                "</div>" +

// Greeting and Message
                "<div style=\"padding: 20px; text-align: left;\">" +
                "<p style=\"font-size: 16px; color: #333;\">" +
                "Hi " + customerName + "," +
                "</p>" +
                "<p style=\"font-size: 16px; color: #333;\">" +
                "Good News! Your KYC request has been approved. Kindly " +
                "<a href=\"https://app.redtechlimited.com/login\" style=\"color: #1A56DB; text-decoration: none;\">log in</a> " +
                "to your account to continue with operations." +
                "</p>" +
                "<p style=\"font-size: 16px; color: #333;\">" +
                "Thank you,<br>" +
                "redtech Team" +
                "</p>" +
                "</div>" +

// Footer
                "<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">" +
                "<p style=\"font-size: 14px; color: #888888;\">© redtech 2024. All rights reserved</p>" +
                "<div style=\"margin-top: 10px;\">" +
                "<a href=\"https://wa.me/your-whatsapp\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-whatsapp-icon.png\" alt=\"WhatsApp\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"https://www.linkedin.com/company/redtech\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-linkedin-icon.png\" alt=\"LinkedIn\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"https://www.facebook.com/redtech\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-facebook-icon.png\" alt=\"Facebook\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"https://www.instagram.com/redtech\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-instagram-icon.png\" alt=\"Instagram\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"mailto:redtech.support@bunchbay.com\">" +
                "<img src=\"https://your-website.com/path-to-email-icon.png\" alt=\"Email\" style=\"width: 24px;\">" +
                "</a>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return emailContent;
    }

    public static String buildKycRejectionmessage(String url, String customer) {

        String img = "<img src=\"" + url + "\" alt=\"redtech Logo\" style=\"width: 150px;\">";

        String emailContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>KYC Verification Issue</title>" +
                "</head>" +
                "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #F5F7FA;\">" +
                "<div style=\"max-width: 600px; margin: 0 auto; background-color: white; padding: 20px;\">" +

// Header
                "<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">" +
                img +
                "</div>" +

// Greeting and Message
                "<div style=\"padding: 20px; text-align: left;\">" +
                "<p style=\"font-size: 16px; color: #333;\">" +
                "Hi [Customer Name]," +
                "</p>" +
                "<p style=\"font-size: 16px; color: #333;\">" +
                "Sorry to interrupt, but there is something wrong with your KYC request. " +
                "Kindly resend a better copy of the documents you sent before to resume verification." +
                "</p>" +
                "<p style=\"font-size: 16px; color: #333;\">" +
                "Thank you,<br>" +
                "redtech Team" +
                "</p>" +
                "</div>" +

// Footer
                "<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">" +
                "<p style=\"font-size: 14px; color: #888888;\">© redtech 2024. All rights reserved</p>" +
                "<div style=\"margin-top: 10px;\">" +
                "<a href=\"https://wa.me/your-whatsapp\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-whatsapp-icon.png\" alt=\"WhatsApp\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"https://www.linkedin.com/company/redtech\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-linkedin-icon.png\" alt=\"LinkedIn\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"https://www.facebook.com/redtech\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-facebook-icon.png\" alt=\"Facebook\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"https://www.instagram.com/redtech\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-instagram-icon.png\" alt=\"Instagram\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"mailto:redtech.support@bunchbay.com\">" +
                "<img src=\"https://your-website.com/path-to-email-icon.png\" alt=\"Email\" style=\"width: 24px;\">" +
                "</a>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return emailContent;

    }

    public static String generatePlainHtmlContent(String subject, String url) {

        String img = "<img src=\"" + url + "\" alt=\"redtech Logo\" style=\"width: 150px;\">";

        String emailContent = "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>KYC Verification Issue</title>" +
                "</head>" +
                "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #F5F7FA;\">" +
                "<div style=\"max-width: 600px; margin: 0 auto; background-color: white; padding: 20px;\">" +

// Header
                "<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">" +
                img +
                "</div>" +

// Greeting and Message
                "<div style=\"padding: 20px; text-align: left;\">" +
                "<p style=\"font-size: 16px; color: #333;\">" +
                subject +
                "</p>" +
                "<p style=\"font-size: 16px; color: #333;\">" +
                "Thank you,<br>" +
                "redtech Team" +
                "</p>" +
                "</div>" +

// Footer
                "<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">" +
                "<p style=\"font-size: 14px; color: #888888;\">© redtech 2024. All rights reserved</p>" +
                "<div style=\"margin-top: 10px;\">" +
                "<a href=\"https://wa.me/your-whatsapp\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-whatsapp-icon.png\" alt=\"WhatsApp\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"https://www.linkedin.com/company/redtech\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-linkedin-icon.png\" alt=\"LinkedIn\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"https://www.facebook.com/redtech\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-facebook-icon.png\" alt=\"Facebook\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"https://www.instagram.com/redtech\" style=\"margin-right: 10px;\">" +
                "<img src=\"https://your-website.com/path-to-instagram-icon.png\" alt=\"Instagram\" style=\"width: 24px;\">" +
                "</a>" +
                "<a href=\"mailto:redtech.support@bunchbay.com\">" +
                "<img src=\"https://your-website.com/path-to-email-icon.png\" alt=\"Email\" style=\"width: 24px;\">" +
                "</a>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return emailContent;

    }
}
