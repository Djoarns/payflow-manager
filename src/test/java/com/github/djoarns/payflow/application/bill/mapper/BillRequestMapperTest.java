package com.github.djoarns.payflow.application.bill.mapper;

import com.github.djoarns.payflow.application.bill.dto.request.BillRequestDTO;
import com.github.djoarns.payflow.domain.bill.valueobject.Status;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BillRequestMapperTest extends BaseUnitTest {

    private BillRequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BillRequestMapper();
    }

    @Nested
    @DisplayName("toCreateCommand")
    class ToCreateCommand {
        @Test
        @DisplayName("Should map create DTO to command")
        void shouldMapCreateDtoToCommand() {
            // Arrange
            var dto = new BillRequestDTO.Create(
                    LocalDate.now().plusDays(7),
                    new BigDecimal("100.00"),
                    "Test Bill"
            );

            // Act
            var command = mapper.toCreateCommand(dto);

            // Assert
            assertEquals(dto.dueDate(), command.dueDate());
            assertEquals(dto.amount(), command.amount());
            assertEquals(dto.description(), command.description());
        }

        @Test
        @DisplayName("Should handle minimum valid values")
        void shouldHandleMinimumValidValues() {
            // Arrange
            var dto = new BillRequestDTO.Create(
                    LocalDate.now(),
                    BigDecimal.ONE,
                    "x"
            );

            // Act
            var command = mapper.toCreateCommand(dto);

            // Assert
            assertEquals(dto.dueDate(), command.dueDate());
            assertEquals(dto.amount(), command.amount());
            assertEquals(dto.description(), command.description());
        }

        @Test
        @DisplayName("Should handle maximum valid values")
        void shouldHandleMaximumValidValues() {
            // Arrange
            var dto = new BillRequestDTO.Create(
                    LocalDate.now().plusYears(100),
                    new BigDecimal("999999999.99"),
                    "x".repeat(255)
            );

            // Act
            var command = mapper.toCreateCommand(dto);

            // Assert
            assertEquals(dto.dueDate(), command.dueDate());
            assertEquals(dto.amount(), command.amount());
            assertEquals(dto.description(), command.description());
        }
    }

    @Nested
    @DisplayName("toUpdateCommand")
    class ToUpdateCommand {
        @Test
        @DisplayName("Should map update DTO to command")
        void shouldMapUpdateDtoToCommand() {
            // Arrange
            var id = 1L;
            var dto = new BillRequestDTO.Update(
                    LocalDate.now().plusDays(7),
                    new BigDecimal("100.00"),
                    "Test Bill"
            );

            // Act
            var command = mapper.toUpdateCommand(id, dto);

            // Assert
            assertEquals(id, command.id());
            assertEquals(dto.dueDate(), command.dueDate());
            assertEquals(dto.amount(), command.amount());
            assertEquals(dto.description(), command.description());
        }

        @Test
        @DisplayName("Should handle different id values")
        void shouldHandleDifferentIdValues() {
            // Arrange
            var dto = new BillRequestDTO.Update(
                    LocalDate.now(),
                    BigDecimal.ONE,
                    "Test"
            );

            // Act & Assert
            Stream.of(1L, Long.MAX_VALUE).forEach(id -> {
                var command = mapper.toUpdateCommand(id, dto);
                assertEquals(id, command.id());
            });
        }
    }

    @Nested
    @DisplayName("toPayCommand")
    class ToPayCommand {
        @Test
        @DisplayName("Should map pay DTO to command")
        void shouldMapPayDtoToCommand() {
            // Arrange
            var id = 1L;
            var dto = new BillRequestDTO.Pay(LocalDate.now());

            // Act
            var command = mapper.toPayCommand(id, dto);

            // Assert
            assertEquals(id, command.id());
            assertEquals(dto.paymentDate(), command.paymentDate());
        }

        @ParameterizedTest
        @MethodSource("paymentDateProvider")
        @DisplayName("Should handle different payment dates")
        void shouldHandleDifferentPaymentDates(LocalDate paymentDate) {
            // Arrange
            var id = 1L;
            var dto = new BillRequestDTO.Pay(paymentDate);

            // Act
            var command = mapper.toPayCommand(id, dto);

            // Assert
            assertEquals(paymentDate, command.paymentDate());
        }

        private static Stream<Arguments> paymentDateProvider() {
            return Stream.of(
                    Arguments.of(LocalDate.now()),
                    Arguments.of(LocalDate.now().minusDays(1)),
                    Arguments.of(LocalDate.now().minusYears(1))
            );
        }
    }

    @Nested
    @DisplayName("toListCommand")
    class ToListCommand {
        @Test
        @DisplayName("Should map search DTO to list command")
        void shouldMapSearchDtoToListCommand() {
            // Arrange
            var dto = new BillRequestDTO.Search(
                    LocalDate.now(),
                    LocalDate.now().plusMonths(1),
                    "Test",
                    0,
                    10
            );

            // Act
            var command = mapper.toListCommand(dto);

            // Assert
            assertEquals(dto.startDate(), command.startDate());
            assertEquals(dto.endDate(), command.endDate());
            assertEquals(dto.description(), command.description());
            assertEquals(dto.page(), command.page());
            assertEquals(dto.size(), command.size());
        }

        @Test
        @DisplayName("Should handle null description")
        void shouldHandleNullDescription() {
            // Arrange
            var dto = new BillRequestDTO.Search(
                    LocalDate.now(),
                    LocalDate.now().plusMonths(1),
                    null,
                    0,
                    10
            );

            // Act
            var command = mapper.toListCommand(dto);

            // Assert
            assertNull(command.description());
        }

        @ParameterizedTest
        @MethodSource("paginationProvider")
        @DisplayName("Should handle different pagination values")
        void shouldHandleDifferentPaginationValues(int page, int size) {
            // Arrange
            var dto = new BillRequestDTO.Search(
                    LocalDate.now(),
                    LocalDate.now().plusMonths(1),
                    "Test",
                    page,
                    size
            );

            // Act
            var command = mapper.toListCommand(dto);

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
    @DisplayName("toCalculateTotalCommand")
    class ToCalculateTotalCommand {
        @Test
        @DisplayName("Should map calculate total DTO to command")
        void shouldMapCalculateTotalDtoToCommand() {
            // Arrange
            var dto = new BillRequestDTO.CalculateTotal(
                    LocalDate.now().minusMonths(1),
                    LocalDate.now()
            );

            // Act
            var command = mapper.toCalculateTotalCommand(dto);

            // Assert
            assertEquals(dto.startDate(), command.startDate());
            assertEquals(dto.endDate(), command.endDate());
        }

        @Test
        @DisplayName("Should handle same start and end date")
        void shouldHandleSameStartAndEndDate() {
            // Arrange
            var date = LocalDate.now();
            var dto = new BillRequestDTO.CalculateTotal(date, date);

            // Act
            var command = mapper.toCalculateTotalCommand(dto);

            // Assert
            assertEquals(date, command.startDate());
            assertEquals(date, command.endDate());
        }
    }

    @Nested
    @DisplayName("toChangeStatusCommand")
    class ToChangeStatusCommand {
        @Test
        @DisplayName("Should map change status DTO to command")
        void shouldMapChangeStatusDtoToCommand() {
            // Arrange
            var id = 1L;
            var dto = new BillRequestDTO.ChangeStatus(Status.CANCELLED);

            // Act
            var command = mapper.toChangeStatusCommand(id, dto);

            // Assert
            assertEquals(id, command.id());
            assertEquals(dto.newStatus(), command.newStatus());
        }

        @Test
        @DisplayName("Should handle all status values")
        void shouldHandleAllStatusValues() {
            // Arrange
            var id = 1L;

            // Act & Assert
            for (Status status : Status.values()) {
                var dto = new BillRequestDTO.ChangeStatus(status);
                var command = mapper.toChangeStatusCommand(id, dto);
                assertEquals(status, command.newStatus());
            }
        }
    }
}