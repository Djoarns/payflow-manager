package com.github.djoarns.payflow.domain.exception;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create concrete DomainException with message")
    void shouldCreateConcreteExceptionWithMessage() {
        // Arrange
        String errorMessage = "Test error message";

        // Act
        DomainException exception = new ConcreteDomainException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should properly handle null message")
    void shouldHandleNullMessage() {
        // Act
        DomainException exception = new ConcreteDomainException(null);

        // Assert
        assertNull(exception.getMessage());
    }

    // Concrete implementation for testing abstract class
    private static class ConcreteDomainException extends DomainException {
        public ConcreteDomainException(String message) {
            super(message);
        }
    }
}