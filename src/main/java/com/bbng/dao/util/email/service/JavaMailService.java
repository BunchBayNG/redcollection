package com.bbng.dao.util.email.service;


import com.bbng.dao.util.email.dto.request.MailStructure;
public interface JavaMailService {
    void sendGridHtmlContent(String recipientEmail, MailStructure mailStructure);
}
