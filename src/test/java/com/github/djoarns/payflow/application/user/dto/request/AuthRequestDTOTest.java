package com.github.djoarns.payflow.application.user.dto.request;

import com.github.djoarns.payflow.util.BaseUnitTest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthRequestDTOTest extends BaseUnitTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Register DTO Tests")
    class RegisterDTOTests {
        @Test
        @DisplayName("Should create valid register DTO")
        void shouldCreateValidRegisterDTO() {
            // Arrange
            var dto = new AuthRequestDTO.Register("testuser", "password123");

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertTrue(violations.isEmpty());
            assertEquals("testuser", dto.username());
            assertEquals("password123", dto.password());
        }

        @Test
        @DisplayName("Should fail when username is null")
        void shouldFailWhenUsernameNull() {
            // Arrange
            var dto = new AuthRequestDTO.Register(null, "password123");

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Username is required", violations.iterator().next().getMessage());
        }

        @Test
        @DisplayName("Should fail when password is null")
        void shouldFailWhenPasswordNull() {
            // Arrange
            var dto = new AuthRequestDTO.Register("testuser", null);

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Password is required", violations.iterator().next().getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        @DisplayName("Should fail when username is blank")
        void shouldFailWhenUsernameBlank(String username) {
            // Arrange
            var dto = new AuthRequestDTO.Register(username, "password123");

            // Act
            var violations = validator.validate(dto);

            // Assert
            var usernameViolation = violations.stream()
                    .filter(v -> v.getPropertyPath().toString().equals("username"))
                    .filter(v -> v.getMessage().equals("Username is required"))
                    .findFirst();

            assertTrue(usernameViolation.isPresent());
            assertEquals("Username is required", usernameViolation.get().getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        @DisplayName("Should fail when password is blank")
        void shouldFailWhenPasswordBlank(String password) {
            // Arrange
            var dto = new AuthRequestDTO.Register("testuser", password);

            // Act
            var violations = validator.validate(dto);

            // Assert
            var passwordViolation = violations.stream()
                    .filter(v -> v.getPropertyPath().toString().equals("password"))
                    .filter(v -> v.getMessage().equals("Password is required"))
                    .findFirst();

            assertTrue(passwordViolation.isPresent());
            assertEquals("Password is required", passwordViolation.get().getMessage());
        }

        @Test
        @DisplayName("Should fail when username is too short")
        void shouldFailWhenUsernameTooShort() {
            // Arrange
            var dto = new AuthRequestDTO.Register("ab", "password123");

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 50 characters", violations.iterator().next().getMessage());
        }

        @Test
        @DisplayName("Should fail when username is too long")
        void shouldFailWhenUsernameTooLong() {
            // Arrange
            var longUsername = "a".repeat(51);
            var dto = new AuthRequestDTO.Register(longUsername, "password123");

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 50 characters", violations.iterator().next().getMessage());
        }

        @Test
        @DisplayName("Should fail when password is too short")
        void shouldFailWhenPasswordTooShort() {
            // Arrange
            var dto = new AuthRequestDTO.Register("testuser", "12345");

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Password must be at least 6 characters", violations.iterator().next().getMessage());
        }
    }

    @Nested
    @DisplayName("Login DTO Tests")
    class LoginDTOTests {
        @Test
        @DisplayName("Should create valid login DTO")
        void shouldCreateValidLoginDTO() {
            // Arrange
            var dto = new AuthRequestDTO.Login("testuser", "password123");

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertTrue(violations.isEmpty());
            assertEquals("testuser", dto.username());
            assertEquals("password123", dto.password());
        }

        @Test
        @DisplayName("Should fail when username is null")
        void shouldFailWhenUsernameNull() {
            // Arrange
            var dto = new AuthRequestDTO.Login(null, "password123");

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Username is required", violations.iterator().next().getMessage());
        }

        @Test
        @DisplayName("Should fail when password is null")
        void shouldFailWhenPasswordNull() {
            // Arrange
            var dto = new AuthRequestDTO.Login("testuser", null);

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Password is required", violations.iterator().next().getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        @DisplayName("Should fail when username is blank")
        void shouldFailWhenUsernameBlank(String username) {
            // Arrange
            var dto = new AuthRequestDTO.Login(username, "password123");

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Username is required", violations.iterator().next().getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        @DisplayName("Should fail when password is blank")
        void shouldFailWhenPasswordBlank(String password) {
            // Arrange
            var dto = new AuthRequestDTO.Login("testuser", password);

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Password is required", violations.iterator().next().getMessage());
        }
    }
}