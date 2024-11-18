package com.github.djoarns.payflow.infrastructure.security.user;

import com.github.djoarns.payflow.domain.user.Role;
import com.github.djoarns.payflow.domain.user.User;
import com.github.djoarns.payflow.domain.user.valueobject.Password;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest extends BaseUnitTest {

    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        user = User.create(
                Username.of("testuser"),
                Password.of("password123")
        );
        userDetails = new UserDetailsImpl(user);
    }

    @Nested
    @DisplayName("getAuthorities")
    class GetAuthorities {
        @Test
        @DisplayName("Should return correct authorities for user roles")
        void shouldReturnCorrectAuthoritiesForUserRoles() {
            // Arrange
            var expectedAuthority = new SimpleGrantedAuthority("ROLE_USER");

            // Act
            var authorities = userDetails.getAuthorities();

            // Assert
            assertNotNull(authorities);
            assertEquals(1, authorities.size());
            assertTrue(authorities.contains(expectedAuthority));
        }

        @Test
        @DisplayName("Should handle multiple roles")
        void shouldHandleMultipleRoles() {
            // Arrange
            user = User.reconstitute(
                    null,
                    Username.of("testuser"),
                    Password.of("password123"),
                    Set.of(Role.USER, Role.ADMIN),
                    true
            );
            userDetails = new UserDetailsImpl(user);

            var expectedAuthorities = Set.of(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN")
            );

            // Act
            var authorities = userDetails.getAuthorities();

            // Assert
            assertNotNull(authorities);
            assertEquals(2, authorities.size());
            for (GrantedAuthority authority : authorities) {
                assertTrue(expectedAuthorities.contains(authority));
            }
        }
    }

    @Test
    @DisplayName("Should return correct username")
    void shouldReturnCorrectUsername() {
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    @DisplayName("Should return correct password")
    void shouldReturnCorrectPassword() {
        assertEquals("password123", userDetails.getPassword());
    }

    @Test
    @DisplayName("Should return enabled status from user")
    void shouldReturnEnabledStatusFromUser() {
        assertEquals(user.isEnabled(), userDetails.isEnabled());
    }

    @Nested
    @DisplayName("Account Status Tests")
    class AccountStatusTests {
        @Test
        @DisplayName("Should always return true for isAccountNonExpired")
        void shouldReturnTrueForIsAccountNonExpired() {
            assertTrue(userDetails.isAccountNonExpired());
        }

        @Test
        @DisplayName("Should always return true for isAccountNonLocked")
        void shouldReturnTrueForIsAccountNonLocked() {
            assertTrue(userDetails.isAccountNonLocked());
        }

        @Test
        @DisplayName("Should always return true for isCredentialsNonExpired")
        void shouldReturnTrueForIsCredentialsNonExpired() {
            assertTrue(userDetails.isCredentialsNonExpired());
        }
    }

    @Test
    @DisplayName("Should handle disabled user")
    void shouldHandleDisabledUser() {
        // Arrange
        user = User.reconstitute(
                null,
                Username.of("testuser"),
                Password.of("password123"),
                Set.of(Role.USER),
                false
        );
        userDetails = new UserDetailsImpl(user);

        // Assert
        assertFalse(userDetails.isEnabled());
    }
}