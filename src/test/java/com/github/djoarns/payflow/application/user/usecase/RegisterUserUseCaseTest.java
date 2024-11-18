package com.github.djoarns.payflow.application.user.usecase;

import com.github.djoarns.payflow.application.user.command.AuthCommand;
import com.github.djoarns.payflow.domain.user.User;
import com.github.djoarns.payflow.domain.user.UserRepository;
import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import com.github.djoarns.payflow.infrastructure.security.JwtService;
import com.github.djoarns.payflow.infrastructure.security.user.UserDetailsImpl;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterUserUseCaseTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterUserUseCase(userRepository, passwordEncoder, jwtService);
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        // Arrange
        String username = "newuser";
        String password = "password123";
        String encodedPassword = "encoded_password";
        String token = "valid.jwt.token";

        var command = new AuthCommand.Register(username, password);

        when(userRepository.existsByUsername(any(Username.class)))
                .thenReturn(false);
        when(passwordEncoder.encode(password))
                .thenReturn(encodedPassword);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(UserDetailsImpl.class)))
                .thenReturn(token);

        // Act
        var result = useCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.token());
        assertEquals(username, result.username());

        var userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(username, savedUser.getUsername().getValue());
        assertEquals(encodedPassword, savedUser.getPassword().getValue());
        assertTrue(savedUser.isEnabled());
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowExceptionWhenUsernameExists() {
        // Arrange
        String username = "existinguser";
        String password = "password123";
        var command = new AuthCommand.Register(username, password);

        when(userRepository.existsByUsername(any(Username.class)))
                .thenReturn(true);

        // Act & Assert
        assertThrows(InvalidUserDataException.class, () -> useCase.execute(command));

        verify(userRepository).existsByUsername(any(Username.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(any(UserDetailsImpl.class));
    }

    @Test
    @DisplayName("Should handle registration with minimum valid values")
    void shouldHandleRegistrationWithMinimumValidValues() {
        // Arrange
        String username = "min";
        String password = "pass123";
        String encodedPassword = "encoded_min_pass";
        String token = "token";

        var command = new AuthCommand.Register(username, password);

        when(userRepository.existsByUsername(any(Username.class)))
                .thenReturn(false);
        when(passwordEncoder.encode(password))
                .thenReturn(encodedPassword);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(UserDetailsImpl.class)))
                .thenReturn(token);

        // Act
        var result = useCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.token());
        assertEquals(username, result.username());
    }

    @Test
    @DisplayName("Should handle registration with maximum valid values")
    void shouldHandleRegistrationWithMaximumValidValues() {
        // Arrange
        String username = "a".repeat(50);
        String password = "a".repeat(100);
        String encodedPassword = "encoded_max_pass";
        String token = "token";

        var command = new AuthCommand.Register(username, password);

        when(userRepository.existsByUsername(any(Username.class)))
                .thenReturn(false);
        when(passwordEncoder.encode(password))
                .thenReturn(encodedPassword);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(UserDetailsImpl.class)))
                .thenReturn(token);

        // Act
        var result = useCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.token());
        assertEquals(username, result.username());
    }
}