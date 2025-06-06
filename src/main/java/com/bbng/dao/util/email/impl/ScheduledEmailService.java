package com.bbng.dao.util.email.impl;


import com.bbng.dao.microservices.auth.organization.entity.OrganizationEntity;
import com.bbng.dao.microservices.auth.organization.repository.OrganizationRepository;
import com.bbng.dao.microservices.auth.passport.entity.UserEntity;
import com.bbng.dao.microservices.auth.passport.repository.UserRepository;
import com.bbng.dao.util.email.dto.request.EmailRequestDTO;
import com.bbng.dao.util.email.dto.request.MailStructure;
import com.bbng.dao.util.email.dto.request.Message;
import com.bbng.dao.util.email.dto.request.To;
import com.bbng.dao.util.email.utils.Utils;
import com.bbng.dao.util.exceptions.customExceptions.InternalServerException;
import com.bbng.dao.util.fileUpload.entity.HeaderLogoEntity;
import com.bbng.dao.util.fileUpload.repository.HeaderLogoRepository;
import com.bbng.dao.util.fileUpload.services.PdfGenerator;
import com.sendgrid.helpers.mail.objects.Attachments;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledEmailService {

    private final EmailServiceImpl emailService;
    private final UserRepository userRepository;
    private final HeaderLogoRepository headerLogoRepository;
    private final JavaMailServiceImpl javaMailService;
    private final OrganizationRepository organizationRepository;

    @Scheduled(cron = "0 0 22 * * ?")  // 6 pm daily
//    @Scheduled(cron = "0 */5 * * * ?")
    public void sendDailReportEmails() throws MessagingException {
        //get all marchant email
        log.info("############################### fetching all the merchant from the table to send daily report to");
        List<UserEntity> merchants = userRepository.getAllUsers();


        for(UserEntity user: merchants){
           Optional<OrganizationEntity> organizationEntity = organizationRepository.findOrganizationByMerchantAdminId(user.getId());
           String organizationId = "";
           if(organizationEntity.isPresent()){
               organizationId = organizationEntity.get().getId();
           }
            EmailRequestDTO emailRequestDTO = buildEmailRequest(user.getEmail(), user, organizationId);
            javaMailService.sendGridHtmlContent(user.getEmail(), MailStructure.builder()
                    .htmlContent(emailRequestDTO.getMessage().getHtml())
                    .subject(emailRequestDTO.getMessage().getSubject())
                    .build(), emailRequestDTO.getMessage().getAttachments().stream().findFirst().get());
//            emailService.sendSimpleMail(emailRequestDTO);
        }
    }

    private EmailRequestDTO buildEmailRequest(String email, UserEntity user, String orgIdd) {
        String userName = user.getUserName();
        String reportDate = LocalDateTime.now().toString();
        LocalDate today = LocalDate.now();

        // Start of the day (UTC)
        Instant startOfDayInstant = today.atStartOfDay(ZoneId.of("UTC")).toInstant();


        // Current time (UTC)
        Instant currentTime = Instant.now();


        double successfulTransactionPercentage = 0.0;

        double failedTransactionPercentage = 0.0;
    log.info("generating Html contents");

    // get the public id;
        HeaderLogoEntity logo = headerLogoRepository.findById(1L).orElse(new HeaderLogoEntity());
       String url =  logo.getUrl();
       if (url == null || url.isEmpty()){
           url = "https://res.cloudinary.com/bunchbay/image/upload/v1731115765/redtech_HeaderLogo/byw0qnu5jr1cuzbxabmh.png";
       }


    //Generate Html content
//        String htmlContent = Utils.generateHtmlContent(userName, reportDate, totalTransactions,
//                totalCommissionEarns, totalWalletSpending, totalCommissionPercent, successfulTransactionPercentage,
//                failedTransactionPercentage, user.getEmail(), url);
        String htmlContent = "";

        //Build and emailRequestDto
        try {
            log.info("building emailRequestDto");
           ByteArrayOutputStream pdfFile =  PdfGenerator.createCommissionReportPdf(
                    url,
                    "Commission Report Receipt",
                    reportDate,
                    user.getFirstName() + " " + user.getLastName(),
                    0,
                    0,
                    0,
                    0

            );
           log.info("Adding attachment");
            Attachments pdfAttachment = new Attachments();
            pdfAttachment.setContent(Base64.getEncoder().encodeToString(pdfFile.toByteArray()));
            pdfAttachment.setType("application/pdf");
            pdfAttachment.setFilename("CommissionReport.pdf");
            pdfAttachment.setDisposition("attachment");
            EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
            emailRequestDTO.setEmailType("transaction-summary");
            emailRequestDTO.setSendAt("immediate");
            emailRequestDTO.setMessage(Message.builder()
                    .html(htmlContent)
                    .subject("Daily Transaction Summary")
                    .fromEmail("Info@bunchbay.com")
                    .fromName("BunchBay")
                    .attachments(Collections.singletonList(pdfAttachment))
                    .to(Collections.singletonList(To.builder()
                            .email(email)
                            .name(user.getFirstName())
                            .type("Transaction Report")
                            .build()))// Set merchant email here

                    .trackOpens("true")
                    .trackClicks("true")

                    .build());
            emailRequestDTO.setIpPool("Main Pool");
            emailRequestDTO.setAsync("false");

            return emailRequestDTO;
        }catch (Exception e){
            log.error("An error occurred while trying to send message: {}", e.getMessage());
            throw new InternalServerException(e.getMessage());
        }

    }

    private List<String> getMerchantEmails(List<UserEntity> merchants) {

        // get emails
        return merchants.stream().map(UserEntity::getEmail).toList();
    }
}
