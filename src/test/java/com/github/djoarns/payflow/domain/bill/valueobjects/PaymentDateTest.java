package com.github.djoarns.payflow.domain.bill.valueobjects;

import com.github.djoarns.payflow.domain.bill.exceptions.InvalidBillDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDateTest {

    @Nested
    @DisplayName("PaymentDate.of")
    class PaymentDateOf {
        @Test
        @DisplayName("Should create PaymentDate when date is valid")
        void shouldCreatePaymentDateWhenValid() {
            // Arrange
            LocalDate validDate = LocalDate.now();

            // Act
            PaymentDate paymentDate = PaymentDate.of(validDate);

            // Assert
            assertNotNull(paymentDate);
            assertEquals(validDate, paymentDate.getValue());
        }

        @Test
        @DisplayName("Should create PaymentDate when date is in the past")
        void shouldCreatePaymentDateWhenPast() {
            // Arrange
            LocalDate pastDate = LocalDate.now().minusDays(7);

            // Act
            PaymentDate paymentDate = PaymentDate.of(pastDate);

            // Assert
            assertNotNull(paymentDate);
            assertEquals(pastDate, paymentDate.getValue());
        }

        @Test
        @DisplayName("Should throw exception when date is null")
        void shouldThrowExceptionWhenNull() {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> PaymentDate.of(null)
            );
            assertEquals("Payment date cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when date is in the future")
        void shouldThrowExceptionWhenFuture() {
            // Arrange
            LocalDate futureDate = LocalDate.now().plusDays(1);

            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> PaymentDate.of(futureDate)
            );
            assertEquals("Payment date cannot be in the future", exception.getMessage());
        }
    }

    @Test
    @DisplayName("PaymentDate should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        LocalDate date = LocalDate.now();
        PaymentDate paymentDate1 = PaymentDate.of(date);
        PaymentDate paymentDate2 = PaymentDate.of(date);
        PaymentDate paymentDate3 = PaymentDate.of(date.minusDays(1));

        // Assert
        assertEquals(paymentDate1, paymentDate2);
        assertNotEquals(paymentDate1, paymentDate3);
        assertEquals(paymentDate1.hashCode(), paymentDate2.hashCode());
        assertNotEquals(paymentDate1.hashCode(), paymentDate3.hashCode());
    }
}