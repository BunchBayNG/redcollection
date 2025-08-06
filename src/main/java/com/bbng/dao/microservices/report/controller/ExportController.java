package com.bbng.dao.microservices.report.controller;

import com.bbng.dao.microservices.auth.apilog.impl.ApiLogImpl;
import com.bbng.dao.microservices.auth.auditlog.service.AuditLogService;
import com.bbng.dao.microservices.auth.organization.impl.OrganizationServiceImpl;
import com.bbng.dao.microservices.auth.organization.service.OrganizationService;
import com.bbng.dao.microservices.report.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
@RequestMapping("${apiVersion}" + "/exports")
public class ExportController {

    private final TransactionService transactionService;
    private final AuditLogService auditLogService;
    private final ApiLogImpl apiLogService;
    private final OrganizationServiceImpl organizationService;
    private final PayoutService payoutService;
    private final SettlementService settlementService;
    private final VnubanService vnubanService;

    public ExportController(TransactionService transactionService, AuditLogService auditLogService, ApiLogImpl apiLogService, OrganizationServiceImpl organizationService, PayoutService payoutService,
                            SettlementService settlementService, VnubanService vnubanService) {
        this.transactionService = transactionService;
        this.auditLogService = auditLogService;
        this.apiLogService = apiLogService;
        this.organizationService = organizationService;
        this.payoutService = payoutService;
        this.settlementService = settlementService;
        this.vnubanService = vnubanService;
    }


    @GetMapping("/transactions/excel")
    public ResponseEntity<byte[]> exportTransactionsToExcel(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = transactionService.exportToExcel(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=transactions.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(data);
    }

    @GetMapping("/transactions/pdf")
    public ResponseEntity<byte[]> exportTransactionsToPdf(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = transactionService.exportToPdf(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=transactions.pdf")
                .header("Content-Type", "application/pdf")
                .body(data);
    }

    @GetMapping("/transactions/csv")
    public ResponseEntity<byte[]> exportTransactionsToCSV(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) {

        byte[] data = transactionService.exportToCsv(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=transactions.csv")
                .header("Content-Type", "text/csv")
                .body(data);
    }



    ///#########################################################################


    @GetMapping("/vnuban/excel")
    public ResponseEntity<byte[]> exportVnubansToExcel(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = vnubanService.exportToExcel(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=vnuban.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(data);
    }

    @GetMapping("/vnuban/pdf")
    public ResponseEntity<byte[]> exportVnubansToPdf(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = vnubanService.exportToPdf(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=vnuban.pdf")
                .header("Content-Type", "application/pdf")
                .body(data);
    }

    @GetMapping("/vnuban/csv")
    public ResponseEntity<byte[]> exportVnubansToCSV(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) {

        byte[] data = vnubanService.exportToCsv(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=vnuban.csv")
                .header("Content-Type", "text/csv")
                .body(data);
    }






    ///#########################################################################







    @GetMapping("/settlements/excel")
    public ResponseEntity<byte[]> exportSettlementsToExcel(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = settlementService.exportToExcel(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=settlements.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(data);
    }

    @GetMapping("/settlements/pdf")
    public ResponseEntity<byte[]> exportSettlementsToPdf(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = settlementService.exportToPdf(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=settlements.pdf")
                .header("Content-Type", "application/pdf")
                .body(data);
    }

    @GetMapping("/settlements/csv")
    public ResponseEntity<byte[]> exportSettlementsToCSV(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) {

        byte[] data = settlementService.exportToCsv(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=settlements.csv")
                .header("Content-Type", "text/csv")
                .body(data);
    }


    ///#########################################################################



    @GetMapping("/payouts/excel")
    public ResponseEntity<byte[]> exportPayoutsToExcel(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = payoutService.exportToExcel(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=payouts.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(data);
    }

    @GetMapping("/payouts/pdf")
    public ResponseEntity<byte[]> exportPayoutsToPdf(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = payoutService.exportToPdf(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=payouts.pdf")
                .header("Content-Type", "application/pdf")
                .body(data);
    }

    @GetMapping("/payouts/csv")
    public ResponseEntity<byte[]> exportPayoutsToCSV(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) {

        byte[] data = payoutService.exportToCsv(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=payouts.csv")
                .header("Content-Type", "text/csv")
                .body(data);
    }


    ///#########################################################################



    @GetMapping("/api-logs/excel")
    public ResponseEntity<byte[]> exportApiLogsToExcel(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = apiLogService.exportToExcel(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=api-logs.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(data);
    }

    @GetMapping("/api-logs/pdf")
    public ResponseEntity<byte[]> exportApiLogsToPdf(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = apiLogService.exportToPdf(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=api-logs.pdf")
                .header("Content-Type", "application/pdf")
                .body(data);
    }

    @GetMapping("/api-logs/csv")
    public ResponseEntity<byte[]> exportApiLogsToCSV(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) {

        byte[] data = apiLogService.exportToCsv(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=api-logs.csv")
                .header("Content-Type", "text/csv")
                .body(data);
    }

    ///#########################################################################


    @GetMapping("/audit-logs/excel")
    public ResponseEntity<byte[]> exportAuditLogsToExcel(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = auditLogService.exportToExcel(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=audit-logs.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(data);
    }

    @GetMapping("/audit-logs/pdf")
    public ResponseEntity<byte[]> exportAuditLogsToPdf(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = auditLogService.exportToPdf(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=audit-logs.pdf")
                .header("Content-Type", "application/pdf")
                .body(data);
    }

    @GetMapping("/audit-logs/csv")
    public ResponseEntity<byte[]> exportAuditLogsToCSV(
            @RequestParam(required = false) String merchantOrgId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) {

        byte[] data = auditLogService.exportToCsv(merchantOrgId, startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=audit-logs.csv")
                .header("Content-Type", "text/csv")
                .body(data);
    }



    ///#########################################################################

    @GetMapping("/merchants/excel")
    public ResponseEntity<byte[]> exportOrganizationsToExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = organizationService.exportToExcel( startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=merchants.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(data);
    }

    @GetMapping("/merchants/pdf")
    public ResponseEntity<byte[]> exportOrganizationsToPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) throws Exception {

        byte[] data = organizationService.exportToPdf( startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=merchants.pdf")
                .header("Content-Type", "application/pdf")
                .body(data);
    }

    @GetMapping("/merchants/csv")
    public ResponseEntity<byte[]> exportOrganizationsToCSV(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ) {

        byte[] data = organizationService.exportToCsv( startDate, endDate);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=merchants.csv")
                .header("Content-Type", "text/csv")
                .body(data);
    }





}
