package com.github.djoarns.payflow.domain.user.valueobject;

import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest extends BaseUnitTest {

    @Nested
    @DisplayName("UserId.of")
    class UserIdOf {
        @Test
        @DisplayName("Should create UserId when value is valid")
        void shouldCreateUserIdWhenValid() {
            // Arrange
            Long validId = 1L;

            // Act
            UserId userId = UserId.of(validId);

            // Assert
            assertNotNull(userId);
            assertEquals(validId, userId.getValue());
        }

        @Test
        @DisplayName("Should create UserId with maximum valid value")
        void shouldCreateUserIdWithMaxValue() {
            // Arrange
            Long maxValue = Long.MAX_VALUE;

            // Act
            UserId userId = UserId.of(maxValue);

            // Assert
            assertNotNull(userId);
            assertEquals(maxValue, userId.getValue());
        }

        @Test
        @DisplayName("Should throw exception when id is null")
        void shouldThrowExceptionWhenNull() {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> UserId.of(null)
            );
            assertEquals("Invalid user ID", exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(longs = {0L, -1L, -100L})
        @DisplayName("Should throw exception when id is zero or negative")
        void shouldThrowExceptionWhenZeroOrNegative(Long invalidId) {
            // Act & Assert
            InvalidUserDataException exception = assertThrows(
                    InvalidUserDataException.class,
                    () -> UserId.of(invalidId)
            );
            assertEquals("Invalid user ID", exception.getMessage());
        }
    }

    @Test
    @DisplayName("UserId should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        UserId id1 = UserId.of(1L);
        UserId id2 = UserId.of(1L);
        UserId id3 = UserId.of(2L);

        // Assert
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }
}