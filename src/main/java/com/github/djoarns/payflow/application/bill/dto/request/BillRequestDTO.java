package com.github.djoarns.payflow.application.bill.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public sealed interface BillRequestDTO {
    record Create(
            @NotNull(message = "Due date is required")
            LocalDate dueDate,

            @NotNull(message = "Amount is required")
            @Positive(message = "Amount must be greater than zero")
            BigDecimal amount,

            @NotBlank(message = "Description is required")
            @Size(max = 255, message = "Description cannot exceed 255 characters")
            String description
    ) implements BillRequestDTO {}

    record Update(
            @NotNull(message = "Due date is required")
            LocalDate dueDate,

            @NotNull(message = "Amount is required")
            @Positive(message = "Amount must be greater than zero")
            BigDecimal amount,

            @NotBlank(message = "Description is required")
            @Size(max = 255, message = "Description cannot exceed 255 characters")
            String description
    ) implements BillRequestDTO {}

    record Pay(
            @NotNull(message = "Payment date is required")
            @PastOrPresent(message = "Payment date cannot be in the future")
            LocalDate paymentDate
    ) implements BillRequestDTO {}

    record Search(
            @NotNull(message = "Start date is required")
            LocalDate startDate,

            @NotNull(message = "End date is required")
            LocalDate endDate,

            String description,

            @PositiveOrZero(message = "Page number cannot be negative")
            int page,

            @Positive(message = "Page size must be greater than zero")
            @Max(value = 100, message = "Page size cannot exceed 100")
            int size
    ) implements BillRequestDTO {}

    record CalculateTotal(
            @NotNull(message = "Start date is required")
            LocalDate startDate,

            @NotNull(message = "End date is required")
            LocalDate endDate
    ) implements BillRequestDTO {}
}