package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.PayoutSpecification;
import com.bbng.dao.microservices.report.entity.PayoutEntity;
import com.bbng.dao.microservices.report.repository.PayoutRepository;
import com.bbng.dao.microservices.report.service.PayoutService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseDto<Page<PayoutEntity>>  getPayouts(String sourceAccount, String merchantName, String merchantOrgId, String destinationAccount,
                                                       String transactionRef, String paymentReference, LocalDateTime startDate, LocalDateTime endDate, String status,
                                                       String sortBy, boolean ascending, int page, int size) {

        //Specification<SettlementEntity> spec = Specification.where(null);

        Specification<PayoutEntity> spec = (root, query, builder) -> null;

        if (sourceAccount != null) {
            spec = spec.and(PayoutSpecification.hasSourceAccount(sourceAccount));
        }

        if (destinationAccount != null) {
            spec = spec.and(PayoutSpecification.hasDestinationAccount(destinationAccount));
        }
        if (merchantName != null) {
            spec = spec.and(PayoutSpecification.hasMerchantName(merchantName));
        }

        if (merchantOrgId != null) {
            spec = spec.and(PayoutSpecification.hasMerchantOrgId(merchantOrgId));
        }
        if (transactionRef != null) {
            spec = spec.and(PayoutSpecification.hasTransactionRef(transactionRef));
        }

        if (paymentReference != null) {
            spec = spec.and(PayoutSpecification.hasPaymentReference(paymentReference));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and(PayoutSpecification.isBetweenTimestamps(startDate, endDate));
        }
        if (status != null) {
            spec = spec.and(PayoutSpecification.hasStatus(status));
        }

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return ResponseDto.<Page<PayoutEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Payouts fetched successfully")
                .data(payoutRepository.findAll(spec, pageRequest))
                .build();
    }
}


