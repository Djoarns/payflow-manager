package com.github.djoarns.payflow.application.bill.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        String code,
        LocalDateTime timestamp
) {}
