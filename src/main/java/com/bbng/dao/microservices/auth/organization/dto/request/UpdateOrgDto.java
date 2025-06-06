package com.bbng.dao.microservices.auth.organization.dto.request;


import com.bbng.dao.microservices.auth.organization.enums.OrgStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateOrgDto {

    private String organizationId;
    private String organizationName;
    private String contactName;
    private String contactEmail;
    private OrgStatus status;
    private String productPrefix;
}
