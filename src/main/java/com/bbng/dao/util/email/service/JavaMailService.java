package com.bbng.dao.util.email.service;


import com.bbng.dao.util.email.dto.request.MailStructure;
import com.sendgrid.helpers.mail.objects.Attachments;
import jakarta.mail.MessagingException;

public interface JavaMailService {
    void sendGridHtmlContent(String recipientEmail, MailStructure mailStructure, Attachments pdfAttachment) throws MessagingException;
}
