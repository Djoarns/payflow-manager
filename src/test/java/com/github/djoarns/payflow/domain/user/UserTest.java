package com.github.djoarns.payflow.domain.user;

import com.github.djoarns.payflow.domain.user.valueobject.Password;
import com.github.djoarns.payflow.domain.user.valueobject.UserId;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest extends BaseUnitTest {

    @Nested
    @DisplayName("User.create")
    class UserCreate {
        @Test
        @DisplayName("Should create user with valid data")
        void shouldCreateUserWithValidData() {
            // Arrange
            Username username = Username.of("testuser");
            Password password = Password.of("password123");

            // Act
            User user = User.create(username, password);

            // Assert
            assertNull(user.getId());
            assertEquals(username, user.getUsername());
            assertEquals(password, user.getPassword());
            assertEquals(Set.of(Role.USER), user.getRoles());
            assertTrue(user.isEnabled());
        }

        @Test
        @DisplayName("Should create user with default role USER")
        void shouldCreateUserWithDefaultRoleUser() {
            // Arrange
            Username username = Username.of("testuser");
            Password password = Password.of("password123");

            // Act
            User user = User.create(username, password);

            // Assert
            assertEquals(1, user.getRoles().size());
            assertTrue(user.getRoles().contains(Role.USER));
        }

        @Test
        @DisplayName("Should create enabled user by default")
        void shouldCreateEnabledUserByDefault() {
            // Arrange
            Username username = Username.of("testuser");
            Password password = Password.of("password123");

            // Act
            User user = User.create(username, password);

            // Assert
            assertTrue(user.isEnabled());
        }
    }

    @Nested
    @DisplayName("User.reconstitute")
    class UserReconstitute {
        @Test
        @DisplayName("Should reconstitute user with all attributes")
        void shouldReconstituteUserWithAllAttributes() {
            // Arrange
            UserId id = UserId.of(1L);
            Username username = Username.of("testuser");
            Password password = Password.of("password123");
            Set<Role> roles = Set.of(Role.USER, Role.ADMIN);
            boolean enabled = true;

            // Act
            User user = User.reconstitute(id, username, password, roles, enabled);

            // Assert
            assertEquals(id, user.getId());
            assertEquals(username, user.getUsername());
            assertEquals(password, user.getPassword());
            assertEquals(roles, user.getRoles());
            assertTrue(user.isEnabled());
        }

        @Test
        @DisplayName("Should reconstitute disabled user")
        void shouldReconstituteDisabledUser() {
            // Arrange
            UserId id = UserId.of(1L);
            Username username = Username.of("testuser");
            Password password = Password.of("password123");
            Set<Role> roles = Set.of(Role.USER);
            boolean enabled = false;

            // Act
            User user = User.reconstitute(id, username, password, roles, enabled);

            // Assert
            assertFalse(user.isEnabled());
        }

        @Test
        @DisplayName("Should reconstitute user with multiple roles")
        void shouldReconstituteUserWithMultipleRoles() {
            // Arrange
            UserId id = UserId.of(1L);
            Username username = Username.of("testuser");
            Password password = Password.of("password123");
            Set<Role> roles = Set.of(Role.USER, Role.ADMIN);
            boolean enabled = true;

            // Act
            User user = User.reconstitute(id, username, password, roles, enabled);

            // Assert
            assertEquals(2, user.getRoles().size());
            assertTrue(user.getRoles().contains(Role.USER));
            assertTrue(user.getRoles().contains(Role.ADMIN));
        }
    }
}