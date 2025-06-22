package com.bbng.dao.microservices.report.dto;

// For count summary (SUCCESS, PENDING, FAILED)
public record AnalyticsCountSummaryDTO(long total, long success, long pending, long failed) {}
