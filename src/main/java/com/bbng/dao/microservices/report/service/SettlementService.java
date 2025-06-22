package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SettlementService {



    ResponseDto<Page<SettlementEntity>>
    getSettlements(String search, String merchantOrgId, String status, String sortBy,
                   String sortOrder, LocalDate startDate, LocalDate endDate, int page, int size);

    AnalyticsCountSummaryDTO getSettlementCountSummary(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);

    double getSuccessfulSettlementRate(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    BigDecimal getSuccessfulSettlementVolume(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    List<ChartPointDTO> getSuccessfulSettlementVolumeChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);
    List<ChartPointDTO> getSuccessfulSettlementCountChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);


}
