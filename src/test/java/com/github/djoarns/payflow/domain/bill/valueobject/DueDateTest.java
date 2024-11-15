package com.github.djoarns.payflow.domain.bill.valueobject;

import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DueDateTest {

    @Nested
    @DisplayName("DueDate.of")
    class DueDateOf {
        @Test
        @DisplayName("Should create DueDate when date is valid")
        void shouldCreateDueDateWhenValid() {
            // Arrange
            LocalDate validDate = LocalDate.now().plusDays(7);

            // Act
            DueDate dueDate = DueDate.of(validDate);

            // Assert
            assertNotNull(dueDate);
            assertEquals(validDate, dueDate.getValue());
        }

        @Test
        @DisplayName("Should accept past dates")
        void shouldAcceptPastDates() {
            // Arrange
            LocalDate pastDate = LocalDate.now().minusDays(7);

            // Act
            DueDate dueDate = DueDate.of(pastDate);

            // Assert
            assertNotNull(dueDate);
            assertEquals(pastDate, dueDate.getValue());
        }

        @Test
        @DisplayName("Should throw exception when date is null")
        void shouldThrowExceptionWhenNull() {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> DueDate.of(null)
            );
            assertEquals("Due date cannot be null", exception.getMessage());
        }
    }

    @Test
    @DisplayName("DueDate should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        LocalDate date = LocalDate.now();
        DueDate dueDate1 = DueDate.of(date);
        DueDate dueDate2 = DueDate.of(date);
        DueDate dueDate3 = DueDate.of(date.plusDays(1));

        // Assert
        assertEquals(dueDate1, dueDate2);
        assertNotEquals(dueDate1, dueDate3);
        assertEquals(dueDate1.hashCode(), dueDate2.hashCode());
        assertNotEquals(dueDate1.hashCode(), dueDate3.hashCode());
    }
}