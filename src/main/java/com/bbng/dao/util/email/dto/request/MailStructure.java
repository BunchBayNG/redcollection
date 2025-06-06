package com.bbng.dao.util.email.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailStructure {
    private String subject;
    private String message;
    private String htmlContent;
    private List<String> recipients;
}
