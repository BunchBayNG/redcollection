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

        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html lang=\"en\">");
        htmlContent.append("<head>");
        htmlContent.append("<meta charset=\"UTF-8\">");
        htmlContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        htmlContent.append("<title>Password Reset Email</title>");
        htmlContent.append("<style>");
        htmlContent.append("body { font-family: Arial, sans-serif; background-color: #F4F7FA; margin: 0; padding: 0; }");
        htmlContent.append(".email-container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; ");
        htmlContent.append("border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; }");
        htmlContent.append(".header { padding: 20px; background-color: #E8F4FF; ");
        htmlContent.append("background-image: url('background-pattern.png'); background-repeat: no-repeat; ");
        htmlContent.append("background-size: cover; text-align: center; }");
        htmlContent.append(".header img { height: 50px; }");
        htmlContent.append(".content { padding: 20px; }");
        htmlContent.append(".content h2 { color: #1a73e8; font-size: 18px; }");
        htmlContent.append(".content p { color: #555; line-height: 1.6; }");
        htmlContent.append(".content a { color: #0073E6; text-decoration: none; }");
        htmlContent.append(".content a:hover { text-decoration: underline; }");
        htmlContent.append(".reset-button { display: inline-block; padding: 10px 20px; margin-top: 20px; color: white; ");
        htmlContent.append("background-color: #0073E6; color: #ffffff; font-weight: bold; text-decoration: none; ");
        htmlContent.append("border-radius: 5px; }");
        htmlContent.append(".footer { text-align: center; padding: 20px; background-color: #F4F7FA; ");
        htmlContent.append("font-size: 12px; color: #888; }");
        htmlContent.append(".footer img { width: 24px; margin: 0 5px; vertical-align: middle; }");
        htmlContent.append("</style>");
        htmlContent.append("</head>");
        htmlContent.append("<body>");
        htmlContent.append("<div class=\"email-container\">");

        // Header Section
        htmlContent.append("<div class=\"header\">");
        String img = "<img src=\"" + headerLogo + "\" alt=\"redtech Logo\" style=\"width: 150px;\">";
        htmlContent.append(img); // Replace with your logo URL
        htmlContent.append("</div>");

        // Content Section
        htmlContent.append("<div class=\"content\">");
        htmlContent.append("<p>Hi ").append(toName).append(",</p>");
        htmlContent.append("<h2>Recalling passwords can be stressful</h2>");
        htmlContent.append("<p>You have been sent this email because we received a request to reset the password to your account.</p>");
        htmlContent.append("<p>To do this, please click the button below to reset your password.</p>");
        htmlContent.append("<a href=\"").append(verificationLink).append("\" class=\"reset-button\">Reset Password</a>");
        htmlContent.append("<p>If you did not request a password reset, please report this email to redtech.support@bunchbay.com.</p>");
        htmlContent.append("</div>");

        // Footer Section
        htmlContent.append("<div class=\"footer\">");
        htmlContent.append("<p>&copy; redtech 2025. All rights reserved</p>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-whatsapp.png\" alt=\"WhatsApp\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-linkedin.png\" alt=\"LinkedIn\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-facebook.png\" alt=\"Facebook\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-instagram.png\" alt=\"Instagram\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-email.png\" alt=\"Email\"></a>");
        htmlContent.append("</div>");

        htmlContent.append("</div>");
        htmlContent.append("</body>");
        htmlContent.append("</html>");

        return htmlContent.toString();

    }

    public static String verifyEmailHtmlContent(String firstName, String verificationLink, String headerLogo) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<div>");
// Header Section
        emailContent.append("<div style=\"background: url('https://i.ibb.co/9ZYtMGv/olena-bohovyk-d-IMJWLx1-Yb-E-unsplash-1-1.png') no-repeat center / cover; ");
        emailContent.append("width: 100%; max-width: 59rem; margin: auto; min-height: 200px;\">");
        emailContent.append("<img style=\"padding-left: 50px; padding-top: 20px;\" src=\"https://your-website.com/assets/images/Group.svg\" alt=\"\">");
        emailContent.append("<h1 style=\"color: #fff; font-weight: 700; font-size: 50px; text-align: center; padding-top: 30px\">");
        emailContent.append("Hello ").append(firstName).append("</h1>");
        emailContent.append("</div>");

// Main Content
        emailContent.append("<div style=\"text-align: center;\">");
        emailContent.append("<p style=\"width: fit-content; margin: auto; max-width: 29.5rem; text-align: center; font-weight: 400; ");
        emailContent.append("font-size: 18px; line-height: 25px; font-family: Inter, sans-serif; color: black;\">");
        emailContent.append("We have received a request to verify your account. To verify your account, please click on the link below.");
        emailContent.append("</p>");

