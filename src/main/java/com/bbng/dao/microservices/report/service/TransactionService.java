package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface TransactionService {



    ResponseDto<Page<TransactionEntity>>  getTransactions(String transactionId, String merchantName, String merchantOrgId,
                                                          String vNuban,
                                                   LocalDateTime startDate, LocalDateTime endDate, String status,
                                                   String sortBy, boolean ascending, int page, int size);

}
