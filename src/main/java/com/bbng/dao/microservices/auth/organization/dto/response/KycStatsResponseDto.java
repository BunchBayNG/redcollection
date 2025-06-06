package com.bbng.dao.microservices.auth.organization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycStatsResponseDto {
    private long totalKycCase;
    private int totalApprovedCase;
    private int totalPendingCase;
    private int totalDeclineCase;
}
