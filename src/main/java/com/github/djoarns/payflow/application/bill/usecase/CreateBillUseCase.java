package com.github.djoarns.payflow.application.bill.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.result.BillResult;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.valueobject.Amount;
import com.github.djoarns.payflow.domain.bill.valueobject.Description;
import com.github.djoarns.payflow.domain.bill.valueobject.DueDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateBillUseCase {
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("999999999.99");
    private final BillRepository billRepository;

    @Transactional
    public BillResult.Single execute(BillCommand.Create command) {
        validateDueDate(command.dueDate());
        validateAmount(command.amount());

        var bill = Bill.create(
                DueDate.of(command.dueDate()),
                Amount.of(command.amount()),
                Description.of(command.description())
        );

        return new BillResult.Single(billRepository.save(bill));
    }

    private void validateDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new InvalidBillDataException("Due date cannot be null");
        }

        LocalDate today = LocalDate.now();

        if (dueDate.isBefore(today)) {
            throw new InvalidBillDataException("Due date cannot be in the past");
        }

        LocalDate maxFutureDate = today.plusYears(10);
        if (dueDate.isAfter(maxFutureDate)) {
            throw new InvalidBillDataException("Due date cannot be more than 10 years in the future");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidBillDataException("Amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBillDataException("Amount must be greater than zero");
        }

        if (amount.compareTo(MAX_AMOUNT) > 0) {
            throw new InvalidBillDataException("Amount cannot be greater than 999999999.99");
        }
    }
}