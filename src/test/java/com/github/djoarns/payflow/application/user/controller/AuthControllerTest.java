package com.github.djoarns.payflow.application.user.controller;

import com.github.djoarns.payflow.application.user.dto.request.AuthRequestDTO;
import com.github.djoarns.payflow.application.user.dto.response.AuthResponseDTO;
import com.github.djoarns.payflow.application.user.mapper.AuthRequestMapper;
import com.github.djoarns.payflow.application.user.mapper.AuthResponseMapper;
import com.github.djoarns.payflow.application.user.usecase.AuthenticateUserUseCase;
import com.github.djoarns.payflow.application.user.usecase.RegisterUserUseCase;
import com.github.djoarns.payflow.application.user.command.AuthCommand;
import com.github.djoarns.payflow.application.user.result.AuthResult;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest extends BaseUnitTest {

    @Mock
    private RegisterUserUseCase registerUserUseCase;
    @Mock
    private AuthenticateUserUseCase authenticateUserUseCase;
    @Mock
    private AuthRequestMapper requestMapper;
    @Mock
    private AuthResponseMapper responseMapper;

    private AuthController controller;

    @BeforeEach
    void setUp() {
        controller = new AuthController(
                registerUserUseCase,
                authenticateUserUseCase,
                requestMapper,
                responseMapper
        );
    }

    @Nested
    @DisplayName("register")
    class Register {
        @Test
        @DisplayName("Should register user successfully")
        void shouldRegisterUserSuccessfully() {
            // Arrange
            var request = new AuthRequestDTO.Register("testuser", "password123");
            var command = new AuthCommand.Register("testuser", "password123");
            var result = new AuthResult.Authentication("token123", "testuser");
            var response = new AuthResponseDTO.Authentication("token123", "testuser");

            when(requestMapper.toRegisterCommand(request)).thenReturn(command);
            when(registerUserUseCase.execute(command)).thenReturn(result);
            when(responseMapper.toResponseDTO(result)).thenReturn(response);

            // Act
            var responseEntity = controller.register(request);

            // Assert
            assertNotNull(responseEntity);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(response, responseEntity.getBody());
            verify(requestMapper).toRegisterCommand(request);
            verify(registerUserUseCase).execute(command);
            verify(responseMapper).toResponseDTO(result);
        }

        @Test
        @DisplayName("Should handle invalid registration request")
        void shouldHandleInvalidRegistrationRequest() {
            // Arrange
            var request = new AuthRequestDTO.Register("testuser", "password123");
            when(requestMapper.toRegisterCommand(request))
                    .thenThrow(new IllegalArgumentException("Invalid registration data"));

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> controller.register(request));
            verify(requestMapper).toRegisterCommand(request);
            verify(registerUserUseCase, never()).execute(any());
            verify(responseMapper, never()).toResponseDTO(any());
        }
    }

    @Nested
    @DisplayName("login")
    class Login {
        @Test
        @DisplayName("Should login user successfully")
        void shouldLoginUserSuccessfully() {
            // Arrange
            var request = new AuthRequestDTO.Login("testuser", "password123");
            var command = new AuthCommand.Login("testuser", "password123");
            var result = new AuthResult.Authentication("token123", "testuser");
            var response = new AuthResponseDTO.Authentication("token123", "testuser");

            when(requestMapper.toLoginCommand(request)).thenReturn(command);
            when(authenticateUserUseCase.execute(command)).thenReturn(result);
            when(responseMapper.toResponseDTO(result)).thenReturn(response);

            // Act
            var responseEntity = controller.login(request);

            // Assert
            assertNotNull(responseEntity);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(response, responseEntity.getBody());
            verify(requestMapper).toLoginCommand(request);
            verify(authenticateUserUseCase).execute(command);
            verify(responseMapper).toResponseDTO(result);
        }

        @Test
        @DisplayName("Should handle invalid login request")
        void shouldHandleInvalidLoginRequest() {
            // Arrange
            var request = new AuthRequestDTO.Login("testuser", "password123");
            when(requestMapper.toLoginCommand(request))
                    .thenThrow(new IllegalArgumentException("Invalid login data"));

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> controller.login(request));
            verify(requestMapper).toLoginCommand(request);
            verify(authenticateUserUseCase, never()).execute(any());
            verify(responseMapper, never()).toResponseDTO(any());
        }
    }
}