package com.github.djoarns.payflow.application.mapper;

import com.github.djoarns.payflow.application.bill.mapper.ErrorMapper;
import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import com.github.djoarns.payflow.domain.exception.BillDomainException;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorMapperTest extends BaseUnitTest {

    private ErrorMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ErrorMapper();
    }

    @Test
    @DisplayName("Should map domain exception to error DTO")
    void shouldMapDomainExceptionToErrorDto() {
        // Arrange
        var exception = new InvalidBillDataException("Test error message");

        // Act
        var dto = mapper.toErrorDTO(exception);

        // Assert
        assertEquals(exception.getMessage(), dto.message());
        assertEquals("BUSINESS_ERROR", dto.code());
        assertNotNull(dto.timestamp());
    }

    @Test
    @DisplayName("Should map system exception to error DTO")
    void shouldMapSystemExceptionToErrorDto() {
        // Arrange
        var exception = new RuntimeException("Test error message");

        // Act
        var dto = mapper.toErrorDTO(exception);

        // Assert
        assertEquals(exception.getMessage(), dto.message());
        assertEquals("SYSTEM_ERROR", dto.code());
        assertNotNull(dto.timestamp());
    }

    @Test
    @DisplayName("Should map nested exception to error DTO")
    void shouldMapNestedExceptionToErrorDto() {
        // Arrange
        var cause = new IllegalArgumentException("Root cause");
        var exception = new RuntimeException("Wrapper", cause);

        // Act
        var dto = mapper.toErrorDTO(exception);

        // Assert
        assertEquals(exception.getMessage(), dto.message());
        assertEquals("SYSTEM_ERROR", dto.code());
        assertNotNull(dto.timestamp());
    }

    @Test
    @DisplayName("Should map null message exception to error DTO")
    void shouldMapNullMessageExceptionToErrorDto() {
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
    @DisplayName("Should map custom domain exception to error DTO")
    void shouldMapCustomDomainExceptionToErrorDto() {
        // Arrange
        var exception = new BillDomainException("Custom domain error");

        // Act
        var dto = mapper.toErrorDTO(exception);

        // Assert
        assertEquals(exception.getMessage(), dto.message());
        assertEquals("BUSINESS_ERROR", dto.code());
        assertNotNull(dto.timestamp());
    }

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
        assertTrue(
                !dto.timestamp().isBefore(before) &&
                        !dto.timestamp().isAfter(after),
                "Timestamp should be between test execution times"
        );
    }
}
