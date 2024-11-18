package com.github.djoarns.payflow.infrastructure.security;

import com.github.djoarns.payflow.util.BaseUnitTest;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest extends BaseUnitTest {

    private JwtService jwtService;
    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long EXPIRATION = 86400000; // 1 day

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);
    }

    @Test
    @DisplayName("Should generate token successfully")
    void shouldGenerateToken() {
        // Arrange
        UserDetails userDetails = createTestUser();

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    @DisplayName("Should extract username from token")
    void shouldExtractUsername() {
        // Arrange
        UserDetails userDetails = createTestUser();
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    @DisplayName("Should validate token successfully")
    void shouldValidateToken() {
        // Arrange
        UserDetails userDetails = createTestUser();
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should invalidate token with wrong user")
    void shouldInvalidateTokenWithWrongUser() {
        // Arrange
        UserDetails userDetails = createTestUser();
        UserDetails wrongUser = User.withUsername("wronguser")
                .password("password")
                .authorities(new ArrayList<>())
                .build();
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, wrongUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should throw exception for expired token")
    void shouldThrowExceptionForExpiredToken() {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1); // Expired immediately
        UserDetails userDetails = createTestUser();
        String token = jwtService.generateToken(userDetails);

        // Act & Assert
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, userDetails));
    }

    private UserDetails createTestUser() {
        return User.withUsername("testuser")
                .password("password")
                .authorities(new ArrayList<>())
                .build();
    }
}