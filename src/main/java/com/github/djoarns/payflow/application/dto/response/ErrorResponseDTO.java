package com.github.djoarns.payflow.application.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        String code,
        LocalDateTime timestamp
) {}
