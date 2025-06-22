package com.bbng.dao.microservices.report.dto;

// For charts (daily/monthly/yearly)
public record ChartPointDTO(String period, Number value) {}