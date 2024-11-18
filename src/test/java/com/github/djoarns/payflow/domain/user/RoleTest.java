package com.github.djoarns.payflow.domain.user;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest extends BaseUnitTest {

    @Test
    @DisplayName("Should have correct enum values")
    void shouldHaveCorrectEnumValues() {
        // Arrange & Act
        Role[] roles = Role.values();

        // Assert
        assertEquals(2, roles.length);
        assertArrayEquals(
                new Role[] {
                        Role.USER,
                        Role.ADMIN
                },
                roles
        );
    }

    @Test
    @DisplayName("Should correctly convert to string")
    void shouldCorrectlyConvertToString() {
        assertEquals("USER", Role.USER.toString());
        assertEquals("ADMIN", Role.ADMIN.toString());
    }

    @Test
    @DisplayName("Should maintain enum singleton pattern")
    void shouldMaintainEnumSingleton() {
        assertSame(Role.USER, Role.valueOf("USER"));
        assertSame(Role.ADMIN, Role.valueOf("ADMIN"));
    }
}