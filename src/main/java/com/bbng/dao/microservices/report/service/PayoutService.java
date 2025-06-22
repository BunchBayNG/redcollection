package com.bbng.dao.microservices.report.service;


import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.entity.PayoutEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PayoutService {


    ResponseDto<Page<PayoutEntity>>
    getPayouts(String search, String merchantOrgId, String status, String sortBy,
               String sortOrder, LocalDate startDate, LocalDate endDate, int page, int size);
    
    
    AnalyticsCountSummaryDTO getPayoutCountSummary(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    double getSuccessfulPayoutRate(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    BigDecimal getSuccessfulPayoutVolume(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    List<ChartPointDTO> getSuccessfulPayoutVolumeChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);
    List<ChartPointDTO> getSuccessfulPayoutCountChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);
   

}
