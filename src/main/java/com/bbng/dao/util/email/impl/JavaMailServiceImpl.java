package com.bbng.dao.util.email.impl;


import com.bbng.dao.util.email.dto.request.MailStructure;
import com.bbng.dao.util.email.service.JavaMailService;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;


@Slf4j
@Service
@RequiredArgsConstructor()
public class  JavaMailServiceImpl implements JavaMailService {


    @Value("$(spring.mail.username)")
    private String fromMail;


    @Value("${mail.from}")
    private String sender;

    @Value("${SENDGRID_API_KEY}")
    private String send_grid_apiKey;


    private static final String DEFAULT_FROM_EMAIL = "bunchpay.support@bunchbay.com";

    @Value("${spring.mail.gmail.username}")
    private String gmailUsername;

    @Value("${spring.mail.gmail.password}")
    private String gmailPassword;




    @Override
    public void sendGridHtmlContent(String recipientEmail, MailStructure mailStructure) {

        String subject = mailStructure.getSubject();
        String content = mailStructure.getHtmlContent();
       // Content content = new Content("text/html", mailStructure.getMessage());
        try {
            log.info("Trying Gmail SMTP with port 587 (STARTTLS)");
            sendViaDynamicGmailSender(recipientEmail, subject, content, 587, false);
        } catch (Exception firstEx) {
            log.warn("Gmail SMTP via port 587 failed: {}", firstEx.getMessage());
            try {
                log.info("Trying Gmail SMTP with port 465 (SSL)");
                sendViaDynamicGmailSender(recipientEmail, subject, content, 465, true);
            } catch (Exception secondEx) {
                log.error("Gmail SMTP via both ports failed: {}", secondEx.getMessage());
            }
        }
    }


    private void sendViaDynamicGmailSender(String to, String subject, String htmlContent, int port, boolean useSsl) throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(port);
        mailSender.setUsername(gmailUsername);
        mailSender.setPassword(gmailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");

        if (useSsl) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
        } else {
            props.put("mail.smtp.starttls.enable", "true");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setFrom(DEFAULT_FROM_EMAIL);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
