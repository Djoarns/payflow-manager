package com.github.djoarns.payflow.infrastructure.security;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecurityBeansTest extends BaseUnitTest {

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private AuthenticationConfiguration authConfig;
    @Mock
    private AuthenticationManager authManager;

    private SecurityBeans securityBeans;

    @BeforeEach
    void setUp() {
        securityBeans = new SecurityBeans(userDetailsService);
    }

    @Test
    @DisplayName("Should create AuthenticationManager bean")
    void shouldCreateAuthenticationManager() throws Exception {
        // Arrange
        when(authConfig.getAuthenticationManager()).thenReturn(authManager);

        // Act
        AuthenticationManager result = securityBeans.authenticationManager(authConfig);

        // Assert
        assertNotNull(result);
        assertEquals(authManager, result);
        verify(authConfig).getAuthenticationManager();
    }

    @Test
    @DisplayName("Should create PasswordEncoder bean")
    void shouldCreatePasswordEncoder() {
        // Act
        PasswordEncoder encoder = securityBeans.passwordEncoder();

        // Assert
        assertNotNull(encoder);

        // Test password encoding
        String rawPassword = "testPassword";
        String encodedPassword = encoder.encode(rawPassword);

        // Verify encoding behavior
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
        assertFalse(encoder.matches("wrongPassword", encodedPassword));
    }
}