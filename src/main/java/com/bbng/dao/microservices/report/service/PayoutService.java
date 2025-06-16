package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.dto.PayoutFilterRequestDto;
import com.bbng.dao.microservices.report.entity.PayoutEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface PayoutService {


    ResponseDto<Page<PayoutEntity>>  getPayouts(PayoutFilterRequestDto request);

}
