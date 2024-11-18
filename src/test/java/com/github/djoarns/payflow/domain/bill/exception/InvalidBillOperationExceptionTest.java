package com.github.djoarns.payflow.domain.bill.exception;

import com.github.djoarns.payflow.domain.exception.BillDomainException;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidBillOperationExceptionTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        // Arrange
        String message = "Invalid bill operation";

        // Act
        InvalidBillOperationException exception = new InvalidBillOperationException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertInstanceOf(BillDomainException.class, exception);
    }

    @Test
    @DisplayName("Should preserve stack trace")
    void shouldPreserveStackTrace() {
        // Act
        InvalidBillOperationException exception = new InvalidBillOperationException("test");

        // Assert
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }
}