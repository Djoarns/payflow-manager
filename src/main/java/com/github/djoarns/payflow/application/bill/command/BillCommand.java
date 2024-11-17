package com.github.djoarns.payflow.application.bill.command;

import com.github.djoarns.payflow.domain.bill.valueobject.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public sealed interface BillCommand permits
        BillCommand.Create,
        BillCommand.Update,
        BillCommand.Pay,
        BillCommand.List,
        BillCommand.CalculateTotalPaid,
        BillCommand.Find,
        BillCommand.ChangeStatus {

    record Create(
            LocalDate dueDate,
            BigDecimal amount,
            String description
    ) implements BillCommand {}

    record Update(
            Long id,
            LocalDate dueDate,
            BigDecimal amount,
            String description
    ) implements BillCommand {}

    record Pay(
            Long id,
            LocalDate paymentDate
    ) implements BillCommand {}

    record List(
            LocalDate startDate,
            LocalDate endDate,
            String description,
            int page,
            int size
    ) implements BillCommand {}

    record CalculateTotalPaid(
            LocalDate startDate,
            LocalDate endDate
    ) implements BillCommand {}

    record Find(Long id) implements BillCommand {}

    record ChangeStatus(
            Long id,
            Status newStatus
    ) implements BillCommand {}
}