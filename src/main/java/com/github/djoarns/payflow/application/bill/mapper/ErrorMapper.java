package com.github.djoarns.payflow.application.bill.mapper;

import com.github.djoarns.payflow.application.bill.dto.response.ErrorResponseDTO;
import com.github.djoarns.payflow.domain.exception.BillDomainException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ErrorMapper {
    public ErrorResponseDTO toErrorDTO(Exception ex) {
        return new ErrorResponseDTO(
                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred",
                ex instanceof BillDomainException ? "BUSINESS_ERROR" : "SYSTEM_ERROR",
                LocalDateTime.now()
        );
    }
}