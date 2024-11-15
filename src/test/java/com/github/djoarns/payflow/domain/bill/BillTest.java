package com.github.djoarns.payflow.domain.bill;

import com.github.djoarns.payflow.domain.bill.exceptions.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.exceptions.InvalidBillStatusException;
import com.github.djoarns.payflow.domain.bill.valueobjects.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BillTest {

    @Nested
    @DisplayName("Bill.create")
    class BillCreate {
        @Test
        @DisplayName("Should create bill with valid data")
        void shouldCreateBillWithValidData() {
            // Arrange
            DueDate dueDate = DueDate.of(LocalDate.now().plusDays(7));
            Amount amount = Amount.of(new BigDecimal("100.00"));
            Description description = Description.of("Test Bill");

            // Act
            Bill bill = Bill.create(dueDate, amount, description);

            // Assert
            assertNull(bill.getId());
            assertEquals(dueDate, bill.getDueDate());
            assertEquals(amount, bill.getAmount());
            assertEquals(description, bill.getDescription());
            assertEquals(Status.PENDING, bill.getStatus());
            assertNull(bill.getPaymentDate());
        }

        @Test
        @DisplayName("Should throw exception when dueDate is null")
        void shouldThrowExceptionWhenDueDateNull() {
            // Arrange
            Amount amount = Amount.of(new BigDecimal("100.00"));
            Description description = Description.of("Test Bill");

            // Act & Assert
            assertThrows(
                    InvalidBillDataException.class,
                    () -> Bill.create(null, amount, description),
                    "All bill data must be provided"
            );
        }

        @Test
        @DisplayName("Should throw exception when amount is null")
        void shouldThrowExceptionWhenAmountNull() {
            // Arrange
            DueDate dueDate = DueDate.of(LocalDate.now().plusDays(7));
            Description description = Description.of("Test Bill");

            // Act & Assert
            assertThrows(
                    InvalidBillDataException.class,
                    () -> Bill.create(dueDate, null, description),
                    "All bill data must be provided"
            );
        }

        @Test
        @DisplayName("Should throw exception when description is null")
        void shouldThrowExceptionWhenDescriptionNull() {
            // Arrange
            DueDate dueDate = DueDate.of(LocalDate.now().plusDays(7));
            Amount amount = Amount.of(new BigDecimal("100.00"));

            // Act & Assert
            assertThrows(
                    InvalidBillDataException.class,
                    () -> Bill.create(dueDate, amount, null),
                    "All bill data must be provided"
            );
        }
    }

    @Nested
    @DisplayName("Bill.pay")
    class BillPay {
        @Test
        @DisplayName("Should pay pending bill")
        void shouldPayPendingBill() {
            // Arrange
            Bill bill = createValidBill();
            PaymentDate paymentDate = PaymentDate.of(LocalDate.now());

            // Act
            bill.pay(paymentDate);

            // Assert
            assertEquals(Status.PAID, bill.getStatus());
            assertEquals(paymentDate, bill.getPaymentDate());
        }

        @Test
        @DisplayName("Should pay overdue bill")
        void shouldPayOverdueBill() {
            // Arrange
            Bill bill = createValidBill();
            bill.update(
                    DueDate.of(LocalDate.now().minusDays(1)),
                    bill.getAmount(),
                    bill.getDescription()
            );
            PaymentDate paymentDate = PaymentDate.of(LocalDate.now());

            // Act
            bill.pay(paymentDate);

            // Assert
            assertEquals(Status.PAID, bill.getStatus());
            assertEquals(paymentDate, bill.getPaymentDate());
        }

        @Test
        @DisplayName("Should throw exception when paying cancelled bill")
        void shouldThrowExceptionWhenPayingCancelledBill() {
            // Arrange
            Bill bill = createValidBill();
            bill.cancel();

            // Act & Assert
            assertThrows(
                    InvalidBillStatusException.class,
                    () -> bill.pay(PaymentDate.of(LocalDate.now())),
                    "Bill cannot be paid in current status: CANCELLED"
            );
        }

        @Test
        @DisplayName("Should throw exception when paying paid bill")
        void shouldThrowExceptionWhenPayingPaidBill() {
            // Arrange
            Bill bill = createValidBill();
            bill.pay(PaymentDate.of(LocalDate.now()));

            // Act & Assert
            assertThrows(
                    InvalidBillStatusException.class,
                    () -> bill.pay(PaymentDate.of(LocalDate.now())),
                    "Bill cannot be paid in current status: PAID"
            );
        }

        @Test
        @DisplayName("Should throw exception when payment date is null")
        void shouldThrowExceptionWhenPaymentDateNull() {
            // Arrange
            Bill bill = createValidBill();

            // Act & Assert
            assertThrows(
                    InvalidBillDataException.class,
                    () -> bill.pay(null),
                    "Payment date must be provided"
            );
        }
    }

    @Nested
    @DisplayName("Bill.update")
    class BillUpdate {
        @Test
        @DisplayName("Should update pending bill")
        void shouldUpdatePendingBill() {
            // Arrange
            Bill bill = createValidBill();
            DueDate newDueDate = DueDate.of(LocalDate.now().plusDays(14));
            Amount newAmount = Amount.of(new BigDecimal("200.00"));
            Description newDescription = Description.of("Updated Bill");

            // Act
            bill.update(newDueDate, newAmount, newDescription);

            // Assert
            assertEquals(newDueDate, bill.getDueDate());
            assertEquals(newAmount, bill.getAmount());
            assertEquals(newDescription, bill.getDescription());
            assertEquals(Status.PENDING, bill.getStatus());
        }

        @Test
        @DisplayName("Should throw exception when updating cancelled bill")
        void shouldThrowExceptionWhenUpdatingCancelledBill() {
            // Arrange
            Bill bill = createValidBill();
            bill.cancel();
            DueDate newDueDate = DueDate.of(LocalDate.now().plusDays(14));
            Amount newAmount = Amount.of(new BigDecimal("200.00"));
            Description newDescription = Description.of("Updated Bill");

            // Act & Assert
            assertThrows(
                    InvalidBillStatusException.class,
                    () -> bill.update(newDueDate, newAmount, newDescription),
                    "Bill cannot be modified in current status: CANCELLED"
            );
        }

        @Test
        @DisplayName("Should throw exception when updating with null values")
        void shouldThrowExceptionWhenUpdatingWithNullValues() {
            // Arrange
            Bill bill = createValidBill();

            // Act & Assert
            assertThrows(
                    InvalidBillDataException.class,
                    () -> bill.update(null, null, null),
                    "All update data must be provided"
            );
        }
    }

    @Nested
    @DisplayName("Bill.cancel")
    class BillCancel {
        @Test
        @DisplayName("Should cancel pending bill")
        void shouldCancelPendingBill() {
            // Arrange
            Bill bill = createValidBill();

            // Act
            bill.cancel();

            // Assert
            assertEquals(Status.CANCELLED, bill.getStatus());
        }

        @Test
        @DisplayName("Should throw exception when cancelling paid bill")
        void shouldThrowExceptionWhenCancellingPaidBill() {
            // Arrange
            Bill bill = createValidBill();
            bill.pay(PaymentDate.of(LocalDate.now()));

            // Act & Assert
            assertThrows(
                    InvalidBillStatusException.class,
                    () -> bill.cancel(),
                    "Bill cannot be cancelled in current status: PAID"
            );
        }
    }

    private Bill createValidBill() {
        return Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Test Bill")
        );
    }
}