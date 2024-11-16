package com.github.djoarns.payflow.application.mapper;

import com.github.djoarns.payflow.application.bill.mapper.BillResponseMapper;
import com.github.djoarns.payflow.application.bill.result.BillResult;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.valueobject.*;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BillResponseMapperTest extends BaseUnitTest {

    private BillResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BillResponseMapper();
    }

    @Nested
    @DisplayName("toResponseDTO")
    class ToResponseDTO {
        @Test
        @DisplayName("Should map unpaid bill to response DTO")
        void shouldMapUnpaidBillToResponseDto() {
            // Arrange
            var bill = createUnpaidBill();

            // Act
            var dto = mapper.toResponseDTO(bill);

            // Assert
            assertNotNull(dto);
            assertEquals(bill.getDueDate().getValue(), dto.dueDate());
            assertEquals(bill.getAmount().getValue(), dto.amount());
            assertEquals(bill.getDescription().getValue(), dto.description());
            assertEquals(bill.getStatus().name(), dto.status());
            assertNull(dto.paymentDate());
        }

        @Test
        @DisplayName("Should map paid bill to response DTO")
        void shouldMapPaidBillToResponseDto() {
            // Arrange
            var bill = createPaidBill();

            // Act
            var dto = mapper.toResponseDTO(bill);

            // Assert
            assertNotNull(dto);
            assertEquals(bill.getPaymentDate().getValue(), dto.paymentDate());
            assertEquals(Status.PAID.name(), dto.status());
        }

        @Test
        @DisplayName("Should map cancelled bill to response DTO")
        void shouldMapCancelledBillToResponseDto() {
            // Arrange
            var bill = createCancelledBill();

            // Act
            var dto = mapper.toResponseDTO(bill);

            // Assert
            assertNotNull(dto);
            assertEquals(Status.CANCELLED.name(), dto.status());
        }
    }

    @Nested
    @DisplayName("toPageDTO")
    class ToPageDTO {
        @Test
        @DisplayName("Should map empty list to page DTO")
        void shouldMapEmptyListToPageDto() {
            // Arrange
            var result = new BillResult.List(List.of(), 0, 0, 10);

            // Act
            var dto = mapper.toPageDTO(result);

            // Assert
            assertTrue(dto.content().isEmpty());
            assertEquals(0, dto.totalElements());
            assertFalse(dto.hasNext());
            assertFalse(dto.hasPrevious());
        }

        @Test
        @DisplayName("Should map single page list to page DTO")
        void shouldMapSinglePageListToPageDto() {
            // Arrange
            var bills = List.of(createUnpaidBill());
            var result = new BillResult.List(bills, 1, 0, 10);

            // Act
            var dto = mapper.toPageDTO(result);

            // Assert
            assertEquals(1, dto.content().size());
            assertEquals(1, dto.totalElements());
            assertFalse(dto.hasNext());
            assertFalse(dto.hasPrevious());
        }

        @Test
        @DisplayName("Should map multiple pages list to page DTO")
        void shouldMapMultiplePagesListToPageDto() {
            // Arrange
            var bills = List.of(createUnpaidBill(), createPaidBill());
            var result = new BillResult.List(bills, 30, 1, 10);

            // Act
            var dto = mapper.toPageDTO(result);

            // Assert
            assertEquals(2, dto.content().size());
            assertEquals(30, dto.totalElements());
            assertTrue(dto.hasNext());
            assertTrue(dto.hasPrevious());
        }
    }

    @Nested
    @DisplayName("toTotalPaidDTO")
    class ToTotalPaidDTO {
        @Test
        @DisplayName("Should map zero total paid to DTO")
        void shouldMapZeroTotalPaidToDto() {
            // Arrange
            var result = new BillResult.TotalPaid(Amount.of(BigDecimal.ONE));
            var startDate = LocalDate.now().minusMonths(1);
            var endDate = LocalDate.now();

            // Act
            var dto = mapper.toTotalPaidDTO(result, startDate, endDate);

            // Assert
            assertEquals(BigDecimal.ONE, dto.totalAmount());
            assertEquals(startDate, dto.startDate());
            assertEquals(endDate, dto.endDate());
        }

        @ParameterizedTest
        @MethodSource("totalPaidProvider")
        @DisplayName("Should map different total paid values to DTO")
        void shouldMapDifferentTotalPaidValuesToDto(BigDecimal amount) {
            // Arrange
            var result = new BillResult.TotalPaid(Amount.of(amount));
            var startDate = LocalDate.now().minusMonths(1);
            var endDate = LocalDate.now();

            // Act
            var dto = mapper.toTotalPaidDTO(result, startDate, endDate);

            // Assert
            assertEquals(amount, dto.totalAmount());
        }

        private static Stream<Arguments> totalPaidProvider() {
            return Stream.of(
                    Arguments.of(new BigDecimal("100.00")),
                    Arguments.of(new BigDecimal("999999.99")),
                    Arguments.of(new BigDecimal("0.01"))
            );
        }
    }

    private Bill createUnpaidBill() {
        return Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Test Bill")
        );
    }

    private Bill createPaidBill() {
        var bill = createUnpaidBill();
        bill.pay(PaymentDate.of(LocalDate.now()));
        return bill;
    }

    private Bill createCancelledBill() {
        var bill = createUnpaidBill();
        bill.cancel();
        return bill;
    }
}