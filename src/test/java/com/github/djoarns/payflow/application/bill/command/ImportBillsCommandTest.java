package com.github.djoarns.payflow.application.bill.command;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ImportBillsCommandTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create command with valid input stream and filename")
    void shouldCreateCommandWithValidData() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String filename = "test.csv";

        // Act
        var command = new ImportBillsCommand(inputStream, filename);

        // Assert
        assertNotNull(command);
        assertEquals(inputStream, command.file());
        assertEquals(filename, command.filename());
    }

    @Test
    @DisplayName("Should allow null values")
    void shouldAllowNullValues() {
        // Act
        var command = new ImportBillsCommand(null, null);

        // Assert
        assertNotNull(command);
        assertNull(command.file());
        assertNull(command.filename());
    }

    @Test
    @DisplayName("Should create command with empty filename")
    void shouldCreateCommandWithEmptyFilename() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String filename = "";

        // Act
        var command = new ImportBillsCommand(inputStream, filename);

        // Assert
        assertNotNull(command);
        assertEquals(inputStream, command.file());
        assertEquals(filename, command.filename());
    }
}