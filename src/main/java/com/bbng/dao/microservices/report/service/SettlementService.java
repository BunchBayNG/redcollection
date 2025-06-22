package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface SettlementService {



    ResponseDto<Page<SettlementEntity>>
    getSettlements(String search, String merchantOrgId, String status, String sortBy,
                   String sortOrder, LocalDate startDate, LocalDate endDate, int page, int size);

}
