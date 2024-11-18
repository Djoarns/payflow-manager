package com.github.djoarns.payflow.infrastructure.security.user;

import com.github.djoarns.payflow.domain.user.User;
import com.github.djoarns.payflow.domain.user.UserRepository;
import com.github.djoarns.payflow.domain.user.valueobject.Password;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void shouldLoadUserByUsernameSuccessfully() {
        // Arrange
        String username = "testuser";
        User user = User.create(
                Username.of(username),
                Password.of("password123")
        );

        when(userRepository.findByUsername(any(Username.class)))
                .thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertTrue(userDetails instanceof UserDetailsImpl);
        verify(userRepository).findByUsername(Username.of(username));
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.findByUsername(any(Username.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username)
        );
        assertEquals("User not found: " + username, exception.getMessage());
        verify(userRepository).findByUsername(Username.of(username));
    }

    @Test
    @DisplayName("Should pass through repository exceptions")
    void shouldPassThroughRepositoryExceptions() {
        // Arrange
        String username = "testuser";
        when(userRepository.findByUsername(any(Username.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(
                RuntimeException.class,
                () -> userDetailsService.loadUserByUsername(username)
        );
        verify(userRepository).findByUsername(Username.of(username));
    }
}