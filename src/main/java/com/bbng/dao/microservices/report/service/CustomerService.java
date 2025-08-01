package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.entity.CustomerEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface CustomerService {



    ResponseDto<Page<CustomerEntity>>
    getOrgCustomers(String search, String merchantOrgId, String status, String sortBy,
                    String sortOrder, LocalDate startDate, LocalDate endDate, int page, int size);

}
