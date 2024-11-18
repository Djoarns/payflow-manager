package com.github.djoarns.payflow.application.bill.result;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BillImportResult")
class BillImportResultTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create result with valid data")
    void shouldCreateResultWithValidData() {
        // Arrange
        int totalProcessed = 10;
        int successCount = 8;
        int errorCount = 2;
        String message = "Import completed successfully";

        // Act
        var result = new BillImportResult(totalProcessed, successCount, errorCount, message);

        // Assert
        assertEquals(totalProcessed, result.totalProcessed());
        assertEquals(successCount, result.successCount());
        assertEquals(errorCount, result.errorCount());
        assertEquals(message, result.message());
    }

    @Test
    @DisplayName("Should create result with zero values")
    void shouldCreateResultWithZeroValues() {
        // Act
        var result = new BillImportResult(0, 0, 0, "No bills processed");

        // Assert
        assertEquals(0, result.totalProcessed());
        assertEquals(0, result.successCount());
        assertEquals(0, result.errorCount());
        assertEquals("No bills processed", result.message());
    }

    @Test
    @DisplayName("Should allow null message")
    void shouldAllowNullMessage() {
        // Act
        var result = new BillImportResult(1, 1, 0, null);

        // Assert
        assertNull(result.message());
    }

    @Test
    @DisplayName("Should implement value record equality")
    void shouldImplementValueRecordEquality() {
        // Arrange
        var result1 = new BillImportResult(10, 8, 2, "Success");
        var result2 = new BillImportResult(10, 8, 2, "Success");
        var result3 = new BillImportResult(10, 7, 3, "Success");

        // Assert
        assertEquals(result1, result2);
        assertNotEquals(result1, result3);
        assertEquals(result1.hashCode(), result2.hashCode());
        assertNotEquals(result1.hashCode(), result3.hashCode());
    }

    @Test
    @DisplayName("Should handle maximum values")
    void shouldHandleMaximumValues() {
        // Arrange
        int maxInt = Integer.MAX_VALUE;

        // Act
        var result = new BillImportResult(maxInt, maxInt, maxInt, "a".repeat(1000));

        // Assert
        assertEquals(maxInt, result.totalProcessed());
        assertEquals(maxInt, result.successCount());
        assertEquals(maxInt, result.errorCount());
        assertEquals("a".repeat(1000), result.message());
    }
}