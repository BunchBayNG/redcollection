package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.CustomerSpecification;
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

import java.time.LocalDate;


@Service
public class CustomerImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public ResponseDto<Page<CustomerEntity>>  getOrgCustomers(String search, String merchantOrgId, String status,
                                                              String sortBy, String sortOrder, LocalDate startDate,
                                                              LocalDate endDate, int page, int size) {
        Specification<CustomerEntity> spec =
                CustomerSpecification.getCustomers(search, merchantOrgId, status, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);

        Page<CustomerEntity> response = customerRepository.findAll(spec, pageable);

        return ResponseDto.<Page<CustomerEntity>>builder()
                .statusCode(200)
                .status(true)
                .message("vNUBANs fetched successfully")
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
