package com.github.djoarns.payflow.domain.user.valueobject;

import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest extends BaseUnitTest {

    @Nested
    @DisplayName("Username.of")
    class UsernameOf {
        @Test
        @DisplayName("Should create Username when value is valid")
        void shouldCreateUsernameWhenValid() {
            // Arrange
            String validUsername = "testuser";

            // Act
            Username username = Username.of(validUsername);

            // Assert
            assertNotNull(username);
            assertEquals(validUsername, username.getValue());
        }

        @Test
        @DisplayName("Should trim and convert to lowercase")
        void shouldTrimAndConvertToLowercase() {
            // Arrange
            String untrimmedUsername = "  TestUser  ";

            // Act
            Username username = Username.of(untrimmedUsername);

            // Assert
            assertEquals("testuser", username.getValue());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "   "})
        @DisplayName("Should throw exception when username is empty or blank")
        void shouldThrowExceptionWhenEmptyOrBlank(String invalidUsername) {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> Username.of(invalidUsername)
            );
            assertEquals("Username cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when username is null")
        void shouldThrowExceptionWhenNull() {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> Username.of(null)
            );
            assertEquals("Username cannot be empty", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"a", "ab"})
        @DisplayName("Should throw exception when username is too short")
        void shouldThrowExceptionWhenTooShort(String shortUsername) {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> Username.of(shortUsername)
            );
            assertEquals("Username must be between 3 and 50 characters", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when username is too long")
        void shouldThrowExceptionWhenTooLong() {
            // Arrange
            String longUsername = "a".repeat(51);

            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> Username.of(longUsername)
            );
            assertEquals("Username must be between 3 and 50 characters", exception.getMessage());
        }
    }

    @Test
    @DisplayName("Username should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        Username username1 = Username.of("testuser");
        Username username2 = Username.of("testuser");
        Username username3 = Username.of("otheruser");

        // Assert
        assertEquals(username1, username2);
        assertNotEquals(username1, username3);
        assertEquals(username1.hashCode(), username2.hashCode());
        assertNotEquals(username1.hashCode(), username3.hashCode());
    }

    @Test
    @DisplayName("Username should be case insensitive for equality")
    void shouldBeCaseInsensitiveForEquality() {
        // Arrange
        Username username1 = Username.of("TestUser");
        Username username2 = Username.of("testuser");

        // Assert
        assertEquals(username1, username2);
        assertEquals(username1.hashCode(), username2.hashCode());
    }
}