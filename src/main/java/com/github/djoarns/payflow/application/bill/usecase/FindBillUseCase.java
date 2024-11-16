package com.github.djoarns.payflow.application.bill.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.result.BillResult;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.valueobject.BillId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindBillUseCase {
    private final BillRepository billRepository;

    @Transactional(readOnly = true)
    public BillResult.Single execute(BillCommand.Find command) {
        return new BillResult.Single(
                billRepository.findById(BillId.of(command.id()))
                        .orElseThrow(() -> new InvalidBillDataException("Bill not found"))
        );
    }
}