package com.github.djoarns.payflow.domain.bill.valueobjects;

import com.github.djoarns.payflow.domain.bill.exceptions.InvalidBillDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BillIdTest {

    @Nested
    @DisplayName("BillId.of(Long)")
    class BillIdOfLong {
        @Test
        @DisplayName("Should create BillId when id is valid")
        void shouldCreateBillIdWhenValid() {
            // Arrange
            Long validId = 1L;

            // Act
            BillId billId = BillId.of(validId);

            // Assert
            assertNotNull(billId);
            assertEquals(validId, billId.getValue());
        }

        @Test
        @DisplayName("Should throw exception when id is null")
        void shouldThrowExceptionWhenNull() {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> BillId.of((Long) null)
            );
            assertEquals("Bill ID cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when id is zero")
        void shouldThrowExceptionWhenZero() {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> BillId.of(0L)
            );
            assertEquals("Bill ID must be greater than zero", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when id is negative")
        void shouldThrowExceptionWhenNegative() {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> BillId.of(-1L)
            );
            assertEquals("Bill ID must be greater than zero", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("BillId.of(String)")
    class BillIdOfString {
        @Test
        @DisplayName("Should create BillId when string id is valid")
        void shouldCreateBillIdWhenStringValid() {
            // Arrange
            String validId = "1";

            // Act
            BillId billId = BillId.of(validId);

            // Assert
            assertNotNull(billId);
            assertEquals(1L, billId.getValue());
        }

        @ParameterizedTest
        @ValueSource(strings = {"abc", "1.5", "1,5", "", " "})
        @DisplayName("Should throw exception when string id is invalid")
        void shouldThrowExceptionWhenStringInvalid(String invalidId) {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> BillId.of(invalidId)
            );
            assertEquals("Invalid Bill ID format", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when string id is null")
        void shouldThrowExceptionWhenStringNull() {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> BillId.of((String) null)
            );
            assertEquals("Invalid Bill ID format", exception.getMessage());
        }
    }

    @Test
    @DisplayName("BillId should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        BillId id1 = BillId.of(1L);
        BillId id2 = BillId.of(1L);
        BillId id3 = BillId.of(2L);

        // Assert
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }
}
