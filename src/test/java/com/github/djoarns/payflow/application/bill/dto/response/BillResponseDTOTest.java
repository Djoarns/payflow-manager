package com.github.djoarns.payflow.application.bill.dto.response;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BillResponseDTOTest extends BaseUnitTest {

    @Nested
    @DisplayName("Single")
    class Single {
        @Test
        @DisplayName("Should create Single response with all fields")
        void shouldCreateSingleResponseWithAllFields() {
            // Arrange
            Long id = 1L;
            LocalDate dueDate = LocalDate.now().plusDays(7);
            LocalDate paymentDate = LocalDate.now();
            BigDecimal amount = new BigDecimal("100.00");
            String description = "Test Bill";
            String status = "PENDING";

            // Act
            var response = new BillResponseDTO.Single(
                    id,
                    dueDate,
                    paymentDate,
                    amount,
                    description,
                    status
            );

            // Assert
            assertNotNull(response);
            assertEquals(id, response.id());
            assertEquals(dueDate, response.dueDate());
            assertEquals(paymentDate, response.paymentDate());
            assertEquals(amount, response.amount());
            assertEquals(description, response.description());
            assertEquals(status, response.status());
        }

        @Test
        @DisplayName("Should handle null payment date")
        void shouldHandleNullPaymentDate() {
            // Arrange & Act
            var response = new BillResponseDTO.Single(
                    1L,
                    LocalDate.now(),
                    null,
                    BigDecimal.ONE,
                    "Test",
                    "PENDING"
            );

            // Assert
            assertNull(response.paymentDate());
        }

        @Test
        @DisplayName("Should implement value object equality")
        void shouldImplementValueObjectEquality() {
            // Arrange
            var response1 = new BillResponseDTO.Single(
                    1L,
                    LocalDate.now(),
                    null,
                    BigDecimal.ONE,
                    "Test",
                    "PENDING"
            );
            var response2 = new BillResponseDTO.Single(
                    1L,
                    LocalDate.now(),
                    null,
                    BigDecimal.ONE,
                    "Test",
                    "PENDING"
            );
            var response3 = new BillResponseDTO.Single(
                    2L,
                    LocalDate.now(),
                    null,
                    BigDecimal.ONE,
                    "Test",
                    "PENDING"
            );

            // Assert
            assertEquals(response1, response2);
            assertNotEquals(response1, response3);
            assertEquals(response1.hashCode(), response2.hashCode());
            assertNotEquals(response1.hashCode(), response3.hashCode());
        }
    }

    @Nested
    @DisplayName("Page")
    class Page {
        @Test
        @DisplayName("Should create Page response with content")
        void shouldCreatePageResponseWithContent() {
            // Arrange
            var content = Arrays.asList(
                    createSingleResponse(1L),
                    createSingleResponse(2L)
            );

            // Act
            var response = new BillResponseDTO.Page(
                    content,
                    2,
                    1,
                    0,
                    10,
                    false,
                    false
            );

            // Assert
            assertNotNull(response);
            assertEquals(2, response.content().size());
            assertEquals(2, response.totalElements());
            assertEquals(1, response.totalPages());
            assertEquals(0, response.page());
            assertEquals(10, response.size());
            assertFalse(response.hasNext());
            assertFalse(response.hasPrevious());
        }

        @Test
        @DisplayName("Should handle empty content")
        void shouldHandleEmptyContent() {
            // Act
            var response = new BillResponseDTO.Page(
                    Collections.emptyList(),
                    0,
                    0,
                    0,
                    10,
                    false,
                    false
            );

            // Assert
            assertTrue(response.content().isEmpty());
            assertEquals(0, response.totalElements());
        }

        @ParameterizedTest
        @MethodSource("paginationProvider")
        @DisplayName("Should handle different pagination scenarios")
        void shouldHandleDifferentPaginationScenarios(
                List<BillResponseDTO.Single> content,
                long totalElements,
                int totalPages,
                int page,
                int size,
                boolean hasNext,
                boolean hasPrevious
        ) {
            // Act
            var response = new BillResponseDTO.Page(
                    content,
                    totalElements,
                    totalPages,
                    page,
                    size,
                    hasNext,
                    hasPrevious
            );

            // Assert
            assertEquals(content.size(), response.content().size());
            assertEquals(totalElements, response.totalElements());
            assertEquals(totalPages, response.totalPages());
            assertEquals(page, response.page());
            assertEquals(size, response.size());
            assertEquals(hasNext, response.hasNext());
            assertEquals(hasPrevious, response.hasPrevious());
        }

        private static Stream<Arguments> paginationProvider() {
            return Stream.of(
                    // First page with next
                    Arguments.of(
                            List.of(createSingleResponse(1L)),
                            20L,
                            2,
                            0,
                            10,
                            true,
                            false
                    ),
                    // Middle page
                    Arguments.of(
                            List.of(createSingleResponse(11L)),
                            30L,
                            3,
                            1,
                            10,
                            true,
                            true
                    ),
                    // Last page
                    Arguments.of(
                            List.of(createSingleResponse(21L)),
                            21L,
                            3,
                            2,
                            10,
                            false,
                            true
                    )
            );
        }
    }

    @Nested
    @DisplayName("TotalPaid")
    class TotalPaid {
        @Test
        @DisplayName("Should create TotalPaid response with all fields")
        void shouldCreateTotalPaidResponseWithAllFields() {
            // Arrange
            var totalAmount = new BigDecimal("1000.00");
            var startDate = LocalDate.now().minusMonths(1);
            var endDate = LocalDate.now();

            // Act
            var response = new BillResponseDTO.TotalPaid(totalAmount, startDate, endDate);

            // Assert
            assertNotNull(response);
            assertEquals(totalAmount, response.totalAmount());
            assertEquals(startDate, response.startDate());
            assertEquals(endDate, response.endDate());
        }

        @Test
        @DisplayName("Should handle zero amount")
        void shouldHandleZeroAmount() {
            // Act
            var response = new BillResponseDTO.TotalPaid(
                    BigDecimal.ZERO,
                    LocalDate.now(),
                    LocalDate.now()
            );

            // Assert
            assertEquals(BigDecimal.ZERO, response.totalAmount());
        }

        @Test
        @DisplayName("Should implement value object equality")
        void shouldImplementValueObjectEquality() {
            // Arrange
            var date = LocalDate.now();
            var response1 = new BillResponseDTO.TotalPaid(
                    BigDecimal.ONE,
                    date,
                    date
            );
            var response2 = new BillResponseDTO.TotalPaid(
                    BigDecimal.ONE,
                    date,
                    date
            );
            var response3 = new BillResponseDTO.TotalPaid(
                    BigDecimal.TEN,
                    date,
                    date
            );

            // Assert
            assertEquals(response1, response2);
            assertNotEquals(response1, response3);
            assertEquals(response1.hashCode(), response2.hashCode());
            assertNotEquals(response1.hashCode(), response3.hashCode());
        }
    }

    @Nested
    @DisplayName("Import")
    class Import {
        @Test
        @DisplayName("Should create Import response with all fields")
        void shouldCreateImportResponseWithAllFields() {
            // Arrange
            int totalProcessed = 10;
            int successCount = 8;
            int errorCount = 2;
            String message = "Import completed";

            // Act
            var response = new BillResponseDTO.Import(
                    totalProcessed,
                    successCount,
                    errorCount,
                    message
            );

            // Assert
            assertNotNull(response);
            assertEquals(totalProcessed, response.totalProcessed());
            assertEquals(successCount, response.successCount());
            assertEquals(errorCount, response.errorCount());
            assertEquals(message, response.message());
        }

        @Test
        @DisplayName("Should handle zero counts")
        void shouldHandleZeroCounts() {
            // Act
            var response = new BillResponseDTO.Import(0, 0, 0, "No bills processed");

            // Assert
            assertEquals(0, response.totalProcessed());
            assertEquals(0, response.successCount());
            assertEquals(0, response.errorCount());
        }

        @Test
        @DisplayName("Should handle null message")
        void shouldHandleNullMessage() {
            // Act
            var response = new BillResponseDTO.Import(1, 1, 0, null);

            // Assert
            assertNull(response.message());
        }
    }

    // Utility method to create test responses
    private static BillResponseDTO.Single createSingleResponse(Long id) {
        return new BillResponseDTO.Single(
                id,
                LocalDate.now(),
                null,
                BigDecimal.ONE,
                "Test Bill " + id,
                "PENDING"
        );
    }
}
