package com.github.djoarns.payflow.application.bill.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.result.BillResult;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.valueobject.BillId;
import com.github.djoarns.payflow.domain.bill.valueobject.PaymentDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PayBillUseCase {
    private final BillRepository billRepository;

    @Transactional
    public BillResult.Single execute(BillCommand.Pay command) {
        var bill = billRepository.findById(BillId.of(command.id()))
                .orElseThrow(() -> new InvalidBillDataException("Bill not found"));

        bill.pay(PaymentDate.of(command.paymentDate()));

        return new BillResult.Single(billRepository.save(bill));
    }
}