// Verification Button
        emailContent.append("<div style=\"margin-top: 20px;\">");
        emailContent.append("<a href=\"" + verificationLink + "\" ");
        emailContent.append("style=\"display: inline-block; background-color: #1A56DB; color: white; text-decoration: none; ");
        emailContent.append("padding: 12px 20px; border-radius: 8px; font-size: 16px;\">");
        emailContent.append("Click to Verify Email");
        emailContent.append("</a>");
        emailContent.append("</div>");

// Alternative Link
        emailContent.append("<p style=\"width: fit-content; margin: auto; max-width: 29.5rem; text-align: center; font-weight: 400; ");
        emailContent.append("font-size: 18px; line-height: 25px; font-family: Inter, sans-serif; color: black;\">");
        emailContent.append("Alternatively, copy this link and paste it in your browser:<br>");
        emailContent.append("<a href=\"").append(verificationLink).append("\" style=\"color: #1A56DB;\">").append(verificationLink).append("</a>");
        emailContent.append("</p>");

// Expiry Notice
        emailContent.append("<p style=\"width: fit-content; margin: auto; margin-top: 14px; max-width: 29.5rem; text-align: center; ");
        emailContent.append("font-weight: 400; font-size: 18px; line-height: 25px; font-family: Inter, sans-serif; color: black;\">");
        emailContent.append("Please note that this link is only valid for 10 minutes. If you do not verify your account within this time frame, you will need to submit another request.");
        emailContent.append("</p>");

// Security Notice
        emailContent.append("<p style=\"width: fit-content; margin: auto; max-width: 29.5rem; margin-top: 14px; text-align: center; ");
        emailContent.append("font-weight: 400; font-size: 18px; line-height: 25px; font-family: Inter, sans-serif; color: black;\">");
        emailContent.append("If you did not initiate this request, please disregard this email and contact us immediately. We take the security of our users very seriously and want to ensure that your account remains safe.");
        emailContent.append("</p>");
        emailContent.append("</div>");

// Footer Section
        emailContent.append("<div style=\"background: rgba(217, 217, 217, 0.50); display: flex; margin-bottom: 40px; flex-direction: column; ");
        emailContent.append("gap: 20px; justify-content: center; align-items: center; width: 100%; max-width: 59rem; min-height: 152px; ");
        emailContent.append("margin: auto; margin-top: 2rem;\">");

// Social Media Icons
        emailContent.append("<div style=\"display: flex; align-items: center; gap: 10px; padding-top: 30px;\">");
        emailContent.append("<img src=\"https://your-website.com/assets/images/mdi_instagram.svg\" alt=\"Instagram\" style=\"width: 24px;\">");
        emailContent.append("<img src=\"https://your-website.com/assets/images/ri_twitter-line.svg\" alt=\"Twitter\" style=\"width: 24px;\">");
        emailContent.append("<img src=\"https://your-website.com/assets/images/mingcute_linkedin-line.svg\" alt=\"LinkedIn\" style=\"width: 24px;\">");
        emailContent.append("</div>");

