package com.github.djoarns.payflow.application.bill.usecase;

import com.github.djoarns.payflow.application.bill.command.BillCommand;
import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.domain.bill.BillRepository;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.valueobject.Status;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateBillUseCase")
class CreateBillUseCaseTest extends BaseUnitTest {

    @Mock
    private BillRepository billRepository;

    private CreateBillUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateBillUseCase(billRepository);
    }

    @Nested
    @DisplayName("When creating a bill")
    class CreateBill {

        @Test
        @DisplayName("Should create valid bill with minimum values")
        void shouldCreateValidBillWithMinimumValues() {
            // Arrange
            var command = new BillCommand.Create(
                    LocalDate.now(),
                    BigDecimal.ONE,
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
        @DisplayName("Should create valid bill with maximum values")
        void shouldCreateValidBillWithMaximumValues() {
            // Arrange
            var command = new BillCommand.Create(
                    LocalDate.now().plusYears(10),
                    new BigDecimal("999999999.99"),
                    "A".repeat(255)
            );

            when(billRepository.save(any(Bill.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            var result = useCase.execute(command);

            // Assert
            assertNotNull(result);
            var billCaptor = ArgumentCaptor.forClass(Bill.class);
            verify(billRepository).save(billCaptor.capture());

            var capturedBill = billCaptor.getValue();
            assertEquals(command.dueDate(), capturedBill.getDueDate().getValue());
            assertEquals(command.amount(), capturedBill.getAmount().getValue());
            assertEquals(command.description(), capturedBill.getDescription().getValue());
        }

        @Test
        @DisplayName("Should handle repository failure")
        void shouldHandleRepositoryFailure() {
            // Arrange
            var command = new BillCommand.Create(
                    LocalDate.now(),
                    BigDecimal.TEN,
                    "Test Bill"
            );

            when(billRepository.save(any(Bill.class)))
                    .thenThrow(new RuntimeException("Database error"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> useCase.execute(command));
            verify(billRepository).save(any(Bill.class));
        }
    }

    @Nested
    @DisplayName("When validating amount")
    class ValidateAmount {

        @ParameterizedTest(name = "Amount {0} should throw InvalidBillDataException")
        @MethodSource("invalidAmounts")
        @DisplayName("Should throw exception for invalid amounts")
        void shouldThrowExceptionForInvalidAmounts(BigDecimal amount, String expectedMessage) {
            // Arrange
            var command = new BillCommand.Create(
                    LocalDate.now().plusDays(7),
                    amount,
                    "Test Bill"
            );

            // Act & Assert
            var exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> useCase.execute(command)
            );
            assertEquals(expectedMessage, exception.getMessage());
            verify(billRepository, never()).save(any());
        }

        private static Stream<Arguments> invalidAmounts() {
            return Stream.of(
                    Arguments.of(BigDecimal.ZERO, "Amount must be greater than zero"),
                    Arguments.of(new BigDecimal("-1"), "Amount must be greater than zero"),
                    Arguments.of(new BigDecimal("1000000000.00"), "Amount cannot be greater than 999999999.99")
            );
        }
    }

    @Nested
    @DisplayName("When validating due date")
    class ValidateDueDate {

        @Test
        @DisplayName("Should throw exception when due date is null")
        void shouldThrowExceptionWhenDueDateNull() {
            // Arrange
            var command = new BillCommand.Create(
                    null,
                    BigDecimal.TEN,
                    "Test Bill"
            );

            // Act & Assert
            var exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> useCase.execute(command)
            );
            assertEquals("Due date cannot be null", exception.getMessage());
            verify(billRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when due date is in the past")
        void shouldThrowExceptionWhenDueDateInPast() {
            // Arrange
            var command = new BillCommand.Create(
                    LocalDate.now().minusDays(1),
                    BigDecimal.TEN,
                    "Test Bill"
            );

            // Act & Assert
            var exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> useCase.execute(command)
            );
            assertEquals("Due date cannot be in the past", exception.getMessage());
            verify(billRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when due date is too far in future")
        void shouldThrowExceptionWhenDueDateTooFarInFuture() {
            // Arrange
            var command = new BillCommand.Create(
                    LocalDate.now().plusYears(11),
                    BigDecimal.TEN,
                    "Test Bill"
            );

            // Act & Assert
            var exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> useCase.execute(command)
            );
            assertEquals("Due date cannot be more than 10 years in the future", exception.getMessage());
            verify(billRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("When validating description")
    class ValidateDescription {

        @ParameterizedTest(name = "Description \"{0}\" should throw InvalidBillDataException")
        @MethodSource("invalidDescriptions")
        @DisplayName("Should throw exception for invalid descriptions")
        void shouldThrowExceptionForInvalidDescriptions(String description, String expectedMessage) {
            // Arrange
            var command = new BillCommand.Create(
                    LocalDate.now().plusDays(7),
                    BigDecimal.TEN,
                    description
            );

            // Act & Assert
            var exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> useCase.execute(command)
            );
            assertEquals(expectedMessage, exception.getMessage());
            verify(billRepository, never()).save(any());
        }

        private static Stream<Arguments> invalidDescriptions() {
            return Stream.of(
                    Arguments.of(null, "Description cannot be empty"),
                    Arguments.of("", "Description cannot be empty"),
                    Arguments.of(" ", "Description cannot be empty"),
                    Arguments.of("A".repeat(256), "Description cannot be longer than 255 characters")
            );
        }
    }
}