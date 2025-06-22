package com.bbng.dao.microservices.report.impl;

import com.bbng.dao.microservices.report.config.VnubanSpecification;
import com.bbng.dao.microservices.report.dto.ChartPointDTO;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class VnubanImpl implements VnubanService {

    private final VnubanRepository vnubanRepository;

    public VnubanImpl(VnubanRepository vnubanRepository) {
        this.vnubanRepository = vnubanRepository;
    }



    @Override
    public ResponseDto<Page<VnubanEntity>>  getVnubans(String search, String merchantOrgId, String status,
                                                       String sortBy, String sortOrder, LocalDate startDate,
                                                       LocalDate endDate, int page, int size) {
        Specification<VnubanEntity> spec =
                VnubanSpecification.getVnubans(search, merchantOrgId, status, startDate, endDate);

        Pageable pageable = getPageable(sortBy, sortOrder, page, size);

        Page<VnubanEntity> response = vnubanRepository.findAll(spec, pageable);

        return ResponseDto.<Page<VnubanEntity>>builder()
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


    public long getTotalVnubans(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        return vnubanRepository.countByGeneratedAtBetween(merchantOrgId, startDate, endDate);
    }


    public long getTotalStaticVnubans(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        return vnubanRepository.countByTypeAndGeneratedAtBetween(merchantOrgId, "STATIC", startDate, endDate);
    }

    public long getTotalDynamicVnubans(Long merchantOrgId, LocalDateTime startDate, LocalDateTime endDate) {
        return vnubanRepository.countByTypeAndGeneratedAtBetween(merchantOrgId, "DYNAMIC", startDate, endDate);
    }

//    public List<ChartPointDTO> getGeneratedVnubansChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {
//        return vnubanRepository.groupGeneratedVnubansByPeriod(merchantOrgId, pattern, startDate, endDate);
//    }

    public List<ChartPointDTO> getGeneratedVnubansChart(Long merchantOrgId, String pattern, LocalDateTime startDate, LocalDateTime endDate) {

        List<Object[]> rawResult =  vnubanRepository.groupGeneratedVnubansByPeriod(merchantOrgId, pattern, startDate, endDate);
        List<ChartPointDTO> result = rawResult.stream()
                .map(row -> new ChartPointDTO(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
        return result;
    }







}
