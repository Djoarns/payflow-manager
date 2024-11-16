package com.github.djoarns.payflow.application.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.usecase.UpdateBillUseCase;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
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

class UpdateBillUseCaseTest extends BaseUnitTest {

    @Mock
    private BillRepository billRepository;

    private UpdateBillUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateBillUseCase(billRepository);
    }

    @Test
    @DisplayName("Should update bill successfully")
    void shouldUpdateBillSuccessfully() {
        // Arrange
        var existingBill = Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Original Bill")
        );

        var command = new BillCommand.Update(
                1L,
                LocalDate.now().plusDays(14),
                new BigDecimal("200.00"),
                "Updated Bill"
        );

        when(billRepository.findById(any(BillId.class)))
                .thenReturn(Optional.of(existingBill));
        when(billRepository.save(any(Bill.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = useCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(command.dueDate(), result.bill().getDueDate().getValue());
        assertEquals(command.amount(), result.bill().getAmount().getValue());
        assertEquals(command.description(), result.bill().getDescription().getValue());
        verify(billRepository).save(result.bill());
    }

    @Test
    @DisplayName("Should throw exception when bill not found")
    void shouldThrowExceptionWhenBillNotFound() {
        // Arrange
        var command = new BillCommand.Update(
                999L,
                LocalDate.now().plusDays(14),
                new BigDecimal("200.00"),
                "Updated Bill"
        );

        when(billRepository.findById(any(BillId.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidBillDataException.class, () -> useCase.execute(command));
        verify(billRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when update data is invalid")
    void shouldThrowExceptionWhenUpdateDataInvalid() {
        // Arrange
        var existingBill = Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Original Bill")
        );

        var command = new BillCommand.Update(
                1L,
                LocalDate.now().plusDays(14),
                BigDecimal.ZERO,
                "Updated Bill"
        );

        when(billRepository.findById(any(BillId.class)))
                .thenReturn(Optional.of(existingBill));

        // Act & Assert
        assertThrows(InvalidBillDataException.class, () -> useCase.execute(command));
        verify(billRepository, never()).save(any());
    }
}