package com.github.djoarns.payflow.application.bill.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillStatusException;
import com.github.djoarns.payflow.domain.bill.valueobject.*;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PayBillUseCaseTest extends BaseUnitTest {

    @Mock
    private BillRepository billRepository;

    private PayBillUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new PayBillUseCase(billRepository);
    }

    @Test
    @DisplayName("Should pay bill successfully")
    void shouldPayBillSuccessfully() {
        // Arrange
        var existingBill = Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Test Bill")
        );

        var paymentDate = LocalDate.now();
        var command = new BillCommand.Pay(1L, paymentDate);

        when(billRepository.findById(any(BillId.class)))
                .thenReturn(Optional.of(existingBill));
        when(billRepository.save(any(Bill.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = useCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(Status.PAID, result.bill().getStatus());
        assertEquals(paymentDate, result.bill().getPaymentDate().getValue());
        verify(billRepository).save(result.bill());
    }

    @Test
    @DisplayName("Should throw exception when bill not found")
    void shouldThrowExceptionWhenBillNotFound() {
        // Arrange
        when(billRepository.findById(any(BillId.class)))
                .thenReturn(Optional.empty());

        BillCommand.Pay command = new BillCommand.Pay(999L, LocalDate.now());

        // Act & Assert
        assertThrows(InvalidBillDataException.class, () -> useCase.execute(command));
        verify(billRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when bill is already paid")
    void shouldThrowExceptionWhenBillAlreadyPaid() {
        // Arrange
        var existingBill = Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Test Bill")
        );
        existingBill.pay(PaymentDate.of(LocalDate.now().minusDays(1)));

        when(billRepository.findById(any(BillId.class)))
                .thenReturn(Optional.of(existingBill));

        BillCommand.Pay command = new BillCommand.Pay(1L, LocalDate.now());

        // Act & Assert
        assertThrows(InvalidBillStatusException.class, () -> useCase.execute(command));
        verify(billRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when payment date is in future")
    void shouldThrowExceptionWhenPaymentDateInFuture() {
        // Arrange
        var existingBill = Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Test Bill")
        );

        when(billRepository.findById(any(BillId.class)))
                .thenReturn(Optional.of(existingBill));

        BillCommand.Pay command = new BillCommand.Pay(1L, LocalDate.now().plusDays(1));

        // Act & Assert
        assertThrows(InvalidBillDataException.class, () -> useCase.execute(command));
        verify(billRepository, never()).save(any());
    }
}