// Footer Text
        emailContent.append("<div style=\"display: flex; align-items: center; gap: 10px; line-height: 0px; margin-bottom: 0px;\">");
        emailContent.append("<img style=\"width: 15px;\" src=\"https://your-website.com/assets/images/Group1000005194.svg\" alt=\"Logo\">");
        emailContent.append("<p style=\"color: rgba(31, 31, 31, 0.50);\">Registration Portal 2023</p>");
        emailContent.append("</div>");

        emailContent.append("</div>");
        emailContent.append("</div>");

        return emailContent.toString();
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
                                                      BigDecimal amount){
        return "<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" +
                "  <h2>Wallet Top Up</h2>\n" +
                "  <p>A top up transaction has been initiated for your wallet by <b>" + organizationName + "</b>.</p>\n" +
                "  <ul>\n" +
                "    <li>Transaction ID: " + transactionId + "</li>\n" +
                "    <li>Customer ID: " + customerId + "</li>\n" +
                "    <li>Customer Name: " + customerName + "</li>\n" +
                "    <li>Organization Name: " + organizationName + "</li>\n" +
                "    <li>Amount: &#8358;" + amount.toString() + "</li>\n" +
                "    <li>Payment Status:    PENDING" +"</li>\n" +
                "  </ul>\n" +
                "</div>";
    }
    public static  String confirmationAdminWalletTopupHtmlContent(String messageType, String walletTransactionId, String organizationId,
                                                                  String organizationName,
                                                                   BigDecimal amount,
                                                                  String accountNumber, String accountName, String userName, Instant confirmDate){


        return String.format("<div style=\"background-color: #F5F5F5; padding: 20px; font-family: Arial, sans-serif;\">\n" +
                        "  <h2 style=\"color: #333;\">Wallet Top-Up Notification</h2>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">Hello Dear,</p>\n" +
                        "  <p style=\"font-size: 16px; color: #555;\">\n" +
                        "    A payment Deposit from <b>%s</b> has been %s by the Admin <b>%s</b> in our system. Please review the transaction details below:\n" +
                        "  </p>\n" +
                        "  <ul style=\"font-size: 16px; color: #555; list-style-type: none; padding: 0;\">\n" +
                        "    <li><strong>Transaction ID:</strong> %s</li>\n" +
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
                        "</div>\n", organizationName,messageType, userName, walletTransactionId, organizationId,organizationName, userName,
                                     accountName, accountNumber, amount, confirmDate.toString() );
    }


    public static  String confirmationOrganizationWalletTopupHtmlContent(String messageType, String transactionId, String organizationId, String organizationName,
                                                                        BigDecimal amount, String accountNumber, String accountName, String userName,
                                                                         Instant isConfirmedDate){


        return "<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" +
                "  <h2>Wallet Top Up</h2>\n" +
                "  <p>A top up transaction has been " + messageType +" for your wallet!!!.</p>\n" +
                "  <ul>\n" +
                "    <li>Transaction ID: " + transactionId + "</li>\n" +
                "    <li>Organization ID: " + organizationId + "</li>\n" +
                "    <li>Customer Name: " + userName + "</li>\n" +
                "    <li>Organization Name: " + organizationName + "</li>\n" +
                "    <li>Amount: &#8358;" + amount.toString() + "</li>\n" +
                "    <li>Account Name:" + accountName + "</li>\n" +
                "    <li>Account Number:" + accountNumber + "</li>\n" +
                "    <li>Payment Status:" + messageType +"</li>\n" +
                "    <li>Confirmation Date:" + isConfirmedDate.toString() + "</li>\n" +
                "  </ul>\n" +
                "</div>";
    }



    public static String actionAdminWalletTopupHtmlContent(String walletTransactionId, String organizationName,
                                                           String businessEmail, String userName,
                                                           String accountNumber, String accountName, BigDecimal amount, Instant transactionDate){
        return String.format("<div style=\"background-color: #F5F5F5; padding: 20px; font-family: Arial, sans-serif;\">\n" +
                "  <h2 style=\"color: #333;\">Wallet Top-Up Notification</h2>\n" +
                "  <p style=\"font-size: 16px; color: #555;\">Hello Dear,</p>\n" +
                "  <p style=\"font-size: 16px; color: #555;\">\n" +
                "    A payment Deposit has been initiated by the merchant <b>%s</b> in our system. Please review the transaction details below:\n" +
                "  </p>\n" +
                "  <ul style=\"font-size: 16px; color: #555; list-style-type: none; padding: 0;\">\n" +
                "    <li><strong>Transaction ID:</strong> %s</li>\n" +
                "    <li><strong>Business Name:</strong> %s</li>\n" +
                "    <li><strong>Business email:</strong> %s</li>\n" +
                "    <li><strong>Customer Username:</strong> %s</li>\n" +
                "    <li><strong>Transfer To:</strong> %s</li>\n" +
                "    <li><strong>Account Number:</strong> %s</li>\n" +
                "    <li><strong>Amount:</strong> &#8358;%s</li>\n" +
                        "    <li><strong>Transaction Date:</strong> &#8358;%s</li>\n" +
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
                                                           String organizationName, BigDecimal amount){
        return "<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" +
                "  <h2>Wallet Withdrawal</h2>\n" +
                "  <p>A withdrawal transaction has been initiated from your wallet by <b>" + organizationName + "</b>.</p>\n" +
                "  <ul>\n" +
                "    <li>Transaction ID: " + transactionId + "</li>\n" +
                "    <li>Customer ID: " + customerId + "</li>\n" +
                "    <li>Customer Name: " + customerName + "</li>\n" +
                "    <li>Organization Name: " + organizationName + "</li>\n" +
                "    <li>Amount: &#8358;" + amount.toString() + "</li>\n" +
                "  </ul>\n" +
                "</div>";
    }
    public static String creditAlertHtmlContent(String organizationName, String organizationId,
                                                String customerName,  BigDecimal amount, String transactionDate, String billType,
                                                String transactionId, BigDecimal currentBal,
                                                BigDecimal billAmount, String message, String headerLogo){


        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html lang=\"en\">");
        htmlContent.append("<head>");
        htmlContent.append("<meta charset=\"UTF-8\">");
        htmlContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        htmlContent.append("<title>KYC Request Received</title>");
        htmlContent.append("<style>");
        htmlContent.append("body { font-family: Arial, sans-serif; background-color: #F4F7FA; margin: 0; padding: 0; }");
        htmlContent.append(".email-container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; ");
        htmlContent.append("border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; }");
        htmlContent.append(".header { padding: 20px; background-color: #E8F4FF; ");
        htmlContent.append("background-image: url('background-pattern.png'); background-repeat: no-repeat; ");
        htmlContent.append("background-size: cover; text-align: center; }");
        htmlContent.append(".header img { height: 50px; }");
        htmlContent.append(".content { padding: 20px; }");
        htmlContent.append(".content p { color: #555; line-height: 1.6; }");
        htmlContent.append(".content h2 { color: #1a73e8; font-size: 18px; }");
        htmlContent.append(".footer { text-align: center; padding: 20px; background-color: #F4F7FA; ");
        htmlContent.append("font-size: 12px; color: #888; }");
        htmlContent.append(".footer img { width: 24px; margin: 0 5px; vertical-align: middle; }");
        htmlContent.append("</style>");
        htmlContent.append("</head>");
        htmlContent.append("<body>");
        htmlContent.append("<div class=\"email-container\">");

        // Header Section
        htmlContent.append("<div class=\"header\">");
        String img = "<img src="+headerLogo+" alt=\"redtech Logo\">";
        htmlContent.append(img); // Replace with your logo URL
        htmlContent.append("</div>");

        // Content Section
        htmlContent.append("<div class=\"content\">");
        htmlContent.append("<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" + "  <h2> ").append(message).append(" Credit Notification</h2>\n").append("  <p>A Credit transaction has been perform on your ").append(message).append(" .</p>\n").append("  <ul>\n").append("    <li>Transaction ID: ").append(transactionId).append("</li>\n").append("    <li>Organization ID: ").append(organizationId).append("</li>\n").append("    <li>Customer Name: ").append(customerName).append("</li>\n").append("    <li>Organization Name: ").append(organizationName).append("</li>\n").append("    <li>Amount: &#8358;").append(amount.toString()).append("</li>\n").append("    <li>Bill Type: ").append(billType).append("</li>\n").append("    <li>Bill Amount:  &#8358;").append(billAmount.toString()).append("</li>\n").append("    <li>Current Balance:  &#8358; ").append(currentBal.toString()).append("</li>\n").append("    <li>Transaction Date: ").append(transactionDate).append("</li>\n").append("  </ul>\n").append("</div>");
        htmlContent.append("<p>Thank You,<br>redtech Team</p>");
        htmlContent.append("</div>");

        // Footer Section
        htmlContent.append("<div class=\"footer\">");
        htmlContent.append("<p>&copy; redtech 2024. All rights reserved</p>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-whatsapp.png\" alt=\"WhatsApp\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-linkedin.png\" alt=\"LinkedIn\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-facebook.png\" alt=\"Facebook\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-instagram.png\" alt=\"Instagram\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-email.png\" alt=\"Email\"></a>");
        htmlContent.append("</div>");

        htmlContent.append("</div>");
        htmlContent.append("</body>");
        htmlContent.append("</html>");

        return htmlContent.toString();

    }
    public static String debitAlertHtmlContent(String organizationName,
                                               String organizationId, String customerName,
                                               BigDecimal amount, String transactionDate,
                                               String billType, String transactionId,
                                               BigDecimal currentBal, String headerLogo){

        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html lang=\"en\">");
        htmlContent.append("<head>");
        htmlContent.append("<meta charset=\"UTF-8\">");
        htmlContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        htmlContent.append("<title>KYC Request Received</title>");
        htmlContent.append("<style>");
        htmlContent.append("body { font-family: Arial, sans-serif; background-color: #F4F7FA; margin: 0; padding: 0; }");
        htmlContent.append(".email-container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; ");
        htmlContent.append("border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; }");
        htmlContent.append(".header { padding: 20px; background-color: #E8F4FF; ");
        htmlContent.append("background-image: url('background-pattern.png'); background-repeat: no-repeat; ");
        htmlContent.append("background-size: cover; text-align: center; }");
        htmlContent.append(".header img { height: 50px; }");
        htmlContent.append(".content { padding: 20px; }");
        htmlContent.append(".content p { color: #555; line-height: 1.6; }");
        htmlContent.append(".content h2 { color: #1a73e8; font-size: 18px; }");
        htmlContent.append(".footer { text-align: center; padding: 20px; background-color: #F4F7FA; ");
        htmlContent.append("font-size: 12px; color: #888; }");
        htmlContent.append(".footer img { width: 24px; margin: 0 5px; vertical-align: middle; }");
        htmlContent.append("</style>");
        htmlContent.append("</head>");
        htmlContent.append("<body>");
        htmlContent.append("<div class=\"email-container\">");

        // Header Section
        htmlContent.append("<div class=\"header\">");
        String img = "<img src="+headerLogo+" alt=\"redtech Logo\">";
        htmlContent.append(img); // Replace with your logo URL
        htmlContent.append("</div>");

        // Content Section
        htmlContent.append("<div class=\"content\">");
        htmlContent.append("<div style=\"background-color: #F5F5F5; padding: 20px;\">\n" +
                "  <h2>Wallet Debit Notification!</h2>\n" +
                "  <p>A Debit transaction has been initiated from your wallet by <b>" + customerName + "</b>.</p>\n" +
                "  <ul>\n" +
                "    <li>Transaction ID: " + transactionId + "</li>\n" +
                "    <li>Organization ID: " + organizationId + "</li>\n" +
                "    <li>Customer Name: " + customerName + "</li>\n" +
                "    <li>Organization Name: " + organizationName + "</li>\n" +
                "    <li>Amount: &#8358;" + amount.toString() + "</li>\n" +
                "    <li>Bill Type: " + billType + "</li>\n" +
                "    <li>Current Balance: &#8358;" + currentBal.toString() + "</li>\n" +
                "    <li>Transaction Date: " + transactionDate + "</li>\n" +
                "  </ul>\n" +
                "</div>");
        htmlContent.append("<p>Thank You,<br>redtech Team</p>");
        htmlContent.append("</div>");

        // Footer Section
        htmlContent.append("<div class=\"footer\">");
        htmlContent.append("<p>&copy; redtech 2024. All rights reserved</p>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-whatsapp.png\" alt=\"WhatsApp\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-linkedin.png\" alt=\"LinkedIn\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-facebook.png\" alt=\"Facebook\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-instagram.png\" alt=\"Instagram\"></a>");
        htmlContent.append("<a href=\"#\"><img src=\"icon-email.png\" alt=\"Email\"></a>");
        htmlContent.append("</div>");

        htmlContent.append("</div>");
        htmlContent.append("</body>");
        htmlContent.append("</html>");

        return htmlContent.toString();
    }

    public static String generateHtmlContent(String userName, String reportDate, long totalTransactions,
                                             BigDecimal totalCommissionEarns, BigDecimal totalWalletSpending, double totalCommissionPercen,
                                             double successfulTransactionPercentage, double failedTransactionPercentage,
                                             String recipientEmail, String headerLogo) {

        StringBuilder emailContent = new StringBuilder();

        emailContent.append("<!DOCTYPE html>");
        emailContent.append("<html lang=\"en\">");
        emailContent.append("<head>");
        emailContent.append("<meta charset=\"UTF-8\">");
        emailContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        emailContent.append("<title>Commission Report</title>");
        emailContent.append("</head>");
        emailContent.append("<body style=\"font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0;\">");

        // Main container
        emailContent.append("<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"background-color: #ffffff; border-collapse: collapse; margin-top: 20px;\">");

        // Header section
        // "<img src=image se
        String img = "<img src=" + headerLogo +" alt=\"redtech Logo\" style=\"display: block;\">";
        emailContent.append("<tr><td align=\"center\" style=\"padding: 20px 0;\">");
        emailContent.append(img);
        emailContent.append("</td></tr>");

        // Greeting section
        emailContent.append("<tr><td style=\"padding: 0 30px;\">");
        emailContent.append("<p style=\"font-size: 18px; color: #333;\">Hi ").append(userName).append(",</p>");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">Here is your Commission Report for Today:</p>");
        emailContent.append("</td></tr>");

        // Report table
        emailContent.append("<tr><td style=\"padding: 20px 30px;\">");
        emailContent.append("<table cellpadding=\"5\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse; font-size: 16px; color: #333;\">");

        // Date/Time
        emailContent.append("<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Date/Time</td>");
        emailContent.append("<td style=\"border: 1px solid #ddd; padding: 8px;\">").append(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))).append("</td></tr>");

        // Merchant Name
        emailContent.append("<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Merchant Name</td>");
        emailContent.append("<td style=\"border: 1px solid #ddd; padding: 8px;\">").append(userName, 0, userName.length() - 4).append("</td></tr>");

        // Total Transactions
        emailContent.append("<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Total Transactions</td>");
        emailContent.append("<td style=\"border: 1px solid #ddd; padding: 8px;\">").append(totalTransactions).append("</td></tr>");

        // Amount Generated
        emailContent.append("<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Amount Generated</td>");
        emailContent.append("<td style=\"border: 1px solid #ddd; padding: 8px;\">NGN ").append(String.format("%,.2f", totalWalletSpending)).append("</td></tr>");

        // Commission Earned
        emailContent.append("<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Commission Earned (").append(totalCommissionPercen).append("%)</td>");
        emailContent.append("<td style=\"border: 1px solid #ddd; padding: 8px;\">NGN ").append(String.format("%,.2f", totalCommissionEarns)).append("</td></tr>");

        //Successful transactions percentage
        emailContent.append("<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Total Successful Transactions</td>");
        emailContent.append("<td style=\"border: 1px solid #ddd; padding: 8px;\">NGN ").append(String.format("%,.2f", successfulTransactionPercentage)).append("%</td></tr>");

        //Failed transactions percentage
        emailContent.append("<tr><td style=\"border: 1px solid #ddd; padding: 8px; font-weight: bold;\">Total Failed Transactions</td>");
        emailContent.append("<td style=\"border: 1px solid #ddd; padding: 8px;\">NGN ").append(String.format("%,.2f", failedTransactionPercentage)).append("%</td></tr>");

        emailContent.append("</table>");
        emailContent.append("</td></tr>");



        // Closing section
        emailContent.append("<tr><td style=\"padding: 20px 30px;\">");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">Thank You,</p>");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">redtech Team</p>");
        emailContent.append("</td></tr>");

        // Footer section
        emailContent.append("<tr><td style=\"padding: 20px 30px; background-color: #f9f9f9; font-size: 12px; color: #666; text-align: center;\">");
        emailContent.append("<p>This email was sent to ").append(recipientEmail).append(". For enquiries on redtech’s products and services, please send an email to <a href=\"mailto:redtech.support@bunchbay.com\" style=\"color: #007bff; text-decoration: none;\">redtech.support@bunchbay.com</a></p>");
        emailContent.append("<p style=\"margin-top: 10px;\">&copy; redtech 2024. All rights reserved</p>");
        emailContent.append("</td></tr>");

        // Social Media Icons
        emailContent.append("<tr><td align=\"center\" style=\"padding: 10px 0; background-color: #f9f9f9;\">");
        emailContent.append("<a href=\"https://whatsapp.com\"><img src=\"https://yourimagepath.com/whatsapp-icon.png\" alt=\"WhatsApp\" width=\"24\" style=\"margin: 0 5px;\"></a>");
        emailContent.append("<a href=\"https://linkedin.com\"><img src=\"https://yourimagepath.com/linkedin-icon.png\" alt=\"LinkedIn\" width=\"24\" style=\"margin: 0 5px;\"></a>");
        emailContent.append("<a href=\"https://facebook.com\"><img src=\"https://yourimagepath.com/facebook-icon.png\" alt=\"Facebook\" width=\"24\" style=\"margin: 0 5px;\"></a>");
        emailContent.append("<a href=\"https://instagram.com\"><img src=\"https://yourimagepath.com/instagram-icon.png\" alt=\"Instagram\" width=\"24\" style=\"margin: 0 5px;\"></a>");
        emailContent.append("<a href=\"mailto:redtech.support@bunchbay.com\"><img src=\"https://yourimagepath.com/email-icon.png\" alt=\"Email\" width=\"24\" style=\"margin: 0 5px;\"></a>");
        emailContent.append("</td></tr>");

        emailContent.append("</table>");
        emailContent.append("</body>");
        emailContent.append("</html>");
            return emailContent.toString();
    }

    public static String welcomeEmailHtml(String name, String headerLogo){

        String img = "<img src=" + headerLogo +" alt=\"redtech Logo\">";

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
            StringBuilder htmlContent = new StringBuilder();

            htmlContent.append("<!DOCTYPE html>");
            htmlContent.append("<html lang=\"en\">");
            htmlContent.append("<head>");
            htmlContent.append("<meta charset=\"UTF-8\">");
            htmlContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            htmlContent.append("<title>KYC Request Received</title>");
            htmlContent.append("<style>");
            htmlContent.append("body { font-family: Arial, sans-serif; background-color: #F4F7FA; margin: 0; padding: 0; }");
            htmlContent.append(".email-container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; ");
            htmlContent.append("border-radius: 10px; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden; }");
            htmlContent.append(".header { padding: 20px; background-color: #E8F4FF; ");
            htmlContent.append("background-image: url('background-pattern.png'); background-repeat: no-repeat; ");
            htmlContent.append("background-size: cover; text-align: center; }");
            htmlContent.append(".header img { height: 50px; }");
            htmlContent.append(".content { padding: 20px; }");
            htmlContent.append(".content p { color: #555; line-height: 1.6; }");
            htmlContent.append(".content h2 { color: #1a73e8; font-size: 18px; }");
            htmlContent.append(".footer { text-align: center; padding: 20px; background-color: #F4F7FA; ");
            htmlContent.append("font-size: 12px; color: #888; }");
            htmlContent.append(".footer img { width: 24px; margin: 0 5px; vertical-align: middle; }");
            htmlContent.append("</style>");
            htmlContent.append("</head>");
            htmlContent.append("<body>");
            htmlContent.append("<div class=\"email-container\">");

            // Header Section
            htmlContent.append("<div class=\"header\">");
            String img = "<img src="+headerLogo+" alt=\"redtech Logo\">";
            htmlContent.append(img); // Replace with your logo URL
            htmlContent.append("</div>");

            // Content Section
            htmlContent.append("<div class=\"content\">");
            htmlContent.append("<p>Hi ").append(organizationName).append(",</p>");
            htmlContent.append("<p>Your KYC request has been successfully received and approval is pending. ");
            htmlContent.append("We will notify you once verification is completed and it is approved or if something is wrong.</p>");
            htmlContent.append("<br>");
            htmlContent.append("<p>Thank You,<br>redtech Team</p>");
            htmlContent.append("</div>");

            // Footer Section
            htmlContent.append("<div class=\"footer\">");
            htmlContent.append("<p>&copy; redtech 2024. All rights reserved</p>");
            htmlContent.append("<a href=\"#\"><img src=\"icon-whatsapp.png\" alt=\"WhatsApp\"></a>");
            htmlContent.append("<a href=\"#\"><img src=\"icon-linkedin.png\" alt=\"LinkedIn\"></a>");
            htmlContent.append("<a href=\"#\"><img src=\"icon-facebook.png\" alt=\"Facebook\"></a>");
            htmlContent.append("<a href=\"#\"><img src=\"icon-instagram.png\" alt=\"Instagram\"></a>");
            htmlContent.append("<a href=\"#\"><img src=\"icon-email.png\" alt=\"Email\"></a>");
            htmlContent.append("</div>");

            htmlContent.append("</div>");
            htmlContent.append("</body>");
            htmlContent.append("</html>");

            return htmlContent.toString();

    }

    public static String buildKycSuccessfulmessage(String url, String customerName) {
        StringBuilder emailContent = new StringBuilder();

        emailContent.append("<!DOCTYPE html>");
        emailContent.append("<html lang=\"en\">");
        emailContent.append("<head>");
        emailContent.append("<meta charset=\"UTF-8\">");
        emailContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        emailContent.append("<title>KYC Verification Success</title>");
        emailContent.append("</head>");
        emailContent.append("<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #F5F7FA;\">");

        emailContent.append("<div style=\"max-width: 600px; margin: 0 auto; background-color: white; padding: 20px;\">");

// Header
        emailContent.append("<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">");
       String img = "<img src=\"" + url + "\" alt=\"redtech Logo\" style=\"width: 150px;\">";
        emailContent.append(img);
        emailContent.append("</div>");

// Greeting and Message
        emailContent.append("<div style=\"padding: 20px; text-align: left;\">");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">");
        emailContent.append("Hi ").append(customerName).append(",");
        emailContent.append("</p>");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">");
        emailContent.append("Good News! Your KYC request has been approved. Kindly ");
        emailContent.append("<a href=\"https://app.redtechlimited.com/login\" style=\"color: #1A56DB; text-decoration: none;\">log in</a> ");
        emailContent.append("to your account to continue with operations.");
        emailContent.append("</p>");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">");
        emailContent.append("Thank you,<br>");
        emailContent.append("redtech Team");
        emailContent.append("</p>");
        emailContent.append("</div>");

