package com.github.djoarns.payflow.domain.user;

import com.github.djoarns.payflow.domain.user.valueobject.Password;
import com.github.djoarns.payflow.domain.user.valueobject.UserId;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import com.github.djoarns.payflow.infrastructure.persistence.entity.UserJpaEntity;
import com.github.djoarns.payflow.infrastructure.persistence.repository.UserJpaRepository;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRepositoryImplTest extends BaseUnitTest {

    @Mock
    private UserJpaRepository jpaRepository;

    private UserRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepositoryImpl(jpaRepository);
    }

    @Nested
    @DisplayName("save")
    class Save {
        @Test
        @DisplayName("Should save new user")
        void shouldSaveNewUser() {
            // Arrange
            User user = User.create(
                    Username.of("testuser"),
                    Password.of("password123")
            );

            UserJpaEntity savedEntity = new UserJpaEntity();
            savedEntity.setId(1L);
            savedEntity.setUsername("testuser");
            savedEntity.setPassword("password123");
            savedEntity.setRoles(Set.of(Role.USER));
            savedEntity.setEnabled(true);

            when(jpaRepository.save(any(UserJpaEntity.class)))
                    .thenReturn(savedEntity);

            // Act
            User result = repository.save(user);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId().getValue());
            assertEquals("testuser", result.getUsername().getValue());
            assertEquals("password123", result.getPassword().getValue());
            assertEquals(Set.of(Role.USER), result.getRoles());
            assertTrue(result.isEnabled());
            verify(jpaRepository).save(any(UserJpaEntity.class));
        }

        @Test
        @DisplayName("Should update existing user")
        void shouldUpdateExistingUser() {
            // Arrange
            User user = User.reconstitute(
                    UserId.of(1L),
                    Username.of("testuser"),
                    Password.of("password123"),
                    Set.of(Role.USER, Role.ADMIN),
                    true
            );

            UserJpaEntity savedEntity = new UserJpaEntity();
            savedEntity.setId(1L);
            savedEntity.setUsername("testuser");
            savedEntity.setPassword("password123");
            savedEntity.setRoles(Set.of(Role.USER, Role.ADMIN));
            savedEntity.setEnabled(true);

            when(jpaRepository.save(any(UserJpaEntity.class)))
                    .thenReturn(savedEntity);

            // Act
            User result = repository.save(user);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId().getValue());
            assertEquals(Set.of(Role.USER, Role.ADMIN), result.getRoles());
            verify(jpaRepository).save(any(UserJpaEntity.class));
        }
    }

    @Nested
    @DisplayName("findByUsername")
    class FindByUsername {
        @Test
        @DisplayName("Should find existing user by username")
        void shouldFindExistingUserByUsername() {
            // Arrange
            String username = "testuser";
            UserJpaEntity entity = new UserJpaEntity();
            entity.setId(1L);
            entity.setUsername(username);
            entity.setPassword("password123");
            entity.setRoles(Set.of(Role.USER));
            entity.setEnabled(true);

            when(jpaRepository.findByUsername(username))
                    .thenReturn(Optional.of(entity));

            // Act
            Optional<User> result = repository.findByUsername(Username.of(username));

            // Assert
            assertTrue(result.isPresent());
            assertEquals(username, result.get().getUsername().getValue());
            verify(jpaRepository).findByUsername(username);
        }

        @Test
        @DisplayName("Should return empty when user not found")
        void shouldReturnEmptyWhenUserNotFound() {
            // Arrange
            String username = "nonexistent";
            when(jpaRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            // Act
            Optional<User> result = repository.findByUsername(Username.of(username));

            // Assert
            assertTrue(result.isEmpty());
            verify(jpaRepository).findByUsername(username);
        }
    }

    @Nested
    @DisplayName("existsByUsername")
    class ExistsByUsername {
        @Test
        @DisplayName("Should return true when username exists")
        void shouldReturnTrueWhenUsernameExists() {
            // Arrange
            String username = "testuser";
            when(jpaRepository.existsByUsername(username))
                    .thenReturn(true);

            // Act
            boolean result = repository.existsByUsername(Username.of(username));

            // Assert
            assertTrue(result);
            verify(jpaRepository).existsByUsername(username);
        }

        @Test
        @DisplayName("Should return false when username does not exist")
        void shouldReturnFalseWhenUsernameDoesNotExist() {
            // Arrange
            String username = "nonexistent";
            when(jpaRepository.existsByUsername(username))
                    .thenReturn(false);

            // Act
            boolean result = repository.existsByUsername(Username.of(username));

            // Assert
            assertFalse(result);
            verify(jpaRepository).existsByUsername(username);
        }
    }
}