package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.TransactionSpecification;
import com.bbng.dao.microservices.report.entity.TransactionEntity;
import com.bbng.dao.microservices.report.repository.TransactionRepository;
import com.bbng.dao.microservices.report.service.TransactionService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ResponseDto<Page<TransactionEntity>>  getTransactions(String transactionId, String merchantName,  String merchantOrgId,  String vNuban,
                                                                LocalDateTime startDate, LocalDateTime endDate, String status,
                                                                String sortBy, boolean ascending, int page, int size) {

        //Specification<TransactionEntity> spec = Specification.where(null);

        Specification<TransactionEntity> spec = (root, query, builder) -> null;

        if (transactionId != null) {
            spec = spec.and(TransactionSpecification.hasTransactionId(transactionId));
        }
        if (merchantName != null) {
            spec = spec.and(TransactionSpecification.hasMerchantName(merchantName));
        }
        if (merchantOrgId != null) {
            spec = spec.and(TransactionSpecification.hasMerchantOrgId(merchantOrgId));
        }
        if (vNuban != null) {
            spec = spec.and(TransactionSpecification.hasVNuban(vNuban));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and(TransactionSpecification.isBetweenTimestamps(startDate, endDate));
        }
        if (status != null) {
            spec = spec.and(TransactionSpecification.hasStatus(status));
        }

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return ResponseDto.<Page<TransactionEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Transactions fetched successfully")
                .data(transactionRepository.findAll(spec, pageRequest))
                .build();
    }
}
