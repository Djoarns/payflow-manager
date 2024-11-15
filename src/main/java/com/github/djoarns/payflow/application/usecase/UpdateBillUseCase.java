package com.github.djoarns.payflow.application.usecase;

import com.github.djoarns.payflow.application.command.BillCommand;
import com.github.djoarns.payflow.application.result.BillResult;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.valueobject.Amount;
import com.github.djoarns.payflow.domain.bill.valueobject.BillId;
import com.github.djoarns.payflow.domain.bill.valueobject.Description;
import com.github.djoarns.payflow.domain.bill.valueobject.DueDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateBillUseCase {
    private final BillRepository billRepository;

    @Transactional
    public BillResult.Single execute(BillCommand.Update command) {
        var bill = billRepository.findById(BillId.of(command.id()))
                .orElseThrow(() -> new InvalidBillDataException("Bill not found"));

        bill.update(
                DueDate.of(command.dueDate()),
                Amount.of(command.amount()),
                Description.of(command.description())
        );

        return new BillResult.Single(billRepository.save(bill));
    }
}