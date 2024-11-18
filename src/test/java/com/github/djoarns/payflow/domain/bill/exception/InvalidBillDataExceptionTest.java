package com.github.djoarns.payflow.domain.bill.exception;

import com.github.djoarns.payflow.domain.exception.BillDomainException;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidBillDataExceptionTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        // Arrange
        String message = "Invalid bill data";

        // Act
        InvalidBillDataException exception = new InvalidBillDataException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertInstanceOf(BillDomainException.class, exception);
    }

    @Test
    @DisplayName("Should preserve stack trace")
    void shouldPreserveStackTrace() {
        // Act
        InvalidBillDataException exception = new InvalidBillDataException("test");

        // Assert
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }
}