// Footer
        emailContent.append("<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">");
        emailContent.append("<p style=\"font-size: 14px; color: #888888;\">© redtech 2024. All rights reserved</p>");
        emailContent.append("<div style=\"margin-top: 10px;\">");
        emailContent.append("<a href=\"https://wa.me/your-whatsapp\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-whatsapp-icon.png\" alt=\"WhatsApp\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"https://www.linkedin.com/company/redtech\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-linkedin-icon.png\" alt=\"LinkedIn\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"https://www.facebook.com/redtech\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-facebook-icon.png\" alt=\"Facebook\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"https://www.instagram.com/redtech\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-instagram-icon.png\" alt=\"Instagram\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"mailto:redtech.support@bunchbay.com\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-email-icon.png\" alt=\"Email\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("</div>");
        emailContent.append("</div>");

        emailContent.append("</div>");
        emailContent.append("</body>");
        emailContent.append("</html>");

        return emailContent.toString();
    }

    public static String buildKycRejectionmessage(String url, String customer) {
        StringBuilder emailContent = new StringBuilder();

        emailContent.append("<!DOCTYPE html>");
        emailContent.append("<html lang=\"en\">");
        emailContent.append("<head>");
        emailContent.append("<meta charset=\"UTF-8\">");
        emailContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        emailContent.append("<title>KYC Verification Issue</title>");
        emailContent.append("</head>");
        emailContent.append("<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #F5F7FA;\">");

        emailContent.append("<div style=\"max-width: 600px; margin: 0 auto; background-color: white; padding: 20px;\">");

// Header
        emailContent.append("<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">");
        String img = "<img src=\"" + url + "\" alt=\"redtech Logo\" style=\"width: 150px;\">";
        emailContent.append(img);
        emailContent.append("</div>");

// Greeting and Message
        emailContent.append("<div style=\"padding: 20px; text-align: left;\">");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">");
        emailContent.append("Hi [Customer Name],");
        emailContent.append("</p>");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">");
        emailContent.append("Sorry to interrupt, but there is something wrong with your KYC request. ");
        emailContent.append("Kindly resend a better copy of the documents you sent before to resume verification.");
        emailContent.append("</p>");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">");
        emailContent.append("Thank you,<br>");
        emailContent.append("redtech Team");
        emailContent.append("</p>");
        emailContent.append("</div>");

