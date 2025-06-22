//package com.bbng.dao.microservices.report.dto;
//
//
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Data
//public class SettlementFilterRequestDto {
//    private String search; // Transaction ID, Merchant Name or vNUBAN
//    private String status;
//    private LocalDate startDate;
//    private LocalDate endDate;
//    private String merchantOrgId;
//
//    private String sortBy;     // e.g. merchantName, destinationAccountName, status
//    private String sortOrder;  // ASC, DESC, ACTIVE_FIRST, INACTIVE_FIRST
//
//    private int page = 0;      // default page
//    private int size = 10;     // default size
//}