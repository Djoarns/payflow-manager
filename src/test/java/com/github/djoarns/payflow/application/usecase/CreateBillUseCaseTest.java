package com.github.djoarns.payflow.application.usecase;

import com.github.djoarns.payflow.application.command.BillCommand;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.valueobject.Status;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateBillUseCaseTest extends BaseUnitTest {

    @Mock
    private BillRepository billRepository;

    private CreateBillUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateBillUseCase(billRepository);
    }

    @Test
    @DisplayName("Should create valid bill")
    void shouldCreateValidBill() {
        // Arrange
        var command = new BillCommand.Create(
                LocalDate.now().plusDays(7),
                new BigDecimal("100.00"),
                "Test Bill"
        );

        when(billRepository.save(any(Bill.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = useCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(Status.PENDING, result.bill().getStatus());
        assertNull(result.bill().getPaymentDate());

        var billCaptor = ArgumentCaptor.forClass(Bill.class);
        verify(billRepository).save(billCaptor.capture());

        var capturedBill = billCaptor.getValue();
        assertEquals(command.dueDate(), capturedBill.getDueDate().getValue());
        assertEquals(command.amount(), capturedBill.getAmount().getValue());
        assertEquals(command.description(), capturedBill.getDescription().getValue());
    }

    @Test
    @DisplayName("Should throw exception when amount is invalid")
    void shouldThrowExceptionWhenAmountInvalid() {
        // Arrange
        var command = new BillCommand.Create(
                LocalDate.now().plusDays(7),
                BigDecimal.ZERO,
                "Test Bill"
        );

        // Act & Assert
        assertThrows(InvalidBillDataException.class, () -> useCase.execute(command));
        verify(billRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when description is empty")
    void shouldThrowExceptionWhenDescriptionEmpty() {
        // Arrange
        var command = new BillCommand.Create(
                LocalDate.now().plusDays(7),
                new BigDecimal("100.00"),
                ""
        );

        // Act & Assert
        assertThrows(InvalidBillDataException.class, () -> useCase.execute(command));
        verify(billRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when due date is null")
    void shouldThrowExceptionWhenDueDateNull() {
        // Arrange
        var command = new BillCommand.Create(
                null,
                new BigDecimal("100.00"),
                "Test Bill"
        );

        // Act & Assert
        assertThrows(InvalidBillDataException.class, () -> useCase.execute(command));
        verify(billRepository, never()).save(any());
    }
}