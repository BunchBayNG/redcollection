package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.VnubanSpecification;
import com.bbng.dao.microservices.report.entity.VnubanEntity;
import com.bbng.dao.microservices.report.repository.VnubanRepository;
import com.bbng.dao.microservices.report.service.VnubanService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VnubanImpl implements VnubanService {

    private final VnubanRepository vnubanRepository;

    public VnubanImpl(VnubanRepository vnubanRepository) {
        this.vnubanRepository = vnubanRepository;
    }


    @Override
    public ResponseDto<Page<VnubanEntity>>  getVnubans(String vnubanType, String merchantName, String merchantOrgId, String vnuban,
                                                            LocalDateTime startDate, LocalDateTime endDate, String status,
                                                            String sortBy, boolean ascending, int page, int size) {

        //Specification<VnubanEntity> spec = Specification.where(null);

        Specification<VnubanEntity> spec = (root, query, builder) -> null;

        if (vnubanType != null) {
            spec = spec.and(VnubanSpecification.hasVnubanType(vnubanType));
        }
        if (merchantName != null) {
            spec = spec.and(VnubanSpecification.hasMerchantName(merchantName));
        }
        if (merchantOrgId != null) {
            spec = spec.and(VnubanSpecification.hasMerchantOrgId(merchantOrgId));
        }
        if (vnuban != null) {
            spec = spec.and(VnubanSpecification.hasVNuban(vnuban));
        }
        if (startDate != null && endDate != null) {
            spec = spec.and(VnubanSpecification.isBetweenTimestamps(startDate, endDate));
        }
        if (status != null) {
            spec = spec.and(VnubanSpecification.hasStatus(status));
        }

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return ResponseDto.<Page<VnubanEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("Password reset successfully")
                .data(vnubanRepository.findAll(spec, pageRequest))
                .build();
    }
}
