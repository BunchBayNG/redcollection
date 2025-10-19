//package com.bbng.dao.util.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//
//    @Value("${spring.mail.gmail.username}")
//    private String gmailUsername;
//
//    @Value("${spring.mail.gmail.password}")
//    private String gmailPassword;
//
//    @Bean(name = "sendGridSmtpSender") // ✅ Renamed to avoid conflict
//    public JavaMailSender sendGridMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.sendgrid.net");
//        mailSender.setPort(587);
//        mailSender.setUsername(gmailUsername);
//        mailSender.setPassword(gmailPassword);
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "false");
//
//        return mailSender;
//    }
//}
