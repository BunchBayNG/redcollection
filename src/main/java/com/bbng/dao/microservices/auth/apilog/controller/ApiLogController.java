package com.bbng.dao.microservices.auth.apilog.controller;

import com.bbng.dao.microservices.auth.apilog.entity.ApiLogEntity;
import com.bbng.dao.microservices.auth.apilog.impl.ApiLogImpl;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("${apiVersion}" + "/api-logs")

public class ApiLogController {

    private final ApiLogImpl apiLogImpl;



    private final String defaultPage = "0";      // default page
    private final String defaultSize = "10";     // default size
    private final String defaultSortOrder = "DESC";     // default sort order, Ascending or Descending


    public ApiLogController(ApiLogImpl apiLogImpl) {
        this.apiLogImpl = apiLogImpl;
    }


    @GetMapping("/fetch")
    public ResponseEntity<ResponseDto<Page<ApiLogEntity>>> searchApiLogs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = defaultSortOrder) String sortOrder,
            @RequestParam(defaultValue = defaultPage) int page,
            @RequestParam(defaultValue = defaultSize) int size
    ) {

        return  ResponseEntity.status(HttpStatus.OK).body(apiLogImpl
                .getApiLogs(search, merchantOrgId,status, sortBy, sortOrder, startDate, endDate, page, size ));
    }
}
