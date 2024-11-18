package com.github.djoarns.payflow.domain.user.exception;

import com.github.djoarns.payflow.domain.exception.UserDomainException;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidUserDataExceptionTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        // Arrange
        String errorMessage = "Invalid user data";

        // Act
        InvalidUserDataException exception = new InvalidUserDataException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should extend UserDomainException")
    void shouldExtendUserDomainException() {
        // Arrange
        InvalidUserDataException exception = new InvalidUserDataException("Test message");

        // Assert
        assertTrue(exception instanceof UserDomainException);
    }

    @Test
    @DisplayName("Should preserve stack trace")
    void shouldPreserveStackTrace() {
        // Arrange
        String errorMessage = "Test error message";

        // Act
        InvalidUserDataException exception = new InvalidUserDataException(errorMessage);

        // Assert
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Should handle null message")
    void shouldHandleNullMessage() {
        // Act
        InvalidUserDataException exception = new InvalidUserDataException(null);

        // Assert
        assertNull(exception.getMessage());
    }
}