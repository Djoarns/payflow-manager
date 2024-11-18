package com.github.djoarns.payflow.infrastructure.csv;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BillCSVRecordTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create BillCSVRecord with valid data")
    void shouldCreateBillCSVRecordWithValidData() {
        // Arrange
        BillCSVRecord record = new BillCSVRecord();
        LocalDate dueDate = LocalDate.now();
        BigDecimal amount = new BigDecimal("100.50");
        String description = "Test Bill";

        // Act
        record.setDueDate(dueDate);
        record.setAmount(amount);
        record.setDescription(description);

        // Assert
        assertEquals(dueDate, record.getDueDate());
        assertEquals(amount, record.getAmount());
        assertEquals(description, record.getDescription());
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        // Arrange & Act
        BillCSVRecord record = new BillCSVRecord();

        // Assert
        assertNull(record.getDueDate());
        assertNull(record.getAmount());
        assertNull(record.getDescription());
    }

    @Test
    @DisplayName("Should implement equals and hashCode")
    void shouldImplementEqualsAndHashCode() {
        // Arrange
        BillCSVRecord record1 = new BillCSVRecord();
        record1.setDueDate(LocalDate.now());
        record1.setAmount(new BigDecimal("100.50"));
        record1.setDescription("Test Bill");

        BillCSVRecord record2 = new BillCSVRecord();
        record2.setDueDate(record1.getDueDate());
        record2.setAmount(record1.getAmount());
        record2.setDescription(record1.getDescription());

        BillCSVRecord record3 = new BillCSVRecord();
        record3.setDueDate(LocalDate.now().plusDays(1));
        record3.setAmount(new BigDecimal("200.75"));
        record3.setDescription("Different Bill");

        // Assert
        assertEquals(record1, record2);
        assertEquals(record1.hashCode(), record2.hashCode());
        assertNotEquals(record1, record3);
        assertNotEquals(record1.hashCode(), record3.hashCode());
    }

    @Test
    @DisplayName("Should implement toString")
    void shouldImplementToString() {
        // Arrange
        BillCSVRecord record = new BillCSVRecord();
        LocalDate dueDate = LocalDate.now();
        BigDecimal amount = new BigDecimal("100.50");
        String description = "Test Bill";

        record.setDueDate(dueDate);
        record.setAmount(amount);
        record.setDescription(description);

        // Act
        String toString = record.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains(dueDate.toString()));
        assertTrue(toString.contains(amount.toString()));
        assertTrue(toString.contains(description));
    }
}