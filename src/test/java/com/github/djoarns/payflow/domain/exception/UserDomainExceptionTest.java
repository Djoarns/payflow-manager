package com.github.djoarns.payflow.domain.exception;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDomainExceptionTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create UserDomainException with message")
    void shouldCreateUserDomainExceptionWithMessage() {
        // Arrange
        String errorMessage = "Test error message";

        // Act
        UserDomainException exception = new UserDomainException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
        assertTrue(exception instanceof DomainException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should maintain exception message through inheritance chain")
    void shouldMaintainExceptionMessageThroughInheritanceChain() {
        // Arrange
        String errorMessage = "Inherited error message";

        // Act
        Exception exception = new UserDomainException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}