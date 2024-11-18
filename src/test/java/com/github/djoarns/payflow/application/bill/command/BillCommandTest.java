package com.github.djoarns.payflow.application.bill.command;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BillCommandTest extends BaseUnitTest {

    @Nested
    @DisplayName("Create Command")
    class CreateCommand {
        @Test
        @DisplayName("Should create command with valid data")
        void shouldCreateCommandWithValidData() {
            // Arrange
            LocalDate dueDate = LocalDate.now().plusDays(7);
            BigDecimal amount = new BigDecimal("100.00");
            String description = "Test Bill";

            // Act
            var command = new BillCommand.Create(dueDate, amount, description);

            // Assert
            assertNotNull(command);
            assertEquals(dueDate, command.dueDate());
            assertEquals(amount, command.amount());
            assertEquals(description, command.description());
        }

        @Test
        @DisplayName("Should allow null values in creation")
        void shouldAllowNullValues() {
            // Act
            var command = new BillCommand.Create(null, null, null);

            // Assert
            assertNotNull(command);
            assertNull(command.dueDate());
            assertNull(command.amount());
            assertNull(command.description());
        }
    }

    @Nested
    @DisplayName("Update Command")
    class UpdateCommand {
        @Test
        @DisplayName("Should create update command with valid data")
        void shouldCreateUpdateCommandWithValidData() {
            // Arrange
            Long id = 1L;
            LocalDate dueDate = LocalDate.now().plusDays(7);
            BigDecimal amount = new BigDecimal("100.00");
            String description = "Test Bill";

            // Act
            var command = new BillCommand.Update(id, dueDate, amount, description);

            // Assert
            assertNotNull(command);
            assertEquals(id, command.id());
            assertEquals(dueDate, command.dueDate());
            assertEquals(amount, command.amount());
            assertEquals(description, command.description());
        }

        @Test
        @DisplayName("Should only require id for update command")
        void shouldOnlyRequireId() {
            // Act
            var command = new BillCommand.Update(1L, null, null, null);

            // Assert
            assertNotNull(command);
            assertNotNull(command.id());
            assertNull(command.dueDate());
            assertNull(command.amount());
            assertNull(command.description());
        }
    }

    @Nested
    @DisplayName("Pay Command")
    class PayCommand {
        @Test
        @DisplayName("Should create pay command with valid data")
        void shouldCreatePayCommandWithValidData() {
            // Arrange
            Long id = 1L;
            LocalDate paymentDate = LocalDate.now();

            // Act
            var command = new BillCommand.Pay(id, paymentDate);

            // Assert
            assertNotNull(command);
            assertEquals(id, command.id());
            assertEquals(paymentDate, command.paymentDate());
        }

        @Test
        @DisplayName("Should allow null payment date")
        void shouldAllowNullPaymentDate() {
            // Act
            var command = new BillCommand.Pay(1L, null);

            // Assert
            assertNotNull(command);
            assertNotNull(command.id());
            assertNull(command.paymentDate());
        }
    }

    @Nested
    @DisplayName("Find Command")
    class FindCommand {
        @Test
        @DisplayName("Should create find command with valid id")
        void shouldCreateFindCommandWithValidId() {
            // Arrange
            Long id = 1L;

            // Act
            var command = new BillCommand.Find(id);

            // Assert
            assertNotNull(command);
            assertEquals(id, command.id());
        }
    }

    @Nested
    @DisplayName("List Command")
    class ListCommand {
        @Test
        @DisplayName("Should create list command with all parameters")
        void shouldCreateListCommandWithAllParameters() {
            // Arrange
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusMonths(1);
            String description = "Test";
            int page = 0;
            int size = 10;

            // Act
            var command = new BillCommand.List(startDate, endDate, description, page, size);

            // Assert
            assertNotNull(command);
            assertEquals(startDate, command.startDate());
            assertEquals(endDate, command.endDate());
            assertEquals(description, command.description());
            assertEquals(page, command.page());
            assertEquals(size, command.size());
        }

        @Test
        @DisplayName("Should allow null dates and description")
        void shouldAllowNullDatesAndDescription() {
            // Act
            var command = new BillCommand.List(null, null, null, 0, 10);

            // Assert
            assertNotNull(command);
            assertNull(command.startDate());
            assertNull(command.endDate());
            assertNull(command.description());
            assertEquals(0, command.page());
            assertEquals(10, command.size());
        }

        @ParameterizedTest
        @MethodSource("paginationProvider")
        @DisplayName("Should handle different pagination values")
        void shouldHandleDifferentPaginationValues(int page, int size) {
            // Act
            var command = new BillCommand.List(
                    LocalDate.now(),
                    LocalDate.now().plusMonths(1),
                    "Test",
                    page,
                    size
            );

            // Assert
            assertEquals(page, command.page());
            assertEquals(size, command.size());
        }

        private static Stream<Arguments> paginationProvider() {
            return Stream.of(
                    Arguments.of(0, 10),
                    Arguments.of(1, 20),
                    Arguments.of(100, 50)
            );
        }
    }

    @Nested
    @DisplayName("Calculate Total Paid Command")
    class CalculateTotalPaidCommand {
        @Test
        @DisplayName("Should create calculate total paid command with valid dates")
        void shouldCreateCalculateTotalPaidCommandWithValidDates() {
            // Arrange
            LocalDate startDate = LocalDate.now().minusMonths(1);
            LocalDate endDate = LocalDate.now();

            // Act
            var command = new BillCommand.CalculateTotalPaid(startDate, endDate);

            // Assert
            assertNotNull(command);
            assertEquals(startDate, command.startDate());
            assertEquals(endDate, command.endDate());
        }

        @Test
        @DisplayName("Should allow null dates")
        void shouldAllowNullDates() {
            // Act
            var command = new BillCommand.CalculateTotalPaid(null, null);

            // Assert
            assertNotNull(command);
            assertNull(command.startDate());
            assertNull(command.endDate());
        }
    }
}