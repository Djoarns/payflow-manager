package com.github.djoarns.payflow.application.bill.dto.request;

import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class BillImportDTOTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create import DTO with valid data")
    void shouldCreateImportDTOWithValidData() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String filename = "test.csv";

        // Act
        var dto = new BillImportDTO.Import(inputStream, filename);

        // Assert
        assertNotNull(dto);
        assertEquals(inputStream, dto.file());
        assertEquals(filename, dto.filename());
    }

    @Test
    @DisplayName("Should allow null values")
    void shouldAllowNullValues() {
        // Act
        var dto = new BillImportDTO.Import(null, null);

        // Assert
        assertNotNull(dto);
        assertNull(dto.file());
        assertNull(dto.filename());
    }

    @Test
    @DisplayName("Should create import DTO with empty filename")
    void shouldCreateImportDTOWithEmptyFilename() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        String filename = "";

        // Act
        var dto = new BillImportDTO.Import(inputStream, filename);

        // Assert
        assertNotNull(dto);
        assertEquals(inputStream, dto.file());
        assertEquals(filename, dto.filename());
    }
}