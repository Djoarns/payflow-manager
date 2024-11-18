package com.github.djoarns.payflow.domain.bill;

import com.github.djoarns.payflow.domain.bill.exception.*;
import com.github.djoarns.payflow.domain.bill.valueobject.*;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BillTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create bill successfully")
    void shouldCreateBillSuccessfully() {
        // Arrange
        var dueDate = DueDate.of(LocalDate.now().plusDays(30));
        var amount = Amount.of(new BigDecimal("100.0"));
        var description = Description.of("Test Bill");

        // Act
        var bill = Bill.create(dueDate, amount, description);

        // Assert
        assertNotNull(bill);
        assertEquals(dueDate, bill.getDueDate());
        assertEquals(amount, bill.getAmount());
        assertEquals(description, bill.getDescription());
        assertEquals(Status.PENDING, bill.getStatus());
        assertNull(bill.getId());
        assertNull(bill.getPaymentDate());
    }

    @Test
    @DisplayName("Should throw exception when creating bill with null values")
    void shouldThrowExceptionWhenCreatingBillWithNullValues() {
        // Arrange
        var dueDate = DueDate.of(LocalDate.now().plusDays(30));
        var amount = Amount.of(new BigDecimal("100.0"));
        var description = Description.of("Test Bill");

        // Act & Assert
        assertThrows(InvalidBillDataException.class,
                () -> Bill.create(null, amount, description));
        assertThrows(InvalidBillDataException.class,
                () -> Bill.create(dueDate, null, description));
        assertThrows(InvalidBillDataException.class,
                () -> Bill.create(dueDate, amount, null));
    }

    @Test
    @DisplayName("Should pay bill successfully")
    void shouldPayBillSuccessfully() {
        // Arrange
        var bill = createPendingBill();
        var paymentDate = PaymentDate.of(LocalDate.now());

        // Act
        bill.pay(paymentDate);

        // Assert
        assertEquals(Status.PAID, bill.getStatus());
        assertEquals(paymentDate, bill.getPaymentDate());
    }

    @Test
    @DisplayName("Should throw exception when paying bill with null payment date")
    void shouldThrowExceptionWhenPayingBillWithNullPaymentDate() {
        // Arrange
        var bill = createPendingBill();

        // Act & Assert
        assertThrows(InvalidBillDataException.class, () -> bill.pay(null));
    }

    @Test
    @DisplayName("Should throw exception when paying bill in invalid status")
    void shouldThrowExceptionWhenPayingBillInInvalidStatus() {
        // Arrange
        var bill = createPaidBill();
        var paymentDate = PaymentDate.of(LocalDate.now());

        // Act & Assert
        assertThrows(InvalidBillStatusException.class, () -> bill.pay(paymentDate));
    }

    @Test
    @DisplayName("Should update bill successfully")
    void shouldUpdateBillSuccessfully() {
        // Arrange
        var bill = createPendingBill();
        var newDueDate = DueDate.of(LocalDate.now().plusDays(60));
        var newAmount = Amount.of(new BigDecimal("200.0"));
        var newDescription = Description.of("Updated Test Bill");

        // Act
        bill.update(newDueDate, newAmount, newDescription);

        // Assert
        assertEquals(newDueDate, bill.getDueDate());
        assertEquals(newAmount, bill.getAmount());
        assertEquals(newDescription, bill.getDescription());
    }

    @Test
    @DisplayName("Should throw exception when updating bill with null values")
    void shouldThrowExceptionWhenUpdatingBillWithNullValues() {
        // Arrange
        var bill = createPendingBill();
        var newDueDate = DueDate.of(LocalDate.now().plusDays(60));
        var newAmount = Amount.of(new BigDecimal("200.0"));
        var newDescription = Description.of("Updated Test Bill");

        // Act & Assert
        assertThrows(InvalidBillDataException.class,
                () -> bill.update(null, newAmount, newDescription));
        assertThrows(InvalidBillDataException.class,
                () -> bill.update(newDueDate, null, newDescription));
        assertThrows(InvalidBillDataException.class,
                () -> bill.update(newDueDate, newAmount, null));
    }

    @Test
    @DisplayName("Should throw exception when updating bill in invalid status")
    void shouldThrowExceptionWhenUpdatingBillInInvalidStatus() {
        // Arrange
        var bill = createPaidBill();
        var newDueDate = DueDate.of(LocalDate.now().plusDays(60));
        var newAmount = Amount.of(new BigDecimal("200.0"));
        var newDescription = Description.of("Updated Test Bill");

        // Act & Assert
        assertThrows(InvalidBillStatusException.class,
                () -> bill.update(newDueDate, newAmount, newDescription));
    }

    @Test
    @DisplayName("Should cancel bill successfully")
    void shouldCancelBillSuccessfully() {
        // Arrange
        var bill = createPendingBill();

        // Act
        bill.cancel();

        // Assert
        assertEquals(Status.CANCELLED, bill.getStatus());
    }

    @Test
    @DisplayName("Should throw exception when cancelling bill in invalid status")
    void shouldThrowExceptionWhenCancellingBillInInvalidStatus() {
        // Arrange
        var bill = createPaidBill();

        // Act & Assert
        assertThrows(InvalidBillStatusException.class, () -> bill.cancel());
    }

    @Test
    @DisplayName("Should reconstitute bill successfully")
    void shouldReconstituteBillSuccessfully() {
        // Arrange
        var id = BillId.of(1L);
        var dueDate = DueDate.of(LocalDate.now().plusDays(30));
        var paymentDate = PaymentDate.of(LocalDate.now());
        var amount = Amount.of(new BigDecimal("100.0"));
        var description = Description.of("Test Bill");
        var status = Status.PAID;

        // Act
        var bill = Bill.reconstitute(id, dueDate, paymentDate, amount, description, status);

        // Assert
        assertEquals(id, bill.getId());
        assertEquals(dueDate, bill.getDueDate());
        assertEquals(paymentDate, bill.getPaymentDate());
        assertEquals(amount, bill.getAmount());
        assertEquals(description, bill.getDescription());
        assertEquals(status, bill.getStatus());
    }

    private Bill createPendingBill() {
        return Bill.create(
                DueDate.of(LocalDate.now().plusDays(30)),
                Amount.of(new BigDecimal("100.0")),
                Description.of("Test Bill")
        );
    }

    private Bill createPaidBill() {
        var bill = createPendingBill();
        bill.pay(PaymentDate.of(LocalDate.now()));
        return bill;
    }
}