package com.github.djoarns.payflow.application.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.application.bill.usecase.CalculateTotalPaidUseCase;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.valueobject.Amount;
import com.github.djoarns.payflow.domain.bill.valueobject.Description;
import com.github.djoarns.payflow.domain.bill.valueobject.DueDate;
import com.github.djoarns.payflow.domain.bill.valueobject.PaymentDate;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculateTotalPaidUseCaseTest extends BaseUnitTest {

    @Mock
    private BillRepository billRepository;

    private CalculateTotalPaidUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CalculateTotalPaidUseCase(billRepository);
    }

    @Test
    @DisplayName("Should calculate total amount for paid bills")
    void shouldCalculateTotalAmountForPaidBills() {
        // Arrange
        var startDate = LocalDate.now().minusMonths(1);
        var endDate = LocalDate.now();
        var paymentDate = LocalDate.now().minusDays(1);

        var bill1 = createPaidBill("Bill 1", new BigDecimal("100.00"), paymentDate);
        var bill2 = createPaidBill("Bill 2", new BigDecimal("200.00"), paymentDate);

        when(billRepository.findByPaymentDateBetween(any(), any()))
                .thenReturn(List.of(bill1, bill2));

        // Act
        var result = useCase.execute(new BillCommand.CalculateTotalPaid(startDate, endDate));

        // Assert
        assertEquals(new BigDecimal("300.00"), result.totalPaid().getValue());
    }

    @Test
    @DisplayName("Should return zero when no paid bills found")
    void shouldReturnZeroWhenNoPaidBillsFound() {
        // Arrange
        when(billRepository.findByPaymentDateBetween(any(), any()))
                .thenReturn(List.of());

        // Act
        var result = useCase.execute(new BillCommand.CalculateTotalPaid(
                LocalDate.now().minusMonths(1),
                LocalDate.now()
        ));

        // Assert
        assertEquals(BigDecimal.ZERO, result.totalPaid().getValue());
    }

    private Bill createPaidBill(String description, BigDecimal amount, LocalDate paymentDate) {
        var bill = Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(amount),
                Description.of(description)
        );
        bill.pay(PaymentDate.of(paymentDate));
        return bill;
    }
}