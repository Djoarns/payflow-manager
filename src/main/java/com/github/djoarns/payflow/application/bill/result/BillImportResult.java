package com.github.djoarns.payflow.application.bill.result;

public record BillImportResult(
        int totalProcessed,
        int successCount,
        int errorCount,
        String message
) {}