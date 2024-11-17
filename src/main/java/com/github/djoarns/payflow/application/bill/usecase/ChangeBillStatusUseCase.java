package com.github.djoarns.payflow.application.bill.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.result.BillResult;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillStatusException;
import com.github.djoarns.payflow.domain.bill.valueobject.BillId;
import com.github.djoarns.payflow.domain.bill.valueobject.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ChangeBillStatusUseCase {
    private final BillRepository billRepository;

    @Transactional
    public BillResult.Single execute(BillCommand.ChangeStatus command) {
        var bill = billRepository.findById(BillId.of(command.id()))
                .orElseThrow(() -> new InvalidBillDataException("Bill not found"));

        switch (command.newStatus()) {
            case CANCELLED -> bill.cancel();
            case OVERDUE -> {
                if (bill.getStatus() != Status.PENDING) {
                    throw new InvalidBillStatusException("Only PENDING bills can be marked as OVERDUE");
                }
                if (bill.getDueDate().getValue().isAfter(LocalDate.now())) {
                    throw new InvalidBillStatusException("Cannot mark bill as OVERDUE before due date");
                }
            }
            case PENDING -> {
                if (bill.getStatus() != Status.OVERDUE) {
                    throw new InvalidBillStatusException("Only OVERDUE bills can be marked as PENDING");
                }
            }
            case PAID -> throw new InvalidBillStatusException("Use the pay endpoint to mark a bill as PAID");
        }

        return new BillResult.Single(billRepository.save(bill));
    }
}