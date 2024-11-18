package com.github.djoarns.payflow.application.bill.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.usecase.FindBillUseCase;
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
import static org.mockito.Mockito.when;

class FindBillUseCaseTest extends BaseUnitTest {

    @Mock
    private BillRepository billRepository;

    private FindBillUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindBillUseCase(billRepository);
    }

    @Test
    @DisplayName("Should find existing bill")
    void shouldFindExistingBill() {
        // Arrange
        var existingBill = Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Test Bill")
        );

        when(billRepository.findById(any(BillId.class)))
                .thenReturn(Optional.of(existingBill));

        BillCommand.Find command = new BillCommand.Find(1L);

        // Act
        var result = useCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(existingBill, result.bill());
    }

    @Test
    @DisplayName("Should throw exception when bill not found")
    void shouldThrowExceptionWhenBillNotFound() {
        // Arrange
        when(billRepository.findById(any(BillId.class)))
                .thenReturn(Optional.empty());

        BillCommand.Find command = new BillCommand.Find(999L);

        // Act & Assert
        assertThrows(InvalidBillDataException.class, () -> useCase.execute(command));
    }
}