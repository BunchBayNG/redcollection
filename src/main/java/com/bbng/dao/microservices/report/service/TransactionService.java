package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.dto.TopMerchantDTO;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {



    ResponseDto<Page<TransactionEntity>>
    getTransactions(String search, String merchantOrgId, String status, String sortBy,
                    String sortOrder, LocalDate startDate, LocalDate endDate, int page, int size);

    long getTotalMerchantCount(LocalDateTime startDate, LocalDateTime endDate);
    List<TopMerchantDTO> getTopMerchantsByVolume(LocalDateTime startDate, LocalDateTime endDate, int topN);
    AnalyticsCountSummaryDTO getTransactionCountSummary(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    BigDecimal getSuccessfulTransactionVolume(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    double getSuccessfulTransactionRate(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    List<ChartPointDTO> getSuccessfulTransactionVolumeChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);
    List<ChartPointDTO> getSuccessfulTransactionCountChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);

}
