package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.dto.TransactionFilterRequestDto;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface TransactionService {



    ResponseDto<Page<TransactionEntity>>  getTransactions(TransactionFilterRequestDto request);

}
