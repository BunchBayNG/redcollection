package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.vacctgen.entity.ProvisionedAccount;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VnubanService {



    ResponseDto<Page<ProvisionedAccount>>
    getVnubans(String search, String merchantOrgId, String status, String sortBy,
               String sortOrder, LocalDate startDate, LocalDate endDate, int page, int size);

    byte[] exportToExcel(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] exportToPdf(String merchantOrgId, LocalDate startDate, LocalDate endDate) throws Exception;
    byte[] exportToCsv(String merchantOrgId, LocalDate startDate, LocalDate endDate);

    ResponseDto<Long>  getTotalVnubans(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<Long>  getTotalStaticVnubans(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<Long> getTotalDynamicVnubans(String merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    ResponseDto<List<ChartPointDTO>>  getGeneratedVnubansChart(String merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);

}
