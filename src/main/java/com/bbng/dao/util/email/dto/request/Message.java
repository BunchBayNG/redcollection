package com.bbng.dao.util.email.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.helpers.mail.objects.Attachments;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String html;
    @NotEmpty(message = "Text should not be empty")
    private String text;
    @NotEmpty(message = "Subject should not be empty")
    private String subject;
    @NotEmpty(message = "Sender Email should not be empty")
    @JsonProperty("from_email")
    private String fromEmail;
    @NotEmpty(message = "Sender Name should not be empty")
    @JsonProperty("from_name")
    private String fromName;
    private List<To> to;
    private Headers headers;
    private String important;
    @JsonProperty("track_opens")
    private String trackOpens;
    @JsonProperty("track_clicks")
    private String trackClicks;
    @JsonProperty("auto_text")
    private String autoText;
    @JsonProperty("auto_html")
    private String autoHtml;
    @JsonProperty("inline_css")
    private String inlineCss;
    @JsonProperty("url_strip_qs")
    private String urlStripQs;
    @JsonProperty("preserve_recipients")
    private String preserveRecipients;
    @JsonProperty("view_content_link")
    private String viewContentLink;
    @JsonProperty("bbc_address")
    private String bccAddress;
    @JsonProperty("tracking_domain")
    private String trackingDomain;
    @JsonProperty("signing_domain")
    private String signingDomain;
    @JsonProperty("return_path_domain")
    private String returnPathDomain;
    private String merge;
    @JsonProperty("merge_language")
    private String mergeLanguage;
    @JsonProperty("global_merge_vars")
    private Object globalMergeVars;
    @JsonProperty("merge_vars")
    private Object mergeVars;
    private Object tags;
    private String subaccount;
    @JsonProperty("google_analytics_domain")
    private Object googleAnalyticsDomains;
    @JsonProperty("google_analytics_campaign")
    private Object googleAnalyticsCampaign;
    private Metadata metadata;
    @JsonProperty("recipient_metadata")
    private Object recipientMetadata;
    private List<Attachments> attachments;
    private Object images;


}
