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


    void initiateSettlement(BigDecimal amount, String merchantName, String merchantOrgId,
                            String  sourceAccount, String destinationAccount, String status,
                            String  transactionRef, String reference, String settlementRef);
    ResponseDto<AnalyticsCountSummaryDTO> getSettlementCountSummary(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<BigDecimal> getSuccessfulSettlementVolume(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<Double> getSuccessfulSettlementRate(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<List<ChartPointDTO>> getSuccessfulSettlementVolumeChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<List<ChartPointDTO>> getSuccessfulSettlementCountChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);

}
