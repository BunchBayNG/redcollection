package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.SettlementSpecification;
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

import java.time.LocalDate;

@Service
public class SettlementImpl implements SettlementService {

    private final SettlementRepository settlementRepository;

    public SettlementImpl(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    @Override
    public ResponseDto<Page<SettlementEntity>>  getSettlements(String search, String merchantOrgId, String status,
                                                               String sortBy, String sortOrder, LocalDate startDate,
                                                               LocalDate endDate, int page, int size) {
        Specification<SettlementEntity> spec =
                SettlementSpecification.getSettlements(search, merchantOrgId, status, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);

        Page<SettlementEntity> response = settlementRepository.findAll(spec, pageable);

        return ResponseDto.<Page<SettlementEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Settlements fetched successfully")
                .data(response)
                .build();
    }


    private Pageable getPageable(String sortBy, String sortOrder, int page, int size) {
        String  defaultSortBy = sortBy != null ? sortBy : "createdAt";
        String defaultSortOrder = sortOrder != null ? sortOrder.toUpperCase() : "DESC";

        Sort sort = switch (defaultSortOrder) {
            case "ASC" -> Sort.by(Sort.Direction.ASC, defaultSortBy);
            case "DESC" -> Sort.by(Sort.Direction.DESC, defaultSortBy);
            case "ACTIVE_FIRST" -> Sort.by(Sort.Order.desc("status"));
            case "INACTIVE_FIRST" -> Sort.by(Sort.Order.asc("status"));
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };

        return PageRequest.of(page, size, sort);
    }

    
}
