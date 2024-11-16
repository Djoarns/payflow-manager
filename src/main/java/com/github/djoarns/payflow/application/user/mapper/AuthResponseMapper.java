package com.github.djoarns.payflow.application.user.mapper;

import com.github.djoarns.payflow.application.user.result.AuthResult;
import com.github.djoarns.payflow.application.user.dto.response.AuthResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseMapper {
    public AuthResponseDTO.Authentication toResponseDTO(AuthResult.Authentication result) {
        return new AuthResponseDTO.Authentication(
                result.token(),
                result.username()
        );
    }
}