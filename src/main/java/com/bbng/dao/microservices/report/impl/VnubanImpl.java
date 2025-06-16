package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.VnubanSpecification;
import com.bbng.dao.microservices.report.dto.VnubanFilterRequestDto;
import com.bbng.dao.microservices.report.entity.VnubanEntity;
import com.bbng.dao.microservices.report.repository.VnubanRepository;
import com.bbng.dao.microservices.report.service.VnubanService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class VnubanImpl implements VnubanService {

    private final VnubanRepository vnubanRepository;

    public VnubanImpl(VnubanRepository vnubanRepository) {
        this.vnubanRepository = vnubanRepository;
    }



    @Override
    public ResponseDto<Page<VnubanEntity>>  getVnubans(VnubanFilterRequestDto request) {
        Specification<VnubanEntity> spec = VnubanSpecification.getVnubans(request);

        Pageable pageable = getPageable(request);

        Page<VnubanEntity> page = vnubanRepository.findAll(spec, pageable);

        return ResponseDto.<Page<VnubanEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("vNUBANs fetched successfully")
                .data(page)
                .build();
    }


    private Pageable getPageable(VnubanFilterRequestDto request) {
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
