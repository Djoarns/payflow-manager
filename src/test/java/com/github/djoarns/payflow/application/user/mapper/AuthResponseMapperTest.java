package com.github.djoarns.payflow.application.user.mapper;

import com.github.djoarns.payflow.application.user.dto.response.AuthResponseDTO;
import com.github.djoarns.payflow.application.user.result.AuthResult;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseMapperTest extends BaseUnitTest {

    private AuthResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AuthResponseMapper();
    }

    @Test
    @DisplayName("Should map authentication result to response DTO")
    void shouldMapAuthenticationResultToResponseDto() {
        // Arrange
        var result = new AuthResult.Authentication(
                "jwt.token.string",
                "testuser"
        );

        // Act
        var response = mapper.toResponseDTO(result);

        // Assert
        assertNotNull(response);
        assertEquals(result.token(), response.token());
        assertEquals(result.username(), response.username());
    }

    @ParameterizedTest
    @MethodSource("authenticationDataProvider")
    @DisplayName("Should map different authentication data correctly")
    void shouldMapDifferentAuthenticationData(String token, String username) {
        // Arrange
        var result = new AuthResult.Authentication(token, username);

        // Act
        var response = mapper.toResponseDTO(result);

        // Assert
        assertEquals(token, response.token());
        assertEquals(username, response.username());
    }

    private static Stream<Arguments> authenticationDataProvider() {
        return Stream.of(
                Arguments.of("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", "user1"),
                Arguments.of("short.token", "admin"),
                Arguments.of("very.long.jwt.token.with.many.segments", "test@domain.com"),
                Arguments.of("", "")
        );
    }

    @Test
    @DisplayName("Should handle empty token and username")
    void shouldHandleEmptyTokenAndUsername() {
        // Arrange
        var result = new AuthResult.Authentication("", "");

        // Act
        AuthResponseDTO.Authentication response = mapper.toResponseDTO(result);

        // Assert
        assertEquals("", response.token());
        assertEquals("", response.username());
    }

    @Test
    @DisplayName("Should create immutable response object")
    void shouldCreateImmutableResponseObject() {
        // Arrange
        var result = new AuthResult.Authentication("token", "user");

        // Act
        var response = mapper.toResponseDTO(result);

        // Assert
        assertNotNull(response);
        // Verifique se o objeto é um record (implicitamente imutável)
        assertTrue(response.getClass().isRecord());
    }
}