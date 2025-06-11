package com.bbng.dao.util.email.impl;


import com.bbng.dao.util.email.dto.request.MailStructure;
import com.bbng.dao.util.email.service.JavaMailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor()
public class JavaMailServiceImpl implements JavaMailService {


    @Value("$(spring.mail.username)")
    private String fromMail;


    @Value("${mail.from}")
    private String sender;

    @Value("${SENDGRID_API_KEY}")
    private String send_grid_apiKey;



    @Override
    public void sendGridHtmlContent(String recipientEmail, MailStructure mailStructure, Attachments pdfAttachment) throws MessagingException {
        Email from = new Email(sender);
        String subject = mailStructure.getSubject();
        Email to = new Email(recipientEmail);

        //add content
        Content content = new Content("text/html", mailStructure.getHtmlContent());

        Mail newMail = new Mail(from, subject, to, content);


        //convert to base64
        if (pdfAttachment != null){
            newMail.addAttachments(pdfAttachment);
        }


        //send email using SendGrid

        SendGrid sg = new SendGrid(send_grid_apiKey);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(newMail.build());
            Response response = sg.api(request);
            log.info("DONE SENDING EMAIL USING SEND GRID RETURNING RESPONSE");
            log.info(String.valueOf(response.getStatusCode()));
            log.info(response.getBody());
            log.info(response.getHeaders().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
