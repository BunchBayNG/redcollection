package com.bbng.dao.util.email.impl;


import com.bbng.dao.util.email.dto.request.EmailRequestDTO;
import com.bbng.dao.util.email.dto.request.HeaderLogoRequestDto;
import com.bbng.dao.util.email.dto.request.MailStructure;
import com.bbng.dao.util.email.dto.request.To;
import com.bbng.dao.util.email.entity.EmailEntity;
import com.bbng.dao.util.email.repository.EmailRepository;
import com.bbng.dao.util.email.service.EmailService;
import com.bbng.dao.util.email.utils.Utils;
import com.bbng.dao.util.exceptions.customExceptions.BadRequestException;
import com.bbng.dao.util.exceptions.customExceptions.InternalServerException;
import com.bbng.dao.util.fileUpload.entity.HeaderLogoEntity;
import com.bbng.dao.util.fileUpload.repository.HeaderLogoRepository;
import com.bbng.dao.util.response.ResponseDto;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final Cloudinary cloudinary;
    private final HeaderLogoRepository headerLogoRepository;
    @Value("${SENDGRID_API_KEY}")
    private String key;
    @Value("${mail.from}")
    private String fromEmail;

    @Autowired
    public EmailServiceImpl(EmailRepository emailRepository, Cloudinary cloudinary, HeaderLogoRepository headerLogoRepository) {
        this.emailRepository = emailRepository;
        this.cloudinary = cloudinary;
        this.headerLogoRepository = headerLogoRepository;
    }

    /**
     * @param - EmailRequestDTO Object
     *          The Message Object has compulsory fields to be included in each request
     *          1. fromEmail
     *          2. fromName
     *          3. to:
     *          3a. email
     *          3b. name
     *          3c. type
     *          4.html--- Using html template
     *          5. subject
     *          6. EmailType
     */
