package com.github.djoarns.payflow.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public sealed interface BillResponseDTO {
    record Single(
            Long id,
            LocalDate dueDate,
            LocalDate paymentDate,
            BigDecimal amount,
            String description,
            String status
    ) implements BillResponseDTO {}

    record Page(
            List<Single> content,
            long totalElements,
            int totalPages,
            int page,
            int size,
            boolean hasNext,
            boolean hasPrevious
    ) implements BillResponseDTO {}

    record TotalPaid(
            BigDecimal totalAmount,
            LocalDate startDate,
            LocalDate endDate
    ) implements BillResponseDTO {}
}