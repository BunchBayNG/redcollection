package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.CustomerSpecification;
import com.bbng.dao.microservices.report.dto.CustomerFilterRequestDto;
import com.bbng.dao.microservices.report.entity.CustomerEntity;
import com.bbng.dao.microservices.report.repository.CustomerRepository;
import com.bbng.dao.microservices.report.service.CustomerService;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class CustomerImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public ResponseDto<Page<CustomerEntity>>  getOrgCustomers(CustomerFilterRequestDto request) {
        Specification<CustomerEntity> spec = CustomerSpecification.getCustomers(request);

        Pageable pageable = getPageable(request);

        Page<CustomerEntity> page = customerRepository.findAll(spec, pageable);

        return ResponseDto.<Page<CustomerEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("vNUBANs fetched successfully")
                .data(page)
                .build();
    }


    private Pageable getPageable(CustomerFilterRequestDto request) {
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