// Footer
        emailContent.append("<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">");
        emailContent.append("<p style=\"font-size: 14px; color: #888888;\">© redtech 2024. All rights reserved</p>");
        emailContent.append("<div style=\"margin-top: 10px;\">");
        emailContent.append("<a href=\"https://wa.me/your-whatsapp\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-whatsapp-icon.png\" alt=\"WhatsApp\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"https://www.linkedin.com/company/redtech\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-linkedin-icon.png\" alt=\"LinkedIn\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"https://www.facebook.com/redtech\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-facebook-icon.png\" alt=\"Facebook\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"https://www.instagram.com/redtech\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-instagram-icon.png\" alt=\"Instagram\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"mailto:redtech.support@bunchbay.com\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-email-icon.png\" alt=\"Email\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("</div>");
        emailContent.append("</div>");

        emailContent.append("</div>");
        emailContent.append("</body>");
        emailContent.append("</html>");

        return emailContent.toString();

    }

    public static String generatePlainHtmlContent(String subject, String url) {
        StringBuilder emailContent = new StringBuilder();

        emailContent.append("<!DOCTYPE html>");
        emailContent.append("<html lang=\"en\">");
        emailContent.append("<head>");
        emailContent.append("<meta charset=\"UTF-8\">");
        emailContent.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        emailContent.append("<title>KYC Verification Issue</title>");
        emailContent.append("</head>");
        emailContent.append("<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #F5F7FA;\">");

        emailContent.append("<div style=\"max-width: 600px; margin: 0 auto; background-color: white; padding: 20px;\">");

// Header
        emailContent.append("<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">");
        String img = "<img src=\"" + url + "\" alt=\"redtech Logo\" style=\"width: 150px;\">";
        emailContent.append(img);
        emailContent.append("</div>");

// Greeting and Message
        emailContent.append("<div style=\"padding: 20px; text-align: left;\">");
        emailContent.append("<p style=\"font-size: 16px; color: #333;\">");
        emailContent.append(subject);
        emailContent.append("</p>");

        emailContent.append("<p style=\"font-size: 16px; color: #333;\">");
        emailContent.append("Thank you,<br>");
        emailContent.append("redtech Team");
        emailContent.append("</p>");
        emailContent.append("</div>");

// Footer
        emailContent.append("<div style=\"background: #F0F8FF; padding: 20px; text-align: center;\">");
        emailContent.append("<p style=\"font-size: 14px; color: #888888;\">© redtech 2024. All rights reserved</p>");
        emailContent.append("<div style=\"margin-top: 10px;\">");
        emailContent.append("<a href=\"https://wa.me/your-whatsapp\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-whatsapp-icon.png\" alt=\"WhatsApp\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"https://www.linkedin.com/company/redtech\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-linkedin-icon.png\" alt=\"LinkedIn\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"https://www.facebook.com/redtech\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-facebook-icon.png\" alt=\"Facebook\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"https://www.instagram.com/redtech\" style=\"margin-right: 10px;\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-instagram-icon.png\" alt=\"Instagram\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("<a href=\"mailto:redtech.support@bunchbay.com\">");
        emailContent.append("<img src=\"https://your-website.com/path-to-email-icon.png\" alt=\"Email\" style=\"width: 24px;\">");
        emailContent.append("</a>");
        emailContent.append("</div>");
        emailContent.append("</div>");

        emailContent.append("</div>");
        emailContent.append("</body>");
        emailContent.append("</html>");

        return emailContent.toString();

    }
}
