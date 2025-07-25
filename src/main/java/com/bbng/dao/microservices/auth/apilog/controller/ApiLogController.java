package com.bbng.dao.microservices.auth.apilog.controller;

import com.bbng.dao.microservices.auth.apilog.dto.request.ApiLogFilterRequest;
import com.bbng.dao.microservices.auth.apilog.entity.ApiLogEntity;
import com.bbng.dao.microservices.auth.apilog.impl.ApiLogSpecification;
import com.bbng.dao.microservices.auth.apilog.repository.ApiLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("${apiVersion}" + "/api-logs")
public class ApiLogController {

    @Autowired
    private ApiLogRepository apiLogRepository;

    @PostMapping("/fetch")
    public ResponseEntity<Page<ApiLogEntity>> searchApiLogs(@RequestBody ApiLogFilterRequest request) {
        Specification<ApiLogEntity> spec = ApiLogSpecification.getLogs(request);
        Pageable pageable = getPageable(request);
        Page<ApiLogEntity> page = apiLogRepository.findAll(spec, pageable);
        return ResponseEntity.ok(page);
    }

    private Pageable getPageable(ApiLogFilterRequest request) {
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "timestamp";
        String sortOrder = request.getSortOrder() != null ? request.getSortOrder().toUpperCase() : "DESC";

        Sort sort = sortOrder.equals("ASC") ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);

        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
}
