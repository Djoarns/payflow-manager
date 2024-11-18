package com.github.djoarns.payflow.application.bill.mapper;

import com.github.djoarns.payflow.application.bill.dto.response.BillResponseDTO;
import com.github.djoarns.payflow.application.bill.result.BillImportResult;
import com.github.djoarns.payflow.application.bill.result.BillResult;
import com.github.djoarns.payflow.domain.bill.Bill;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BillResponseMapper {
    public BillResponseDTO.Single toResponseDTO(Bill bill) {
        return new BillResponseDTO.Single(
                bill.getId() != null ? bill.getId().getValue() : null,
                bill.getDueDate().getValue(),
                bill.getPaymentDate() != null ? bill.getPaymentDate().getValue() : null,
                bill.getAmount().getValue(),
                bill.getDescription().getValue(),
                bill.getStatus().name()
        );
    }

    public BillResponseDTO.Page toPageDTO(BillResult.List result) {
        var content = result.bills().stream()
                .map(this::toResponseDTO)
                .toList();

        return new BillResponseDTO.Page(
                content,
                result.totalElements(),
                (int) result.getTotalPages(),
                result.currentPage(),
                result.pageSize(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    public BillResponseDTO.TotalPaid toTotalPaidDTO(
            BillResult.TotalPaid result,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return new BillResponseDTO.TotalPaid(
                result.totalPaid() != null ? result.totalPaid().getValue() : null,
                startDate,
                endDate
        );
    }

    public BillResponseDTO.Import toImportDTO(BillImportResult result) {
        return new BillResponseDTO.Import(
                result.totalProcessed(),
                result.successCount(),
                result.errorCount(),
                result.message()
        );
    }
}