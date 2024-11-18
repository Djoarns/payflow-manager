package com.github.djoarns.payflow.infrastructure.csv;

import com.github.djoarns.payflow.domain.bill.Bill;
import com.github.djoarns.payflow.util.BaseUnitTest;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillCSVImporterTest extends BaseUnitTest {

    private BillCSVImporter importer;

    @BeforeEach
    void setUp() {
        importer = new BillCSVImporter();
    }

    @Nested
    @DisplayName("importBills")
    class ImportBills {
        @Test
        @DisplayName("Should import valid CSV content")
        void shouldImportValidCsvContent() throws IOException {
            // Arrange
            String csvContent = """
                dueDate,amount,description
                2024-12-31,100.50,Test Bill 1
                2024-12-31,200.75,Test Bill 2
                """;
            InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

            // Act
            List<Bill> bills = importer.importBills(inputStream);

            // Assert
            assertNotNull(bills);
            assertEquals(2, bills.size());
            assertEquals("Test Bill 1", bills.get(0).getDescription().getValue());
            assertEquals("Test Bill 2", bills.get(1).getDescription().getValue());
        }

        @Test
        @DisplayName("Should handle empty CSV file")
        void shouldHandleEmptyCsvFile() throws IOException {
            // Arrange
            String csvContent = "dueDate,amount,description\n";
            InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

            // Act
            List<Bill> bills = importer.importBills(inputStream);

            // Assert
            assertNotNull(bills);
            assertTrue(bills.isEmpty());
        }

        @Test
        @DisplayName("Should handle CSV with invalid amount")
        void shouldHandleCsvWithInvalidAmount() throws IOException {
            // Arrange
            String csvContent = """
                dueDate,amount,description
                2024-12-31,-100.50,Invalid Amount Bill
                2024-12-31,200.75,Valid Bill
                """;
            InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

            // Act
            List<Bill> bills = importer.importBills(inputStream);

            // Assert
            assertNotNull(bills);
            assertEquals(1, bills.size());
            assertEquals("Valid Bill", bills.get(0).getDescription().getValue());
        }

        @Test
        @DisplayName("Should handle CSV with empty description")
        void shouldHandleCsvWithEmptyDescription() {
            // Arrange
            String csvContent = """
                dueDate,amount,description
                2024-12-31,100.50,
                2024-12-31,200.75,Valid Bill
                """;
            InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> importer.importBills(inputStream)
            );
            assertInstanceOf(CsvRequiredFieldEmptyException.class, exception.getCause());

            CsvRequiredFieldEmptyException cause = (CsvRequiredFieldEmptyException) exception.getCause();
            assertEquals("Field 'description' is mandatory but no value was provided.", cause.getMessage());
        }

        @Test
        @DisplayName("Should handle malformed CSV content")
        void shouldHandleMalformedCsvContent() {
            // Arrange
            String csvContent = "malformed,csv,content\nwithout,proper,headers";
            InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

            // Act & Assert
            RuntimeException exception = assertThrows(
                    RuntimeException.class,
                    () -> importer.importBills(inputStream)
            );
            assertInstanceOf(CsvRequiredFieldEmptyException.class, exception.getCause());

            CsvRequiredFieldEmptyException cause = (CsvRequiredFieldEmptyException) exception.getCause();
            String expectedMessage = "Header is missing required fields [AMOUNT, DESCRIPTION, DUEDATE]. The list of headers encountered is [malformed,csv,content].";
            assertEquals(expectedMessage, cause.getMessage());
        }
    }
}