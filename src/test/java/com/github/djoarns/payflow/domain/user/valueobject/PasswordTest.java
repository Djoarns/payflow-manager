package com.github.djoarns.payflow.domain.user.valueobject;

import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest extends BaseUnitTest {

    @Nested
    @DisplayName("Password.of")
    class PasswordOf {
        @Test
        @DisplayName("Should create Password when value is valid")
        void shouldCreatePasswordWhenValid() {
            // Arrange
            String validPassword = "password123";

            // Act
            Password password = Password.of(validPassword);

            // Assert
            assertNotNull(password);
            assertEquals(validPassword, password.getValue());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "   "})
        @DisplayName("Should throw exception when password is empty or blank")
        void shouldThrowExceptionWhenEmptyOrBlank(String invalidPassword) {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> Password.of(invalidPassword)
            );
            assertEquals("Password cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when password is null")
        void shouldThrowExceptionWhenNull() {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> Password.of(null)
            );
            assertEquals("Password cannot be empty", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "a", "short"})
        @DisplayName("Should throw exception when password is too short")
        void shouldThrowExceptionWhenTooShort(String shortPassword) {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> Password.of(shortPassword)
            );
            assertEquals("Password must be at least 6 characters", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Password.ofHashed")
    class PasswordOfHashed {
        @Test
        @DisplayName("Should create Password from valid hashed value")
        void shouldCreatePasswordFromValidHash() {
            // Arrange
            String validHash = "$2a$10$abcdefghijklmnopqrstuvwxyz123456789";

            // Act
            Password password = Password.ofHashed(validHash);

            // Assert
            assertNotNull(password);
            assertEquals(validHash, password.getValue());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "   "})
        @DisplayName("Should throw exception when hashed password is empty or blank")
        void shouldThrowExceptionWhenHashEmptyOrBlank(String invalidHash) {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> Password.ofHashed(invalidHash)
            );
            assertEquals("Hashed password cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when hashed password is null")
        void shouldThrowExceptionWhenHashNull() {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> Password.ofHashed(null)
            );
            assertEquals("Hashed password cannot be empty", exception.getMessage());
        }
    }

    @Test
    @DisplayName("Password should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        String passwordValue = "password123";
        Password password1 = Password.of(passwordValue);
        Password password2 = Password.of(passwordValue);
        Password password3 = Password.of("differentpassword");

        // Assert
        assertEquals(password1, password2);
        assertNotEquals(password1, password3);
        assertEquals(password1.hashCode(), password2.hashCode());
        assertNotEquals(password1.hashCode(), password3.hashCode());
    }
}