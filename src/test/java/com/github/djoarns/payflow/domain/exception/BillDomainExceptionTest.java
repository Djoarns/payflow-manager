package com.github.djoarns.payflow.domain.exception;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BillDomainExceptionTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create BillDomainException with message")
    void shouldCreateBillDomainExceptionWithMessage() {
        // Arrange
        String errorMessage = "Test error message";

        // Act
        BillDomainException exception = new BillDomainException(errorMessage);

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
        Exception exception = new BillDomainException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}