package com.github.djoarns.payflow.application.user.command;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthCommandTest extends BaseUnitTest {

    @Nested
    @DisplayName("Register Command")
    class RegisterCommand {
        @Test
        @DisplayName("Should create register command with valid data")
        void shouldCreateRegisterCommandWithValidData() {
            // Arrange
            String username = "testuser";
            String password = "password123";

            // Act
            var command = new AuthCommand.Register(username, password);

            // Assert
            assertNotNull(command);
            assertEquals(username, command.username());
            assertEquals(password, command.password());
        }

        @Test
        @DisplayName("Should allow null values in creation")
        void shouldAllowNullValues() {
            // Act
            var command = new AuthCommand.Register(null, null);

            // Assert
            assertNotNull(command);
            assertNull(command.username());
            assertNull(command.password());
        }

        @Test
        @DisplayName("Should implement value object equality")
        void shouldImplementValueObjectEquality() {
            // Arrange
            var command1 = new AuthCommand.Register("user", "pass");
            var command2 = new AuthCommand.Register("user", "pass");
            var command3 = new AuthCommand.Register("other", "pass");

            // Assert
            assertEquals(command1, command2);
            assertNotEquals(command1, command3);
            assertEquals(command1.hashCode(), command2.hashCode());
            assertNotEquals(command1.hashCode(), command3.hashCode());
        }

        @Test
        @DisplayName("Should create command with empty strings")
        void shouldCreateCommandWithEmptyStrings() {
            // Act
            var command = new AuthCommand.Register("", "");

            // Assert
            assertNotNull(command);
            assertEquals("", command.username());
            assertEquals("", command.password());
        }
    }

    @Nested
    @DisplayName("Login Command")
    class LoginCommand {
        @Test
        @DisplayName("Should create login command with valid data")
        void shouldCreateLoginCommandWithValidData() {
            // Arrange
            String username = "testuser";
            String password = "password123";

            // Act
            var command = new AuthCommand.Login(username, password);

            // Assert
            assertNotNull(command);
            assertEquals(username, command.username());
            assertEquals(password, command.password());
        }

        @Test
        @DisplayName("Should allow null values in creation")
        void shouldAllowNullValues() {
            // Act
            var command = new AuthCommand.Login(null, null);

            // Assert
            assertNotNull(command);
            assertNull(command.username());
            assertNull(command.password());
        }

        @Test
        @DisplayName("Should implement value object equality")
        void shouldImplementValueObjectEquality() {
            // Arrange
            var command1 = new AuthCommand.Login("user", "pass");
            var command2 = new AuthCommand.Login("user", "pass");
            var command3 = new AuthCommand.Login("other", "pass");

            // Assert
            assertEquals(command1, command2);
            assertNotEquals(command1, command3);
            assertEquals(command1.hashCode(), command2.hashCode());
            assertNotEquals(command1.hashCode(), command3.hashCode());
        }

        @Test
        @DisplayName("Should create command with empty strings")
        void shouldCreateCommandWithEmptyStrings() {
            // Act
            var command = new AuthCommand.Login("", "");

            // Assert
            assertNotNull(command);
            assertEquals("", command.username());
            assertEquals("", command.password());
        }
    }
}