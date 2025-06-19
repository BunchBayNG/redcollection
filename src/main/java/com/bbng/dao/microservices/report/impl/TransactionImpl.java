package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.TransactionSpecification;
import com.bbng.dao.microservices.report.dto.TransactionFilterRequestDto;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.repository.TransactionRepository;
import com.bbng.dao.microservices.report.service.TransactionService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class TransactionImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ResponseDto<Page<TransactionEntity>>  getTransactions(TransactionFilterRequestDto request) {
        Specification<TransactionEntity> spec = TransactionSpecification.getTransactions(request);

        Pageable pageable = getPageable(request);
        
        Page<TransactionEntity> page = transactionRepository.findAll(spec, pageable);

        return ResponseDto.<Page<TransactionEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Transactions fetched successfully")
                .data(page)
                .build();
    }




    private Pageable getPageable(TransactionFilterRequestDto request) {
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
