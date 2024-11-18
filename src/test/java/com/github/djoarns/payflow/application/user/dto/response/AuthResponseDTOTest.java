package com.github.djoarns.payflow.application.user.dto.response;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseDTOTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create authentication response with valid data")
    void shouldCreateAuthenticationResponse() {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";

        // Act
        var response = new AuthResponseDTO.Authentication(token, username);

        // Assert
        assertNotNull(response);
        assertEquals(token, response.token());
        assertEquals(username, response.username());
    }

    @Test
    @DisplayName("Should allow null values")
    void shouldAllowNullValues() {
        // Act
        var response = new AuthResponseDTO.Authentication(null, null);

        // Assert
        assertNotNull(response);
        assertNull(response.token());
        assertNull(response.username());
    }

    @Test
    @DisplayName("Should implement value object equality")
    void shouldImplementValueObjectEquality() {
        // Arrange
        var response1 = new AuthResponseDTO.Authentication("token1", "user1");
        var response2 = new AuthResponseDTO.Authentication("token1", "user1");
        var response3 = new AuthResponseDTO.Authentication("token2", "user2");

        // Assert
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
}