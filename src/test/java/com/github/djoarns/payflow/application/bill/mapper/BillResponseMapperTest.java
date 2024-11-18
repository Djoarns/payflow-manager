package com.github.djoarns.payflow.application.bill.mapper;

import com.github.djoarns.payflow.application.bill.result.BillImportResult;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

        @Test
        @DisplayName("Should map overdue bill to response DTO")
        void shouldMapOverdueBillToResponseDto() {
            // Arrange
            var bill = createTestBill();
            bill.update(
                    DueDate.of(LocalDate.now().minusDays(1)),
                    bill.getAmount(),
                    bill.getDescription()
            );

            // Act
            var dto = mapper.toResponseDTO(bill);

            // Assert
            assertNotNull(dto);
            assertTrue(dto.dueDate().isBefore(LocalDate.now()));
            assertEquals(Status.PENDING.name(), dto.status());
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
            var bills = List.of(createTestBill());
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
            var bills = List.of(createTestBill(), createTestBill());
            var result = new BillResult.List(bills, 30, 1, 10);

            // Act
            var dto = mapper.toPageDTO(result);

            // Assert
            assertEquals(2, dto.content().size());
            assertEquals(30, dto.totalElements());
            assertEquals(3, dto.totalPages());
            assertTrue(dto.hasNext());
            assertTrue(dto.hasPrevious());
        }

        @Test
        @DisplayName("Should handle partial last page")
        void shouldHandlePartialLastPage() {
            // Arrange
            var bills = IntStream.range(0, 5)
                    .mapToObj(i -> createTestBill())
                    .collect(Collectors.toList());
            var result = new BillResult.List(bills, 25, 2, 10);

            // Act
            var dto = mapper.toPageDTO(result);

            // Assert
            assertEquals(5, dto.content().size());
            assertEquals(25, dto.totalElements());
            assertEquals(3, dto.totalPages());
            assertFalse(dto.hasNext());
            assertTrue(dto.hasPrevious());
        }

        @Test
        @DisplayName("Should handle exact page size")
        void shouldHandleExactPageSize() {
            // Arrange
            var bills = IntStream.range(0, 10)
                    .mapToObj(i -> createTestBill())
                    .collect(Collectors.toList());
            var result = new BillResult.List(bills, 20, 0, 10);

            // Act
            var dto = mapper.toPageDTO(result);

            // Assert
            assertEquals(10, dto.content().size());
            assertEquals(20, dto.totalElements());
            assertEquals(2, dto.totalPages());
            assertTrue(dto.hasNext());
            assertFalse(dto.hasPrevious());
        }

        @Test
        @DisplayName("Should handle single item on last page")
        void shouldHandleSingleItemOnLastPage() {
            // Arrange
            var bills = List.of(createTestBill());
            var result = new BillResult.List(bills, 21, 2, 10);

            // Act
            var dto = mapper.toPageDTO(result);

            // Assert
            assertEquals(1, dto.content().size());
            assertEquals(21, dto.totalElements());
            assertEquals(3, dto.totalPages());
            assertFalse(dto.hasNext());
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

        @Test
        @DisplayName("Should handle same start and end date")
        void shouldHandleSameStartAndEndDate() {
            // Arrange
            var amount = new BigDecimal("100.00");
            var result = new BillResult.TotalPaid(Amount.of(amount));
            var date = LocalDate.now();

            // Act
            var dto = mapper.toTotalPaidDTO(result, date, date);

            // Assert
            assertEquals(amount, dto.totalAmount());
            assertEquals(date, dto.startDate());
            assertEquals(date, dto.endDate());
        }

        @Test
        @DisplayName("Should handle year-long period")
        void shouldHandleYearLongPeriod() {
            // Arrange
            var amount = new BigDecimal("1000.00");
            var result = new BillResult.TotalPaid(Amount.of(amount));
            var startDate = LocalDate.now().withDayOfYear(1);
            var endDate = LocalDate.now().withDayOfYear(365);

            // Act
            var dto = mapper.toTotalPaidDTO(result, startDate, endDate);

            // Assert
            assertEquals(amount, dto.totalAmount());
            assertEquals(startDate, dto.startDate());
            assertEquals(endDate, dto.endDate());
        }

        private static Stream<Arguments> totalPaidProvider() {
            return Stream.of(
                    Arguments.of(new BigDecimal("100.00")),
                    Arguments.of(new BigDecimal("999999.99")),
                    Arguments.of(new BigDecimal("0.01")),
                    Arguments.of(new BigDecimal("1000000.00"))
            );
        }
    }

    @Nested
    @DisplayName("toImportDTO")
    class ToImportDTO {
        @Test
        @DisplayName("Should map successful import result to DTO")
        void shouldMapSuccessfulImportResultToDto() {
            // Arrange
            var result = new BillImportResult(10, 10, 0, "Import completed successfully");

            // Act
            var dto = mapper.toImportDTO(result);

            // Assert
            assertEquals(10, dto.totalProcessed());
            assertEquals(10, dto.successCount());
            assertEquals(0, dto.errorCount());
            assertEquals("Import completed successfully", dto.message());
        }

        @Test
        @DisplayName("Should map partially successful import result to DTO")
        void shouldMapPartiallySuccessfulImportResultToDto() {
            // Arrange
            var result = new BillImportResult(10, 7, 3, "Import completed with errors");

            // Act
            var dto = mapper.toImportDTO(result);

            // Assert
            assertEquals(10, dto.totalProcessed());
            assertEquals(7, dto.successCount());
            assertEquals(3, dto.errorCount());
            assertEquals("Import completed with errors", dto.message());
        }

        @Test
        @DisplayName("Should map failed import result to DTO")
        void shouldMapFailedImportResultToDto() {
            // Arrange
            var result = new BillImportResult(0, 0, 0, "Import failed: Invalid file format");

            // Act
            var dto = mapper.toImportDTO(result);

            // Assert
            assertEquals(0, dto.totalProcessed());
            assertEquals(0, dto.successCount());
            assertEquals(0, dto.errorCount());
            assertEquals("Import failed: Invalid file format", dto.message());
        }

        @Test
        @DisplayName("Should handle large import result")
        void shouldHandleLargeImportResult() {
            // Arrange
            var result = new BillImportResult(1000, 998, 2, "Large import completed");

            // Act
            var dto = mapper.toImportDTO(result);

            // Assert
            assertEquals(1000, dto.totalProcessed());
            assertEquals(998, dto.successCount());
            assertEquals(2, dto.errorCount());
            assertEquals("Large import completed", dto.message());
        }
    }

    // Helper Methods
    private Bill createTestBill() {
        return Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Test Bill")
        );
    }

    private Bill createUnpaidBill() {
        return createTestBill();
    }

    private Bill createPaidBill() {
        var bill = createTestBill();
        bill.pay(PaymentDate.of(LocalDate.now()));
        return bill;
    }

    private Bill createCancelledBill() {
        var bill = createTestBill();
        bill.cancel();
        return bill;
    }
}