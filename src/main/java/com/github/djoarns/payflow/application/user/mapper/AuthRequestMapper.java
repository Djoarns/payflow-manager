package com.github.djoarns.payflow.application.user.mapper;

import com.github.djoarns.payflow.application.user.command.AuthCommand;
import com.github.djoarns.payflow.application.user.dto.request.AuthRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthRequestMapper {
    public AuthCommand.Register toRegisterCommand(AuthRequestDTO.Register dto) {
        return new AuthCommand.Register(
                dto.username(),
                dto.password()
        );
    }

    public AuthCommand.Login toLoginCommand(AuthRequestDTO.Login dto) {
        return new AuthCommand.Login(
                dto.username(),
                dto.password()
        );
    }
}