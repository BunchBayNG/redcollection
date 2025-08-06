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


    byte[] exportToExcel(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] exportToPdf(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] exportToCsv(String merchantOrgId, LocalDate startDate, LocalDate endDate);

    ResponseDto<AnalyticsCountSummaryDTO> getPayoutCountSummary(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<BigDecimal> getSuccessfulPayoutVolume(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<Double> getSuccessfulPayoutRate(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<List<ChartPointDTO>> getSuccessfulPayoutVolumeChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<List<ChartPointDTO>> getSuccessfulPayoutCountChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);
    



}
