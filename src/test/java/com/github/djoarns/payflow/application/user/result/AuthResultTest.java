package com.github.djoarns.payflow.application.user.result;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResultTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create Authentication result with valid data")
    void shouldCreateAuthenticationResultWithValidData() {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";

        // Act
        var result = new AuthResult.Authentication(token, username);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.token());
        assertEquals(username, result.username());
    }

    @Test
    @DisplayName("Should allow null values")
    void shouldAllowNullValues() {
        // Act
        var result = new AuthResult.Authentication(null, null);

        // Assert
        assertNotNull(result);
        assertNull(result.token());
        assertNull(result.username());
    }

    @Test
    @DisplayName("Should implement value record equality")
    void shouldImplementValueRecordEquality() {
        // Arrange
        var result1 = new AuthResult.Authentication("token1", "user1");
        var result2 = new AuthResult.Authentication("token1", "user1");
        var result3 = new AuthResult.Authentication("token2", "user2");

        // Assert
        assertEquals(result1, result2);
        assertNotEquals(result1, result3);
        assertEquals(result1.hashCode(), result2.hashCode());
        assertNotEquals(result1.hashCode(), result3.hashCode());
    }

    @Test
    @DisplayName("Should generate correct toString")
    void shouldGenerateCorrectToString() {
        // Arrange
        var result = new AuthResult.Authentication("token", "user");

        // Act
        String toString = result.toString();

        // Assert
        assertTrue(toString.contains("token"));
        assertTrue(toString.contains("user"));
    }
}