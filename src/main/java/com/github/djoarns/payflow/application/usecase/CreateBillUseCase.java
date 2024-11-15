package com.github.djoarns.payflow.application.usecase;

import com.github.djoarns.payflow.application.command.BillCommand;
import com.github.djoarns.payflow.application.result.BillResult;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.valueobject.Amount;
import com.github.djoarns.payflow.domain.bill.valueobject.Description;
import com.github.djoarns.payflow.domain.bill.valueobject.DueDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateBillUseCase {
    private final BillRepository billRepository;

    @Transactional
    public BillResult.Single execute(BillCommand.Create command) {
        var bill = Bill.create(
                DueDate.of(command.dueDate()),
                Amount.of(command.amount()),
                Description.of(command.description())
        );

        return new BillResult.Single(billRepository.save(bill));
    }
}