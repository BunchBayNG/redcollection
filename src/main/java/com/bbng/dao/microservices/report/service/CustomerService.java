package com.bbng.dao.microservices.report.service;

import com.bbng.dao.microservices.report.dto.CustomerFilterRequestDto;

import com.bbng.dao.microservices.report.entity.CustomerEntity;
import com.bbng.dao.util.response.ResponseDto;
import org.springframework.data.domain.Page;

public interface CustomerService {



    ResponseDto<Page<CustomerEntity>>  getOrgCustomers(CustomerFilterRequestDto request);

}
