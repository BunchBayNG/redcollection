package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.dto.SettlementFilterRequestDto;
import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface SettlementService {



    ResponseDto<Page<SettlementEntity>>  getSettlements( SettlementFilterRequestDto request);

}
