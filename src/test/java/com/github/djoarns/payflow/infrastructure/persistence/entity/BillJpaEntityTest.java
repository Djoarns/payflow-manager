package com.github.djoarns.payflow.infrastructure.persistence.entity;

import com.github.djoarns.payflow.domain.bill.valueobject.Status;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BillJpaEntity")
class BillJpaEntityTest extends BaseUnitTest {

    @Test
    @DisplayName("Should create empty entity")
    void shouldCreateEmptyEntity() {
        // Act
        BillJpaEntity entity = new BillJpaEntity();

        // Assert
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getDueDate());
        assertNull(entity.getPaymentDate());
        assertNull(entity.getAmount());
        assertNull(entity.getDescription());
        assertNull(entity.getStatus());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        // Arrange
        BillJpaEntity entity = new BillJpaEntity();
        Long id = 1L;
        LocalDate dueDate = LocalDate.now();
        LocalDate paymentDate = LocalDate.now();
        BigDecimal amount = new BigDecimal("100.00");
        String description = "Test Bill";
        Status status = Status.PENDING;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        entity.setId(id);
        entity.setDueDate(dueDate);
        entity.setPaymentDate(paymentDate);
        entity.setAmount(amount);
        entity.setDescription(description);
        entity.setStatus(status);
        entity.setCreatedAt(timestamp);
        entity.setUpdatedAt(timestamp);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals(dueDate, entity.getDueDate());
        assertEquals(paymentDate, entity.getPaymentDate());
        assertEquals(amount, entity.getAmount());
        assertEquals(description, entity.getDescription());
        assertEquals(status, entity.getStatus());
        assertEquals(timestamp, entity.getCreatedAt());
        assertEquals(timestamp, entity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set timestamps on creation")
    void shouldSetTimestampsOnCreation() {
        // Arrange
        BillJpaEntity entity = new BillJpaEntity();
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
    @DisplayName("Should update timestamp on update")
    void shouldUpdateTimestampOnUpdate() throws InterruptedException {
        // Arrange
        BillJpaEntity entity = new BillJpaEntity();
        entity.onCreate();
        LocalDateTime originalTimestamp = entity.getUpdatedAt();

        // Add a small delay to ensure different timestamps
        Thread.sleep(10);

        LocalDateTime before = LocalDateTime.now();

        // Act
        entity.onUpdate();
        LocalDateTime after = LocalDateTime.now();

        // Assert
        assertNotNull(entity.getUpdatedAt());
        assertNotEquals(originalTimestamp, entity.getUpdatedAt());
        assertTrue(
                !entity.getUpdatedAt().isBefore(before) &&
                        !entity.getUpdatedAt().isAfter(after),
                "Updated timestamp should be between test execution times"
        );
    }
}