//    @Override
//    public ResponseEntity<EmailResponseDto[]> sendSimpleMail(EmailRequestDTO emailRequest) {
//        HttpHeaders headers = new HttpHeaders();
//        Message message = Message.builder()
//                .html(getHtml(emailRequest.getMessage().getHtml()))
//                .text(emailRequest.getMessage().getText())
//                .subject(getSubject(emailRequest.getMessage().getSubject()))
//                .fromEmail(fromEmail)
//                .fromName(getFromName(emailRequest.getMessage().getFromName()))
//                .to(getTo(emailRequest.getMessage().getTo()))
//                .headers(emailRequest.getMessage().getHeaders())
//                .important(emailRequest.getMessage().getImportant())
//                .trackOpens(emailRequest.getMessage().getTrackOpens())
//                .trackClicks(emailRequest.getMessage().getTrackClicks())
//                .autoText(emailRequest.getMessage().getAutoText())
//                .autoHtml(emailRequest.getMessage().getAutoHtml())
//                .inlineCss(emailRequest.getMessage().getInlineCss())
//                .urlStripQs(emailRequest.getMessage().getUrlStripQs())
//                .preserveRecipients(emailRequest.getMessage().getPreserveRecipients())
//                .viewContentLink(emailRequest.getMessage().getViewContentLink())
//                .bccAddress(emailRequest.getMessage().getBccAddress())
//                .trackingDomain(emailRequest.getMessage().getTrackingDomain())
//                .signingDomain(emailRequest.getMessage().getSigningDomain())
//                .returnPathDomain(emailRequest.getMessage().getReturnPathDomain())
//                .merge(emailRequest.getMessage().getMerge())
//                .mergeLanguage(emailRequest.getMessage().getMergeLanguage())
//                .globalMergeVars(emailRequest.getMessage().getGlobalMergeVars())
//                .mergeVars(emailRequest.getMessage().getMergeVars())
//                .tags(emailRequest.getMessage().getTags())
//                .subaccount(emailRequest.getMessage().getSubaccount())
//                .googleAnalyticsCampaign(emailRequest.getMessage().getGoogleAnalyticsCampaign())
//                .googleAnalyticsDomains(emailRequest.getMessage().getGoogleAnalyticsDomains())
//                .metadata(emailRequest.getMessage().getMetadata())
//                .recipientMetadata(emailRequest.getMessage().getRecipientMetadata())
//                .attachments(emailRequest.getMessage().getAttachments())
//                .images(emailRequest.getMessage().getImages())
//                .build();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//                EmailRequestDTO request = EmailRequestDTO.builder()
//                .key(key)
//                .sendAt(emailRequest.getSendAt())
//                .async(emailRequest.getAsync())
//                .message(message)
//                .ipPool(emailRequest.getIpPool())
//                .emailType(emailRequest.getEmailType())
//                .build();
//        HttpEntity<EmailRequestDTO> requestEntity = new HttpEntity<>(request,headers);
//        ResponseEntity<EmailResponseDto[]> responseEntity = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                requestEntity,
//                EmailResponseDto[].class
//        );
//        EmailResponseDto[] emailResponses = responseEntity.getBody();
//        assert emailResponses != null;
//        EmailEntity email = EmailEntity.builder()
//                .emailType(emailRequest.getEmailType())
//                .receiver(Arrays.stream(Objects.requireNonNull(responseEntity.getBody())).findFirst().get().getEmail())
//                .status(Arrays.stream(emailResponses).findFirst().get().getStatus())
//                .emailId(Arrays.stream(emailResponses).findFirst().get().getUserId())
//                .queuedReason(Arrays.stream(emailResponses).findFirst().get().getQueuedReason())
//                .rejectReason(Arrays.stream(emailResponses).findFirst().get().getRejectReason())
//                .build();
//        emailRepository.save(email);
//        return ResponseEntity.ok().body(emailResponses);
//
//    }
    @Override
    public ResponseDto<Map<String, String>> uploadHeaderLogo(HeaderLogoRequestDto headerLogoRequest) {
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", "redtech_HeaderLogo",
                "resource_type", "auto"
        );
        Map<?, ?> uploadResult = new HashMap<>();
        try {
            uploadResult = cloudinary.uploader().upload(headerLogoRequest.getHeaderLogo().getBytes(), uploadParams);
        } catch (Exception e) {
            throw new InternalServerException("error occurred: " + e.getMessage());
        }

        // Retrieve the URL and public ID from the upload result
        String url = (String) uploadResult.get("secure_url");  // HTTPS URL for the image
        String publicId = (String) uploadResult.get("public_id"); // Unique public ID

        //save it to the class
        HeaderLogoEntity headerLogo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        headerLogo.setUrl(url);
        headerLogo.setPublicId(publicId);
        headerLogoRepository.save(headerLogo);
