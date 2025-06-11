package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.SettlementSpecification;
import com.bbng.dao.microservices.report.entity.SettlementEntity;
import com.bbng.dao.microservices.report.repository.SettlementRepository;
import com.bbng.dao.microservices.report.service.SettlementService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SettlementImpl implements SettlementService {

    private final SettlementRepository settlementRepository;

    public SettlementImpl(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }


    @Override
    public ResponseDto<Page<SettlementEntity>>  getSettlements( String sourceAccount, String merchantName,
                                                                String merchantOrgId,  String destinationAccount,
                                                                String transactionRef, String reference, LocalDateTime startDate, LocalDateTime endDate, String status,
                                                                String sortBy, boolean ascending, int page, int size) {

        //Specification<SettlementEntity> spec = Specification.where(null);

        Specification<SettlementEntity> spec = (root, query, builder) -> null;

        if (sourceAccount != null) {
            spec = spec.and(SettlementSpecification.hasSourceAccount(sourceAccount));
        }

        if (destinationAccount != null) {
            spec = spec.and(SettlementSpecification.hasDestinationAccount(destinationAccount));
        }
        if (merchantName != null) {
            spec = spec.and(SettlementSpecification.hasMerchantName(merchantName));
        }

        if (merchantOrgId != null) {
            spec = spec.and(SettlementSpecification.hasMerchantOrgId(merchantOrgId));
        }
        if (transactionRef != null) {
            spec = spec.and(SettlementSpecification.hasTransactionRef(transactionRef));
        }

        if (reference != null) {
            spec = spec.and(SettlementSpecification.hasReference(reference));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and(SettlementSpecification.isBetweenTimestamps(startDate, endDate));
        }
        if (status != null) {
            spec = spec.and(SettlementSpecification.hasStatus(status));
        }

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return ResponseDto.<Page<SettlementEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Password reset successfully")
                .data(settlementRepository.findAll(spec, pageRequest))
                .build();
    }
}
