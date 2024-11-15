package com.github.djoarns.payflow.domain.bill.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    @DisplayName("Should have correct enum values")
    void shouldHaveCorrectEnumValues() {
        // Arrange & Act
        Status[] statuses = Status.values();

        // Assert
        assertEquals(4, statuses.length);
        assertArrayEquals(
                new Status[] {
                        Status.PENDING,
                        Status.PAID,
                        Status.OVERDUE,
                        Status.CANCELLED
                },
                statuses
        );
    }

    @Test
    @DisplayName("Should correctly convert to string")
    void shouldCorrectlyConvertToString() {
        assertEquals("PENDING", Status.PENDING.toString());
        assertEquals("PAID", Status.PAID.toString());
        assertEquals("OVERDUE", Status.OVERDUE.toString());
        assertEquals("CANCELLED", Status.CANCELLED.toString());
    }

    @Test
    @DisplayName("Should maintain enum singleton pattern")
    void shouldMaintainEnumSingleton() {
        assertSame(Status.PENDING, Status.valueOf("PENDING"));
        assertSame(Status.PAID, Status.valueOf("PAID"));
        assertSame(Status.OVERDUE, Status.valueOf("OVERDUE"));
        assertSame(Status.CANCELLED, Status.valueOf("CANCELLED"));
    }
}