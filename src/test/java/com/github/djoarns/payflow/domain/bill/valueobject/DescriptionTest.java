package com.github.djoarns.payflow.domain.bill.valueobject;

import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionTest {

    @Nested
    @DisplayName("Description.of")
    class DescriptionOf {
        @Test
        @DisplayName("Should create Description when value is valid")
        void shouldCreateDescriptionWhenValid() {
            // Arrange
            String validDescription = "Internet bill payment";

            // Act
            Description description = Description.of(validDescription);

            // Assert
            assertNotNull(description);
            assertEquals(validDescription, description.getValue());
        }

        @Test
        @DisplayName("Should trim description")
        void shouldTrimDescription() {
            // Arrange
            String untrimmedDescription = "  Internet bill   ";

            // Act
            Description description = Description.of(untrimmedDescription);

            // Assert
            assertEquals("Internet bill", description.getValue());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "   "})
        @DisplayName("Should throw exception when description is empty or blank")
        void shouldThrowExceptionWhenEmptyOrBlank(String invalidDescription) {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> Description.of(invalidDescription)
            );
            assertEquals("Description cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when description is null")
        void shouldThrowExceptionWhenNull() {
            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> Description.of(null)
            );
            assertEquals("Description cannot be empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when description exceeds max length")
        void shouldThrowExceptionWhenTooLong() {
            // Arrange
            String longDescription = "a".repeat(256);

            // Act & Assert
            InvalidBillDataException exception = assertThrows(
                    InvalidBillDataException.class,
                    () -> Description.of(longDescription)
            );
            assertEquals("Description cannot be longer than 255 characters", exception.getMessage());
        }
    }

    @Test
    @DisplayName("Description should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        Description desc1 = Description.of("Internet bill");
        Description desc2 = Description.of("Internet bill");
        Description desc3 = Description.of("Phone bill");

        // Assert
        assertEquals(desc1, desc2);
        assertNotEquals(desc1, desc3);
        assertEquals(desc1.hashCode(), desc2.hashCode());
        assertNotEquals(desc1.hashCode(), desc3.hashCode());
    }
}