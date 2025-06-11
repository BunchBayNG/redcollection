package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.entity.PayoutEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface PayoutService {


    ResponseDto<Page<PayoutEntity>>  getPayouts(String sourceAccount, String merchantName, String merchantOrgId, String destinationAccount,
                                                String transactionRef, String paymentReference, LocalDateTime startDate, LocalDateTime endDate, String status,
                                                String sortBy, boolean ascending, int page, int size);

}
