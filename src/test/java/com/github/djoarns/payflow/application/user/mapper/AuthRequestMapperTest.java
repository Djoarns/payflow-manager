package com.github.djoarns.payflow.application.user.mapper;

import com.github.djoarns.payflow.application.user.dto.request.AuthRequestDTO;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthRequestMapperTest extends BaseUnitTest {

    private AuthRequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AuthRequestMapper();
    }

    @Nested
    @DisplayName("toRegisterCommand")
    class ToRegisterCommand {
        @Test
        @DisplayName("Should map register DTO to command")
        void shouldMapRegisterDtoToCommand() {
            // Arrange
            var dto = new AuthRequestDTO.Register(
                    "testuser",
                    "password123"
            );

            // Act
            var command = mapper.toRegisterCommand(dto);

            // Assert
            assertNotNull(command);
            assertEquals(dto.username(), command.username());
            assertEquals(dto.password(), command.password());
        }

        @ParameterizedTest
        @MethodSource("registrationDataProvider")
        @DisplayName("Should map different registration data correctly")
        void shouldMapDifferentRegistrationData(String username, String password) {
            // Arrange
            var dto = new AuthRequestDTO.Register(username, password);

            // Act
            var command = mapper.toRegisterCommand(dto);

            // Assert
            assertEquals(username, command.username());
            assertEquals(password, command.password());
        }

        private static Stream<Arguments> registrationDataProvider() {
            return Stream.of(
                    Arguments.of("user1", "pass123"),
                    Arguments.of("admin", "adminpass"),
                    Arguments.of("testuser@domain.com", "complex!Pass123"),
                    Arguments.of("short", "min6ch")
            );
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            // Arrange
            var dto = new AuthRequestDTO.Register("", "");

            // Act
            var command = mapper.toRegisterCommand(dto);

            // Assert
            assertEquals("", command.username());
            assertEquals("", command.password());
        }
    }

    @Nested
    @DisplayName("toLoginCommand")
    class ToLoginCommand {
        @Test
        @DisplayName("Should map login DTO to command")
        void shouldMapLoginDtoToCommand() {
            // Arrange
            var dto = new AuthRequestDTO.Login(
                    "testuser",
                    "password123"
            );

            // Act
            var command = mapper.toLoginCommand(dto);

            // Assert
            assertNotNull(command);
            assertEquals(dto.username(), command.username());
            assertEquals(dto.password(), command.password());
        }

        @ParameterizedTest
        @MethodSource("loginDataProvider")
        @DisplayName("Should map different login data correctly")
        void shouldMapDifferentLoginData(String username, String password) {
            // Arrange
            var dto = new AuthRequestDTO.Login(username, password);

            // Act
            var command = mapper.toLoginCommand(dto);

            // Assert
            assertEquals(username, command.username());
            assertEquals(password, command.password());
        }

        private static Stream<Arguments> loginDataProvider() {
            return Stream.of(
                    Arguments.of("user1", "pass123"),
                    Arguments.of("admin", "adminpass"),
                    Arguments.of("testuser@domain.com", "complex!Pass123"),
                    Arguments.of("short", "min6ch")
            );
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            // Arrange
            var dto = new AuthRequestDTO.Login("", "");

            // Act
            var command = mapper.toLoginCommand(dto);

            // Assert
            assertEquals("", command.username());
            assertEquals("", command.password());
        }
    }
}