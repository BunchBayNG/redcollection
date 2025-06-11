package com.bbng.dao.microservices.auth.organization.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OnboardOrgDto {

    private String inviteUserId;
    private String organizationName;
    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;
    private String phoneNumber;
    private String registeredBVN;
    private String businessLogoUrl;
    private String productPrefix;
}
