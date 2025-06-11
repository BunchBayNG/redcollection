package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface SettlementService {



    ResponseDto<Page<SettlementEntity>>  getSettlements( String sourceAccount, String merchantName, String merchantOrgId,  String destinationAccount,
                                                         String transactionRef,  String reference, LocalDateTime startDate, LocalDateTime endDate, String status,
                                                        String sortBy, boolean ascending, int page, int size);

}
