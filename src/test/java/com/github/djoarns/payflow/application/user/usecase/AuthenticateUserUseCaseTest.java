package com.github.djoarns.payflow.application.user.usecase;

import com.github.djoarns.payflow.application.user.command.AuthCommand;
import com.github.djoarns.payflow.domain.user.User;
import com.github.djoarns.payflow.domain.user.UserRepository;
import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import com.github.djoarns.payflow.domain.user.valueobject.Password;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import com.github.djoarns.payflow.infrastructure.security.JwtService;
import com.github.djoarns.payflow.infrastructure.security.user.UserDetailsImpl;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticateUserUseCaseTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    private AuthenticateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AuthenticateUserUseCase(userRepository, jwtService, authenticationManager);
    }

    @Test
    @DisplayName("Should authenticate user successfully")
    void shouldAuthenticateUserSuccessfully() {
        // Arrange
        String username = "testuser";
        String password = "password123";
        String token = "valid.jwt.token";

        var command = new AuthCommand.Login(username, password);
        var user = User.create(
                Username.of(username),
                Password.of(password)
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password));
        when(userRepository.findByUsername(any(Username.class)))
                .thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserDetailsImpl.class)))
                .thenReturn(token);

        // Act
        var result = useCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.token());
        assertEquals(username, result.username());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername(any(Username.class));
        verify(jwtService).generateToken(any(UserDetailsImpl.class));
    }

    @Test
    @DisplayName("Should throw exception when authentication fails")
    void shouldThrowExceptionWhenAuthenticationFails() {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";
        var command = new AuthCommand.Login(username, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> useCase.execute(command));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByUsername(any(Username.class));
        verify(jwtService, never()).generateToken(any(UserDetailsImpl.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        String username = "testuser";
        String password = "password123";
        var command = new AuthCommand.Login(username, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password));
        when(userRepository.findByUsername(any(Username.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidUserDataException.class, () -> useCase.execute(command));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername(any(Username.class));
        verify(jwtService, never()).generateToken(any(UserDetailsImpl.class));
    }
}