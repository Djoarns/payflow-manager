package com.github.djoarns.payflow.domain.bill.valueobject;

import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillOperationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AmountTest {

    @Nested
    @DisplayName("Amount.of")
    class AmountOf {
        @Test
        @DisplayName("Should create Amount when value is valid")
        void shouldCreateAmountWhenValid() {
            // Arrange
            BigDecimal validAmount = new BigDecimal("100.50");

            // Act
            Amount amount = Amount.of(validAmount);

            // Assert
            assertNotNull(amount);
            assertEquals(validAmount, amount.getValue());
        }

        @Test
        @DisplayName("Should throw exception when amount is null")
        void shouldThrowExceptionWhenNull() {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> Amount.of(null)
            );
            assertEquals("Amount cannot be null", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "-1", "-100.50"})
        @DisplayName("Should throw exception when amount is zero or negative")
        void shouldThrowExceptionWhenZeroOrNegative(String invalidAmount) {
            // Arrange
            BigDecimal amount = new BigDecimal(invalidAmount);

            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> Amount.of(amount)
            );
            assertEquals("Amount must be greater than zero", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Amount.add")
    class AmountAdd {
        @Test
        @DisplayName("Should add two amounts correctly")
        void shouldAddAmountsCorrectly() {
            // Arrange
            Amount amount1 = Amount.of(new BigDecimal("100.50"));
            Amount amount2 = Amount.of(new BigDecimal("50.25"));
            BigDecimal expected = new BigDecimal("150.75");

            // Act
            Amount result = amount1.add(amount2);

            // Assert
            assertEquals(expected, result.getValue());
        }

        @Test
        @DisplayName("Should throw exception when adding null amount")
        void shouldThrowExceptionWhenAddingNull() {
            // Arrange
            Amount amount = Amount.of(new BigDecimal("100.50"));

            // Act & Assert
            InvalidBillOperationException exception = assertThrows(
                    InvalidBillOperationException.class,
                    () -> amount.add(null)
            );
            assertEquals("Cannot add null amount", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Amount.subtract")
    class AmountSubtract {
        @Test
        @DisplayName("Should subtract two amounts correctly")
        void shouldSubtractAmountsCorrectly() {
            // Arrange
            Amount amount1 = Amount.of(new BigDecimal("100.50"));
            Amount amount2 = Amount.of(new BigDecimal("50.25"));
            BigDecimal expected = new BigDecimal("50.25");

            // Act
            Amount result = amount1.subtract(amount2);

            // Assert
            assertEquals(expected, result.getValue());
        }

        @Test
        @DisplayName("Should throw exception when subtracting null amount")
        void shouldThrowExceptionWhenSubtractingNull() {
            // Arrange
            Amount amount = Amount.of(new BigDecimal("100.50"));

            // Act & Assert
            InvalidBillOperationException exception = assertThrows(
                    InvalidBillOperationException.class,
                    () -> amount.subtract(null)
            );
            assertEquals("Cannot subtract null amount", exception.getMessage());
        }
    }

    @Test
    @DisplayName("Amount should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        Amount amount1 = Amount.of(new BigDecimal("100.50"));
        Amount amount2 = Amount.of(new BigDecimal("100.50"));
        Amount amount3 = Amount.of(new BigDecimal("200.00"));

        // Assert
        assertEquals(amount1, amount2);
        assertNotEquals(amount1, amount3);
        assertEquals(amount1.hashCode(), amount2.hashCode());
        assertNotEquals(amount1.hashCode(), amount3.hashCode());
    }
}