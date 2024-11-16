package com.github.djoarns.payflow.application.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.usecase.ListBillsUseCase;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.valueobject.*;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ListBillsUseCaseTest extends BaseUnitTest {

    @Mock
    private BillRepository billRepository;

    private ListBillsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListBillsUseCase(billRepository);
    }

    @Test
    @DisplayName("Should list bills with pagination")
    void shouldListBillsWithPagination() {
        // Arrange
        var startDate = LocalDate.now();
        var endDate = startDate.plusMonths(1);
        var description = "Test";
        var page = 0;
        var size = 10;

        var bills = List.of(
                createBill("Bill 1"),
                createBill("Bill 2")
        );

        when(billRepository.findByDueDateBetweenAndDescription(
                any(LocalDate.class),
                any(LocalDate.class),
                anyString(),
                anyInt(),
                anyInt()
        )).thenReturn(bills);

        when(billRepository.countByDueDateBetweenAndDescription(
                any(LocalDate.class),
                any(LocalDate.class),
                anyString()
        )).thenReturn(15L);

        // Act
        var result = useCase.execute(new BillCommand.List(
                startDate,
                endDate,
                description,
                page,
                size
        ));

        // Assert
        assertNotNull(result);
        assertEquals(bills, result.bills());
        assertEquals(15, result.totalElements());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.hasNext());
        assertFalse(result.hasPrevious());
    }

    @Test
    @DisplayName("Should return empty list when no bills found")
    void shouldReturnEmptyListWhenNoBillsFound() {
        // Arrange
        when(billRepository.findByDueDateBetweenAndDescription(
                any(LocalDate.class),
                any(LocalDate.class),
                anyString(),
                anyInt(),
                anyInt()
        )).thenReturn(List.of());

        when(billRepository.countByDueDateBetweenAndDescription(
                any(LocalDate.class),
                any(LocalDate.class),
                anyString()
        )).thenReturn(0L);

        // Act
        var result = useCase.execute(new BillCommand.List(
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                "Test",
                0,
                10
        ));

        // Assert
        assertTrue(result.bills().isEmpty());
        assertEquals(0, result.totalElements());
        assertEquals(0, result.getTotalPages());
        assertFalse(result.hasNext());
        assertFalse(result.hasPrevious());
    }

    private Bill createBill(String description) {
        return Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of(description)
        );
    }
}