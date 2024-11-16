package com.github.djoarns.payflow.application.user.controller;

import com.github.djoarns.payflow.application.user.dto.request.AuthRequestDTO;
import com.github.djoarns.payflow.application.user.dto.response.AuthResponseDTO;
import com.github.djoarns.payflow.application.user.mapper.AuthRequestMapper;
import com.github.djoarns.payflow.application.user.mapper.AuthResponseMapper;
import com.github.djoarns.payflow.application.user.usecase.AuthenticateUserUseCase;
import com.github.djoarns.payflow.application.user.usecase.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final AuthRequestMapper requestMapper;
    private final AuthResponseMapper responseMapper;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthResponseDTO.Authentication> register(
            @RequestBody @Valid AuthRequestDTO.Register request
    ) {
        var command = requestMapper.toRegisterCommand(request);
        var result = registerUserUseCase.execute(command);
        return ResponseEntity.ok(responseMapper.toResponseDTO(result));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user")
    public ResponseEntity<AuthResponseDTO.Authentication> login(
            @RequestBody @Valid AuthRequestDTO.Login request
    ) {
        var command = requestMapper.toLoginCommand(request);
        var result = authenticateUserUseCase.execute(command);
        return ResponseEntity.ok(responseMapper.toResponseDTO(result));
    }
}