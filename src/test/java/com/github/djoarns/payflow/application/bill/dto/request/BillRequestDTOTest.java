package com.github.djoarns.payflow.application.bill.dto.request;

import com.github.djoarns.payflow.domain.bill.valueobject.Status;
import com.github.djoarns.payflow.util.BaseUnitTest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BillRequestDTOTest extends BaseUnitTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Create Tests")
    class CreateTests {
        @Test
        @DisplayName("Should create DTO with valid data")
        void shouldCreateDTOWithValidData() {
            // Arrange
            LocalDate dueDate = LocalDate.now().plusDays(7);
            BigDecimal amount = new BigDecimal("100.00");
            String description = "Test Bill";

            // Act
            var dto = new BillRequestDTO.Create(dueDate, amount, description);

            // Assert
            var violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
            assertEquals(dueDate, dto.dueDate());
            assertEquals(amount, dto.amount());
            assertEquals(description, dto.description());
        }

        @Test
        @DisplayName("Should fail validation when due date is null")
        void shouldFailValidationWhenDueDateNull() {
            // Arrange
            var dto = new BillRequestDTO.Create(
                    null,
                    new BigDecimal("100.00"),
                    "Test Bill"
            );

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Due date is required", violations.iterator().next().getMessage());
        }

        @Test
        @DisplayName("Should fail validation when amount is null")
        void shouldFailValidationWhenAmountNull() {
            // Arrange
            var dto = new BillRequestDTO.Create(
                    LocalDate.now().plusDays(7),
                    null,
                    "Test Bill"
            );

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Amount is required", violations.iterator().next().getMessage());
        }

        @Test
        @DisplayName("Should fail validation when amount is zero or negative")
        void shouldFailValidationWhenAmountZeroOrNegative() {
            // Arrange
            var dto1 = new BillRequestDTO.Create(
                    LocalDate.now().plusDays(7),
                    BigDecimal.ZERO,
                    "Test Bill"
            );
            var dto2 = new BillRequestDTO.Create(
                    LocalDate.now().plusDays(7),
                    new BigDecimal("-1.00"),
                    "Test Bill"
            );

            // Act & Assert
            var violations1 = validator.validate(dto1);
            var violations2 = validator.validate(dto2);

            assertEquals(1, violations1.size());
            assertEquals(1, violations2.size());
            assertEquals("Amount must be greater than zero", violations1.iterator().next().getMessage());
            assertEquals("Amount must be greater than zero", violations2.iterator().next().getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "   "})
        @DisplayName("Should fail validation when description is blank")
        void shouldFailValidationWhenDescriptionBlank(String description) {
            // Arrange
            var dto = new BillRequestDTO.Create(
                    LocalDate.now().plusDays(7),
                    new BigDecimal("100.00"),
                    description
            );

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Description is required", violations.iterator().next().getMessage());
        }

        @Test
        @DisplayName("Should fail validation when description exceeds max length")
        void shouldFailValidationWhenDescriptionTooLong() {
            // Arrange
            String longDescription = "a".repeat(256);
            var dto = new BillRequestDTO.Create(
                    LocalDate.now().plusDays(7),
                    new BigDecimal("100.00"),
                    longDescription
            );

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Description cannot exceed 255 characters", violations.iterator().next().getMessage());
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {
        @Test
        @DisplayName("Should create update DTO with valid data")
        void shouldCreateUpdateDTOWithValidData() {
            // Arrange
            LocalDate dueDate = LocalDate.now().plusDays(7);
            BigDecimal amount = new BigDecimal("100.00");
            String description = "Test Bill";

            // Act
            var dto = new BillRequestDTO.Update(dueDate, amount, description);

            // Assert
            var violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
            assertEquals(dueDate, dto.dueDate());
            assertEquals(amount, dto.amount());
            assertEquals(description, dto.description());
        }

        // Similar validation tests as Create...
    }

    @Nested
    @DisplayName("Pay Tests")
    class PayTests {
        @Test
        @DisplayName("Should create pay DTO with valid data")
        void shouldCreatePayDTOWithValidData() {
            // Arrange
            LocalDate paymentDate = LocalDate.now();

            // Act
            var dto = new BillRequestDTO.Pay(paymentDate);

            // Assert
            var violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
            assertEquals(paymentDate, dto.paymentDate());
        }

        @Test
        @DisplayName("Should fail validation when payment date is null")
        void shouldFailValidationWhenPaymentDateNull() {
            // Arrange
            var dto = new BillRequestDTO.Pay(null);

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Payment date is required", violations.iterator().next().getMessage());
        }

        @Test
        @DisplayName("Should fail validation when payment date is in future")
        void shouldFailValidationWhenPaymentDateInFuture() {
            // Arrange
            var dto = new BillRequestDTO.Pay(LocalDate.now().plusDays(1));

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("Payment date cannot be in the future", violations.iterator().next().getMessage());
        }
    }

    @Nested
    @DisplayName("Search Tests")
    class SearchTests {
        @Test
        @DisplayName("Should create search DTO with valid data")
        void shouldCreateSearchDTOWithValidData() {
            // Arrange
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusMonths(1);
            String description = "Test";
            int page = 0;
            int size = 10;

            // Act
            var dto = new BillRequestDTO.Search(startDate, endDate, description, page, size);

            // Assert
            var violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
            assertEquals(startDate, dto.startDate());
            assertEquals(endDate, dto.endDate());
            assertEquals(description, dto.description());
            assertEquals(page, dto.page());
            assertEquals(size, dto.size());
        }

        @ParameterizedTest
        @MethodSource("invalidPaginationProvider")
        @DisplayName("Should fail validation with invalid pagination values")
        void shouldFailValidationWithInvalidPaginationValues(int page, int size, String expectedMessage) {
            // Arrange
            var dto = new BillRequestDTO.Search(
                    LocalDate.now(),
                    LocalDate.now().plusMonths(1),
                    "Test",
                    page,
                    size
            );

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals(expectedMessage, violations.iterator().next().getMessage());
        }

        private static Stream<Arguments> invalidPaginationProvider() {
            return Stream.of(
                    Arguments.of(-1, 10, "Page number cannot be negative"),
                    Arguments.of(0, 0, "Page size must be greater than zero"),
                    Arguments.of(0, -1, "Page size must be greater than zero"),
                    Arguments.of(0, 101, "Page size cannot exceed 100")
            );
        }
    }

    @Nested
    @DisplayName("CalculateTotal Tests")
    class CalculateTotalTests {
        @Test
        @DisplayName("Should create calculate total DTO with valid data")
        void shouldCreateCalculateTotalDTOWithValidData() {
            // Arrange
            LocalDate startDate = LocalDate.now().minusMonths(1);
            LocalDate endDate = LocalDate.now();

            // Act
            var dto = new BillRequestDTO.CalculateTotal(startDate, endDate);

            // Assert
            var violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
            assertEquals(startDate, dto.startDate());
            assertEquals(endDate, dto.endDate());
        }

        @Test
        @DisplayName("Should fail validation when dates are null")
        void shouldFailValidationWhenDatesNull() {
            // Arrange
            var dto1 = new BillRequestDTO.CalculateTotal(null, LocalDate.now());
            var dto2 = new BillRequestDTO.CalculateTotal(LocalDate.now(), null);

            // Act
            var violations1 = validator.validate(dto1);
            var violations2 = validator.validate(dto2);

            // Assert
            assertEquals(1, violations1.size());
            assertEquals(1, violations2.size());
            assertEquals("Start date is required", violations1.iterator().next().getMessage());
            assertEquals("End date is required", violations2.iterator().next().getMessage());
        }
    }

    @Nested
    @DisplayName("ChangeStatus Tests")
    class ChangeStatusTests {
        @Test
        @DisplayName("Should create change status DTO with valid data")
        void shouldCreateChangeStatusDTOWithValidData() {
            // Arrange & Act
            var dto = new BillRequestDTO.ChangeStatus(Status.PAID);

            // Assert
            var violations = validator.validate(dto);
            assertTrue(violations.isEmpty());
            assertEquals(Status.PAID, dto.newStatus());
        }

        @Test
        @DisplayName("Should fail validation when status is null")
        void shouldFailValidationWhenStatusNull() {
            // Arrange
            var dto = new BillRequestDTO.ChangeStatus(null);

            // Act
            var violations = validator.validate(dto);

            // Assert
            assertEquals(1, violations.size());
            assertEquals("New status is required", violations.iterator().next().getMessage());
        }
    }
}