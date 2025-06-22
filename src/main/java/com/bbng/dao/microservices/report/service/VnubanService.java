package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.dto.ChartPointDTO;
import com.bbng.dao.microservices.report.entity.VnubanEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VnubanService {



    ResponseDto<Page<VnubanEntity>>
    getVnubans(String search, String merchantOrgId, String status, String sortBy,
               String sortOrder, LocalDate startDate, LocalDate endDate, int page, int size);

    long getTotalVnubans(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    long getTotalStaticVnubans(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    long getTotalDynamicVnubans(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate);
    List<ChartPointDTO> getGeneratedVnubansChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate);


}
