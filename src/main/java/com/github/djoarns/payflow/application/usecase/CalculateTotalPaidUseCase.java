package com.github.djoarns.payflow.application.usecase;

import com.github.djoarns.payflow.application.command.BillCommand;
import com.github.djoarns.payflow.application.result.BillResult;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.valueobject.Amount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CalculateTotalPaidUseCase {
    private final BillRepository billRepository;

    @Transactional(readOnly = true)
    public BillResult.TotalPaid execute(BillCommand.CalculateTotalPaid command) {
        var paidBills = billRepository.findByPaymentDateBetween(
                command.startDate(),
                command.endDate()
        );

        var total = paidBills.stream()
                .map(bill -> bill.getAmount().getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BillResult.TotalPaid(Amount.of(total));
    }
}