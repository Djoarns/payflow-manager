package com.github.djoarns.payflow.application.bill.dto.response;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseDTOTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create ErrorResponse with all fields")
    void shouldCreateErrorResponseWithAllFields() {
        // Arrange
        String message = "Test error message";
        String code = "TEST_ERROR";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        var response = new ErrorResponseDTO(message, code, timestamp);

        // Assert
        assertNotNull(response);
        assertEquals(message, response.message());
        assertEquals(code, response.code());
        assertEquals(timestamp, response.timestamp());
    }

    @Test
    @DisplayName("Should handle null values")
    void shouldHandleNullValues() {
        // Act
        var response = new ErrorResponseDTO(null, null, null);

        // Assert
        assertNull(response.message());
        assertNull(response.code());
        assertNull(response.timestamp());
    }

    @Test
    @DisplayName("Should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        var timestamp = LocalDateTime.now();
        var response1 = new ErrorResponseDTO("Error", "CODE", timestamp);
        var response2 = new ErrorResponseDTO("Error", "CODE", timestamp);
        var response3 = new ErrorResponseDTO("Different", "CODE", timestamp);

        // Assert
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    @DisplayName("Should create meaningful string representation")
    void shouldCreateMeaningfulStringRepresentation() {
        // Arrange
        var timestamp = LocalDateTime.now();
        var response = new ErrorResponseDTO("Test error", "TEST_ERROR", timestamp);

        // Act
        var string = response.toString();

        // Assert
        assertTrue(string.contains("Test error"));
        assertTrue(string.contains("TEST_ERROR"));
        assertTrue(string.contains(timestamp.toString()));
    }
}
