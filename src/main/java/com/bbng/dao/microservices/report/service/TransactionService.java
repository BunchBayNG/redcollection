package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.dto.AnalyticsCountSummaryDTO;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.dto.TopMerchantDTO;
import com.bbng.dao.microservices.report.dto.TransactionRequestDTO;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {



    ResponseDto<Page<TransactionEntity>>
    getTransactions(String search, String merchantOrgId, String status, String sortBy, String sortOrder, LocalDate startDate, LocalDate endDate, int page, int size);
    byte[] exportToExcel(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] exportToPdf(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] exportToCsv(String merchantOrgId, LocalDate startDate, LocalDate endDate);

    ResponseDto<TransactionEntity> createTransaction(TransactionRequestDTO requestDTO);



    ResponseDto<List<TopMerchantDTO>> getTopMerchantsByVolume(LocalDateTime startDate, LocalDateTime endDate, int topN);
    ResponseDto<AnalyticsCountSummaryDTO> getTransactionCountSummary(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<BigDecimal> getSuccessfulTransactionVolume(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<List<ChartPointDTO>> getSuccessfulTransactionVolumeChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<List<ChartPointDTO>> getSuccessfulTransactionCountChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<Double> getSuccessfulTransactionRate(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);


}
