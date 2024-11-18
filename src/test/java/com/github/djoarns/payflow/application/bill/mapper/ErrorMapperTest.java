package com.github.djoarns.payflow.application.bill.mapper;

import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillOperationException;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillStatusException;
import com.github.djoarns.payflow.domain.exception.BillDomainException;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ErrorMapper")
class ErrorMapperTest extends BaseUnitTest {

    private ErrorMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ErrorMapper();
    }

    @Nested
    @DisplayName("When mapping domain exceptions")
    class DomainExceptions {
        @ParameterizedTest
        @MethodSource("domainExceptionProvider")
        @DisplayName("Should map domain exceptions to error DTO with business error code")
        void shouldMapDomainExceptionsToErrorDto(BillDomainException exception) {
            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals(exception.getMessage(), dto.message());
            assertEquals("BUSINESS_ERROR", dto.code());
            assertNotNull(dto.timestamp());
            assertTrue(dto.timestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
            assertTrue(dto.timestamp().isAfter(LocalDateTime.now().minusSeconds(1)));
        }

        private static Stream<Arguments> domainExceptionProvider() {
            return Stream.of(
                    Arguments.of(new InvalidBillDataException("Invalid bill data")),
                    Arguments.of(new InvalidBillOperationException("Invalid operation")),
                    Arguments.of(new InvalidBillStatusException("Invalid status")),
                    Arguments.of(new BillDomainException("Generic domain error") {})
            );
        }

        @Test
        @DisplayName("Should map domain exception with empty message")
        void shouldMapDomainExceptionWithEmptyMessage() {
            // Arrange
            var exception = new InvalidBillDataException("");

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals("", dto.message());
            assertEquals("BUSINESS_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }

        @Test
        @DisplayName("Should map nested domain exception")
        void shouldMapNestedDomainException() {
            // Arrange
            var cause = new InvalidBillDataException("Root cause");
            var exception = new InvalidBillOperationException("Wrapper: " + cause.getMessage());

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals(exception.getMessage(), dto.message());
            assertEquals("BUSINESS_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }
    }

    @Nested
    @DisplayName("When mapping system exceptions")
    class SystemExceptions {
        @Test
        @DisplayName("Should map runtime exception to error DTO with system error code")
        void shouldMapRuntimeExceptionToErrorDto() {
            // Arrange
            var exception = new RuntimeException("System error");

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals(exception.getMessage(), dto.message());
            assertEquals("SYSTEM_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }

        @Test
        @DisplayName("Should map exception with null message to default message")
        void shouldMapExceptionWithNullMessageToDefaultMessage() {
            // Arrange
            Exception exception = new RuntimeException((String) null);

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals("An unexpected error occurred", dto.message());
            assertEquals("SYSTEM_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }

        @Test
        @DisplayName("Should map nested system exception")
        void shouldMapNestedSystemException() {
            // Arrange
            var cause = new IllegalArgumentException("Root system error");
            var exception = new RuntimeException("System wrapper", cause);

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals(exception.getMessage(), dto.message());
            assertEquals("SYSTEM_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }

        @Test
        @DisplayName("Should map IllegalArgumentException")
        void shouldMapIllegalArgumentException() {
            // Arrange
            var exception = new IllegalArgumentException("Invalid argument");

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals(exception.getMessage(), dto.message());
            assertEquals("SYSTEM_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }

        @Test
        @DisplayName("Should map NullPointerException")
        void shouldMapNullPointerException() {
            // Arrange
            var exception = new NullPointerException("Null value");

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals(exception.getMessage(), dto.message());
            assertEquals("SYSTEM_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }
    }

    @Nested
    @DisplayName("When validating timestamp")
    class TimestampValidation {
        @Test
        @DisplayName("Should ensure timestamp is current")
        void shouldEnsureTimestampIsCurrent() {
            // Arrange
            var before = LocalDateTime.now();
            var exception = new RuntimeException("Test");

            // Act
            var dto = mapper.toErrorDTO(exception);
            var after = LocalDateTime.now();

            // Assert
            assertNotNull(dto.timestamp());
            assertTrue(!dto.timestamp().isBefore(before) &&
                            !dto.timestamp().isAfter(after),
                    "Timestamp should be between test execution times");
        }

        @Test
        @DisplayName("Should create different timestamps for sequential errors")
        void shouldCreateDifferentTimestampsForSequentialErrors() {
            // Arrange
            var exception = new RuntimeException("Test");

            // Act
            var dto1 = mapper.toErrorDTO(exception);
            var dto2 = mapper.toErrorDTO(exception);

            // Assert
            assertNotNull(dto1.timestamp());
            assertNotNull(dto2.timestamp());
            assertTrue(dto1.timestamp().equals(dto2.timestamp()) ||
                            dto1.timestamp().isBefore(dto2.timestamp()),
                    "Second timestamp should be equal or after first timestamp");
        }
    }

    @Nested
    @DisplayName("When handling edge cases")
    class EdgeCases {
        @Test
        @DisplayName("Should handle exception with very long message")
        void shouldHandleExceptionWithVeryLongMessage() {
            // Arrange
            String longMessage = "a".repeat(10000);
            var exception = new RuntimeException(longMessage);

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals(longMessage, dto.message());
            assertEquals("SYSTEM_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }

        @Test
        @DisplayName("Should handle exception with special characters")
        void shouldHandleExceptionWithSpecialCharacters() {
            // Arrange
            String messageWithSpecialChars = "Error!\n\"Special\"\t×Chars×\r\n";
            var exception = new RuntimeException(messageWithSpecialChars);

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals(messageWithSpecialChars, dto.message());
            assertEquals("SYSTEM_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }

        @Test
        @DisplayName("Should handle anonymous exception class")
        void shouldHandleAnonymousExceptionClass() {
            // Arrange
            Exception exception = new Exception("Anonymous exception") {};

            // Act
            var dto = mapper.toErrorDTO(exception);

            // Assert
            assertEquals(exception.getMessage(), dto.message());
            assertEquals("SYSTEM_ERROR", dto.code());
            assertNotNull(dto.timestamp());
        }
    }
}