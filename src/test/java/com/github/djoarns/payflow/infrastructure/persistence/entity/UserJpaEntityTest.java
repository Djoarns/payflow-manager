package com.github.djoarns.payflow.infrastructure.persistence.entity;

import com.github.djoarns.payflow.domain.user.Role;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserJpaEntity")
class UserJpaEntityTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create empty entity")
    void shouldCreateEmptyEntity() {
        // Act
        UserJpaEntity entity = new UserJpaEntity();

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getUsername());
        assertNull(entity.getPassword());
        assertNull(entity.getRoles());
        assertTrue(entity.isEnabled()); // Default value is true
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        // Arrange
        UserJpaEntity entity = new UserJpaEntity();
        Long id = 1L;
        String username = "testuser";
        String password = "password123";
        Set<Role> roles = Set.of(Role.USER, Role.ADMIN);
        boolean enabled = false;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        entity.setId(id);
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setRoles(roles);
        entity.setEnabled(enabled);
        entity.setCreatedAt(timestamp);
        entity.setUpdatedAt(timestamp);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals(username, entity.getUsername());
        assertEquals(password, entity.getPassword());
        assertEquals(roles, entity.getRoles());
        assertEquals(enabled, entity.isEnabled());
        assertEquals(timestamp, entity.getCreatedAt());
        assertEquals(timestamp, entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set timestamps on creation")
    void shouldSetTimestampsOnCreation() {
        // Arrange
        UserJpaEntity entity = new UserJpaEntity();
        LocalDateTime before = LocalDateTime.now();

        // Act
        entity.onCreate();
        LocalDateTime after = LocalDateTime.now();

        // Assert
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertEquals(entity.getCreatedAt(), entity.getUpdatedAt());
        assertTrue(
                !entity.getCreatedAt().isBefore(before) &&
                        !entity.getCreatedAt().isAfter(after),
                "Created timestamp should be between test execution times"
        );
    }

    @Test
    @DisplayName("Should have correct default enabled value")
    void shouldHaveCorrectDefaultEnabledValue() {
        // Act
        UserJpaEntity entity = new UserJpaEntity();

        // Assert
        assertTrue(entity.isEnabled(), "Default enabled value should be true");
    }
}