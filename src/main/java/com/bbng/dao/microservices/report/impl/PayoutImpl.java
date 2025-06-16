package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.PayoutSpecification;
import com.bbng.dao.microservices.report.dto.PayoutFilterRequestDto;
import com.bbng.dao.microservices.report.entity.PayoutEntity;
import com.bbng.dao.microservices.report.repository.PayoutRepository;
import com.bbng.dao.microservices.report.service.PayoutService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PayoutImpl implements PayoutService {

    private final PayoutRepository payoutRepository;

    public PayoutImpl(PayoutRepository payoutRepository) {
        this.payoutRepository = payoutRepository;
    }


    @Override
    public ResponseDto<Page<PayoutEntity>>  getPayouts(PayoutFilterRequestDto request) {
        Specification<PayoutEntity> spec = PayoutSpecification.getPayouts(request);

        Pageable pageable = getPageable(request);

        Page<PayoutEntity> page = payoutRepository.findAll(spec, pageable);

        return ResponseDto.<Page<PayoutEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Payouts fetched successfully")
                .data(page)
                .build();
    }


    private Pageable getPageable(PayoutFilterRequestDto request) {
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "createdAt";
        String sortOrder = request.getSortOrder() != null ? request.getSortOrder().toUpperCase() : "DESC";

        Sort sort = switch (sortOrder) {
            case "ASC" -> Sort.by(Sort.Direction.ASC, sortBy);
            case "DESC" -> Sort.by(Sort.Direction.DESC, sortBy);
            case "ACTIVE_FIRST" -> Sort.by(Sort.Order.desc("status"));
            case "INACTIVE_FIRST" -> Sort.by(Sort.Order.asc("status"));
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };

        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
}


