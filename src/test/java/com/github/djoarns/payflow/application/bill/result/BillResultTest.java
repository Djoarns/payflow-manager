package com.github.djoarns.payflow.application.bill.result;

import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.valueobject.Amount;
import com.github.djoarns.payflow.domain.bill.valueobject.Description;
import com.github.djoarns.payflow.domain.bill.valueobject.DueDate;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BillResult")
class BillResultTest extends BaseUnitTest {

    @Nested
    @DisplayName("Single")
    class SingleTest {
        @Test
        @DisplayName("Should create single result with valid bill")
        void shouldCreateSingleResultWithValidBill() {
            // Arrange
            Bill bill = createTestBill();

            // Act
            var result = new BillResult.Single(bill);

            // Assert
            assertNotNull(result);
            assertEquals(bill, result.bill());
        }

        @Test
        @DisplayName("Should allow null bill")
        void shouldAllowNullBill() {
            // Act
            var result = new BillResult.Single(null);

            // Assert
            assertNull(result.bill());
        }

        @Test
        @DisplayName("Should implement value record equality")
        void shouldImplementValueRecordEquality() {
            // Arrange
            Bill bill = createTestBill();
            var result1 = new BillResult.Single(bill);
            var result2 = new BillResult.Single(bill);
            var result3 = new BillResult.Single(createTestBill());

            // Assert
            assertEquals(result1, result2);
            assertNotEquals(result1, result3);
            assertEquals(result1.hashCode(), result2.hashCode());
            assertNotEquals(result1.hashCode(), result3.hashCode());
        }
    }

    @Nested
    @DisplayName("List")
    class ListTest {
        @Test
        @DisplayName("Should create list result with valid data")
        void shouldCreateListResultWithValidData() {
            // Arrange
            List<Bill> bills = List.of(createTestBill(), createTestBill());
            long totalElements = 2;
            int currentPage = 0;
            int pageSize = 10;

            // Act
            var result = new BillResult.List(bills, totalElements, currentPage, pageSize);

            // Assert
            assertEquals(bills, result.bills());
            assertEquals(totalElements, result.totalElements());
            assertEquals(currentPage, result.currentPage());
            assertEquals(pageSize, result.pageSize());
        }

        @Test
        @DisplayName("Should allow empty bills list")
        void shouldAllowEmptyBillsList() {
            // Act
            var result = new BillResult.List(Collections.emptyList(), 0, 0, 10);

            // Assert
            assertTrue(result.bills().isEmpty());
            assertEquals(0, result.totalElements());
        }

        @ParameterizedTest
        @MethodSource("paginationTestCases")
        @DisplayName("Should calculate pagination correctly")
        void shouldCalculatePaginationCorrectly(
                long totalElements,
                int currentPage,
                int pageSize,
                long expectedTotalPages,
                boolean expectedHasNext,
                boolean expectedHasPrevious
        ) {
            // Arrange
            var result = new BillResult.List(new ArrayList<>(), totalElements, currentPage, pageSize);

            // Assert
            assertEquals(expectedTotalPages, result.getTotalPages());
            assertEquals(expectedHasNext, result.hasNext());
            assertEquals(expectedHasPrevious, result.hasPrevious());
        }

        private static Stream<Arguments> paginationTestCases() {
            return Stream.of(
                    // totalElements, currentPage, pageSize, expectedTotalPages, expectedHasNext, expectedHasPrevious
                    Arguments.of(0, 0, 10, 0, false, false),
                    Arguments.of(10, 0, 10, 1, false, false),
                    Arguments.of(20, 0, 10, 2, true, false),
                    Arguments.of(25, 1, 10, 3, true, true),
                    Arguments.of(25, 2, 10, 3, false, true)
            );
        }

        @Test
        @DisplayName("Should handle edge cases")
        void shouldHandleEdgeCases() {
            // Arrange & Act
            var result = new BillResult.List(
                    Collections.emptyList(),
                    Long.MAX_VALUE,
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE
            );

            // Assert
            assertNotNull(result);
            assertTrue(result.bills().isEmpty());
            assertEquals(Long.MAX_VALUE, result.totalElements());
            assertEquals(Integer.MAX_VALUE, result.currentPage());
            assertEquals(Integer.MAX_VALUE, result.pageSize());
        }
    }

    @Nested
    @DisplayName("TotalPaid")
    class TotalPaidTest {
        @Test
        @DisplayName("Should create total paid result with valid amount")
        void shouldCreateTotalPaidResultWithValidAmount() {
            // Arrange
            Amount amount = Amount.of(new BigDecimal("100.00"));

            // Act
            var result = new BillResult.TotalPaid(amount);

            // Assert
            assertEquals(amount, result.totalPaid());
        }

        @Test
        @DisplayName("Should allow null amount")
        void shouldAllowNullAmount() {
            // Act
            var result = new BillResult.TotalPaid(null);

            // Assert
            assertNull(result.totalPaid());
        }

        @Test
        @DisplayName("Should implement value record equality")
        void shouldImplementValueRecordEquality() {
            // Arrange
            var amount1 = Amount.of(new BigDecimal("100.00"));
            var amount2 = Amount.of(new BigDecimal("100.00"));
            var amount3 = Amount.of(new BigDecimal("200.00"));

            var result1 = new BillResult.TotalPaid(amount1);
            var result2 = new BillResult.TotalPaid(amount2);
            var result3 = new BillResult.TotalPaid(amount3);

            // Assert
            assertEquals(result1, result2);
            assertNotEquals(result1, result3);
            assertEquals(result1.hashCode(), result2.hashCode());
            assertNotEquals(result1.hashCode(), result3.hashCode());
        }
    }

    private Bill createTestBill() {
        return Bill.create(
                DueDate.of(LocalDate.now().plusDays(7)),
                Amount.of(new BigDecimal("100.00")),
                Description.of("Test Bill")
        );
    }
}