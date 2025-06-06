package com.bbng.dao.util.email.controller;


import com.bbng.dao.microservices.auth.passport.config.JWTService;
import com.bbng.dao.microservices.auth.passport.impl.setupImpl.PermissionService;
import com.bbng.dao.util.email.dto.request.EmailRequestDTO;
import com.bbng.dao.util.email.dto.request.HeaderLogoRequestDto;
import com.bbng.dao.util.email.dto.request.MailStructure;
import com.bbng.dao.util.email.entity.EmailEntity;
import com.bbng.dao.util.email.service.EmailService;
import com.bbng.dao.util.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("api/v1/redtech/email")
@Validated

public class EmailController {

    private final EmailService emailService;
    private  final PermissionService permissionService;
    private final HttpServletRequest request;
    private final JWTService jwtService;

    public EmailController(EmailService emailService, PermissionService permissionService,
                           HttpServletRequest request, JWTService jwtService) {
        this.emailService = emailService;
        this.permissionService = permissionService;
        this.request = request;
        this.jwtService = jwtService;
    }

    @GetMapping("/admin/getSentEmails")
    public ResponseEntity<ResponseDto<List<EmailEntity>>> getMails(){
        log.info("assigning permissions to set merchant specific commission for vendVtu");

        permissionService.checkPermission(request, "ADMIN_GET_SENT_EMAILS", jwtService);
            return ResponseEntity.status(HttpStatus.OK).body(emailService.getSentOutMails());
    }
    @PostMapping("/admin/sendMail")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> sendMail(@Valid @RequestBody EmailRequestDTO requestDTO){
        try {
            return emailService.sendGridSimpleMail(requestDTO);
        } catch (IOException e) {
            log.error("AN Error occurred while sending out emails: {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @PostMapping(value = "/admin/sendEmailNotification", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> sendEmail(
            @RequestParam String subject,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String htmlContent,
            @RequestParam List<String> recipients,
            @RequestParam(required = false) List<MultipartFile> attachments
            ){
        try {
            var mailStructure = MailStructure.builder()
                    .subject(subject)
                    .htmlContent(htmlContent)
                    .message(message)
                    .recipients(recipients).build();

            permissionService.checkPermission(request, "ADMIN_SEND_EMAILS_NOTIFICATIONS", jwtService);
            return emailService.sendGridSimpleEmail(mailStructure, attachments);
        } catch (Exception e) {
            log.error("AN Error occurred while sending out emails: {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }



    @PostMapping(value = "admin/upload-redtech-headerLogo", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDto<Map<String, String>>> uploadHeaderLogo(
            @RequestPart("file") MultipartFile file){
        var headerLogo = HeaderLogoRequestDto.builder()
                .headerLogo(file)
                .build();

        permissionService.checkPermission(request, "ADMIN_UPLOAD_EMAILS_HEADER_IMAGE", jwtService);
    return ResponseEntity.status(HttpStatus.OK).body(emailService.uploadHeaderLogo(headerLogo));
    }

}
