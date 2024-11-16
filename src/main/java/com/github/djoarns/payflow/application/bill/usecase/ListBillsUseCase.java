package com.github.djoarns.payflow.application.bill.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.result.BillResult;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListBillsUseCase {
    private final BillRepository billRepository;

    @Transactional(readOnly = true)
    public BillResult.List execute(BillCommand.List command) {
        var bills = billRepository.findByDueDateBetweenAndDescription(
                command.startDate(),
                command.endDate(),
                command.description(),
                command.page(),
                command.size()
        );

        var total = billRepository.countByDueDateBetweenAndDescription(
                command.startDate(),
                command.endDate(),
                command.description()
        );

        return new BillResult.List(bills, total, command.page(), command.size());
    }
}