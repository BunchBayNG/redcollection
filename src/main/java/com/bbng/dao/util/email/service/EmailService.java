package com.bbng.dao.util.email.service;


import com.bbng.dao.util.email.dto.request.EmailRequestDTO;
import com.bbng.dao.util.email.dto.request.HeaderLogoRequestDto;
import com.bbng.dao.util.email.dto.request.MailStructure;
import com.bbng.dao.util.email.entity.EmailEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface EmailService {
    /**
     * @param emailRequest- EmailRequestDTO Object
     *          The Message Object has compulsory fields to be included in each request
     *                    1. fromEmail
     *                    2. fromName
     *                    3. to:
     *                      3a. email
     *                      3b. name
     *                      3c. type
     *                    4.html--- Using html template
     *                    5. subject
     *                    6. EmailType
     */
    ResponseEntity<String> sendGridSimpleMail(EmailRequestDTO emailRequest) throws IOException;

    ResponseDto<Map<String, String>> uploadHeaderLogo(HeaderLogoRequestDto headerLogo);

    ResponseEntity<String> sendGridSimpleEmail(MailStructure mailStructure, List<MultipartFile> attachments);

    ResponseDto<List<EmailEntity>> getSentOutMails();
}