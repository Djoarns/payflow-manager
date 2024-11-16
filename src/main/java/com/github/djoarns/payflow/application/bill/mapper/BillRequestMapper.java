package com.github.djoarns.payflow.application.bill.mapper;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.dto.request.BillRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class BillRequestMapper {
    public BillCommand.Create toCreateCommand(BillRequestDTO.Create dto) {
        return new BillCommand.Create(
                dto.dueDate(),
                dto.amount(),
                dto.description()
        );
    }

    public BillCommand.Update toUpdateCommand(Long id, BillRequestDTO.Update dto) {
        return new BillCommand.Update(
                id,
                dto.dueDate(),
                dto.amount(),
                dto.description()
        );
    }

    public BillCommand.Pay toPayCommand(Long id, BillRequestDTO.Pay dto) {
        return new BillCommand.Pay(id, dto.paymentDate());
    }

    public BillCommand.List toListCommand(BillRequestDTO.Search dto) {
        return new BillCommand.List(
                dto.startDate(),
                dto.endDate(),
                dto.description(),
                dto.page(),
                dto.size()
        );
    }

    public BillCommand.CalculateTotalPaid toCalculateTotalCommand(BillRequestDTO.CalculateTotal dto) {
        return new BillCommand.CalculateTotalPaid(
                dto.startDate(),
                dto.endDate()
        );
    }
}
