package com.bbng.dao.microservices.report.dto;

import java.math.BigDecimal;


public record TopMerchantDTO(Long merchantId, String merchantName, BigDecimal totalVolume) {}