// Create a response with both URL and publicId
        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        data.put("publicId", publicId);
        return ResponseDto.<Map<String, String>>builder()
                .data(data)
                .message("Image uploaded successfully!")
                .status(true)
                .statusCode(200)
                .build(); // Original URL
    }

    @Override
    public ResponseEntity<String> sendGridSimpleEmail(MailStructure mailStructure, List<MultipartFile> attachments) {
        Email from = new Email(fromEmail);
        String subject = mailStructure.getSubject();

        //set html content
        HeaderLogoEntity headerLogo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
        String logo = headerLogo.getUrl() == null ? "https://res.cloudinary.com/bunchbay/image/upload/v1731115765/redtech_HeaderLogo/byw0qnu5jr1cuzbxabmh.png" : headerLogo.getUrl();
        String htmlContent = mailStructure.getHtmlContent();
        htmlContent = Utils.generatePlainHtmlContent(htmlContent, logo);
        Content content = new Content("text/html", htmlContent);

        // Create the mail object and add the content
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);


        // Add multiple recipients using Personalization
        Personalization personalization = new Personalization();

        for (String recipientEmail : mailStructure.getRecipients()) {
            Email to = new Email(recipientEmail);
            personalization.addTo(to);
        }
        mail.addPersonalization(personalization);


        try {
            if (attachments != null) {
                for (MultipartFile attachment : attachments) {
                    Attachments sendGridAttachment = new Attachments();
                    sendGridAttachment.setContent(Base64.getEncoder().encodeToString(attachment.getBytes()));
                    sendGridAttachment.setType(attachment.getContentType());
                    sendGridAttachment.setFilename(attachment.getOriginalFilename());
                    sendGridAttachment.setDisposition("Attachment");
                    mail.addAttachments(sendGridAttachment);
                }
            }
            // Create a SendGrid client and send the email
            SendGrid sg = new SendGrid(key);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

            // Process response and save to repository if necessary
            EmailEntity email = EmailEntity.builder()
                    .message(mailStructure.getMessage())
                    .htmlContent(mailStructure.getHtmlContent())
                    .receiver(mailStructure.getRecipients())
                    .status(String.valueOf(response.getStatusCode()))
                    .emailId("")
                    .queuedReason(response.getBody())
                    .rejectReason("")
                    .build();
            emailRepository.save(email);

            return ResponseEntity.ok().body("Email sent Out Successfully");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseDto<List<EmailEntity>> getSentOutMails() {
        return ResponseDto.<List<EmailEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Successfully retrieved all sent out emails in the system")
                .data(emailRepository.findAll()).build();
    }


    public String getHeaderLogoUrl(String publicId) {
        // Create a transformed URL with the publicId
        return cloudinary.url()
                .transformation(new Transformation().width(600).height(150).crop("fill"))
                .generate(publicId);

    }

    private String getSubject(String subject) {
        if (subject == null || subject.isEmpty()) {
            throw new BadRequestException("Subject must not be empty");
        }
        return subject;
    }

    private String getHtml(String text) {
        if (text == null || text.isEmpty()) {
            throw new BadRequestException("Text or HTML must not be empty");
        }
        return text;
    }

    private String getFromName(String fromName) {
        if (fromName == null || fromName.isEmpty()) {
            throw new BadRequestException("Sender Name must not be empty");
        }
        return fromName;
    }

    private List<To> getTo(List<To> receiver) {
        if (receiver == null) {
            throw new BadRequestException("Receiver Email must not be empty");
        }
        return receiver;
    }


    public ResponseEntity<String> sendGridSimpleMail(EmailRequestDTO emailRequest) throws IOException {
        Email from = new Email(fromEmail);
        String subject = emailRequest.getMessage().getSubject();

        //set html content

        Content content = new Content("text/html", emailRequest.getMessage().getHtml());

        // Create the mail object and add the content
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);

        // Add multiple recipients using Personalization

        Personalization personalization = new Personalization();

        for (To recipientEmail : emailRequest.getMessage().getTo()) {
            Email to = new Email(recipientEmail.getEmail());
            personalization.addTo(to);
        }
        mail.addPersonalization(personalization);

        if (emailRequest.getMessage().getAttachments() != null) {
            for (Attachments attachments : emailRequest.getMessage().getAttachments()) {
                Attachments sendGridAttachment = new Attachments();
                sendGridAttachment.setContent(Base64.getEncoder().encodeToString(attachments.getContent().getBytes()));
                sendGridAttachment.setType(attachments.getType());
                sendGridAttachment.setFilename(attachments.getFilename());
                sendGridAttachment.setDisposition(attachments.getDisposition());
                mail.addAttachments(sendGridAttachment);
            }
        }
        // Create a SendGrid client and send the email
        SendGrid sg = new SendGrid(key);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        try {
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

            // Process response and save to repository if necessary
            EmailEntity email = EmailEntity.builder()
                    .emailType(emailRequest.getEmailType())
                    .receiver(emailRequest.getMessage().getTo().stream().map(To::getEmail).toList())
                    .status(String.valueOf(response.getStatusCode()))
                    .emailId("")
                    .queuedReason(response.getBody())
                    .rejectReason("")
                    .build();
            emailRepository.save(email);

            return ResponseEntity.ok().body("Email sent Out Successfully");

        } catch (IOException ex) {
            throw ex;
        }
    }


}
