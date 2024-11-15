package com.github.djoarns.payflow.application.mapper;

import com.github.djoarns.payflow.application.dto.response.BillResponseDTO;
import com.github.djoarns.payflow.application.result.BillResult;
import com.github.djoarns.payflow.domain.bill.Bill;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BillResponseMapper {
    public BillResponseDTO.Single toResponseDTO(Bill bill) {
        return new BillResponseDTO.Single(
                bill.getId().getValue(),
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
                result.totalPaid().getValue(),
                startDate,
                endDate
        );
    }
}