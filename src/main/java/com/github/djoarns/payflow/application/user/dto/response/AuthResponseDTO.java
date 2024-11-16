package com.github.djoarns.payflow.application.user.dto.response;

public sealed interface AuthResponseDTO {
    record Authentication(
            String token,
            String username
    ) implements AuthResponseDTO {}
}