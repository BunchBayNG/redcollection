package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.SettlementSpecification;
import com.bbng.dao.microservices.report.dto.SettlementFilterRequestDto;
import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.microservices.report.repository.SettlementRepository;
import com.bbng.dao.microservices.report.service.SettlementService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SettlementImpl implements SettlementService {

    private final SettlementRepository settlementRepository;

    public SettlementImpl(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    @Override
    public ResponseDto<Page<SettlementEntity>>  getSettlements(SettlementFilterRequestDto request) {
        Specification<SettlementEntity> spec = SettlementSpecification.getSettlements(request);

        Pageable pageable = getPageable(request);

        Page<SettlementEntity> page = settlementRepository.findAll(spec, pageable);

        return ResponseDto.<Page<SettlementEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Settlements fetched successfully")
                .data(page)
                .build();
    }


    private Pageable getPageable(SettlementFilterRequestDto request) {
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
