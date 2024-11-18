package com.github.djoarns.payflow.domain.bill;

import com.github.djoarns.payflow.infrastructure.persistence.entity.BillJpaEntity;
import com.github.djoarns.payflow.infrastructure.persistence.repository.BillJpaRepository;
import com.github.djoarns.payflow.domain.bill.valueobject.*;
import com.github.djoarns.payflow.util.BaseUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BillRepositoryImplTest extends BaseUnitTest {

    @Mock
    private BillJpaRepository jpaRepository;

    private BillRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new BillRepositoryImpl(jpaRepository);
    }

    @Test
    @DisplayName("Should save bill successfully")
    void shouldSaveBillSuccessfully() {
        // Arrange
        var bill = createTestBill();
        var jpaEntity = createTestJpaEntity();

        when(jpaRepository.save(any(BillJpaEntity.class))).thenReturn(jpaEntity);

        // Act
        var result = repository.save(bill);

        // Assert
        assertNotNull(result);
        assertEquals(bill.getDueDate().getValue(), result.getDueDate().getValue());
        assertEquals(bill.getAmount().getValue(), result.getAmount().getValue());
        assertEquals(bill.getDescription().getValue(), result.getDescription().getValue());
        verify(jpaRepository).save(any(BillJpaEntity.class));
    }

    @Test
    @DisplayName("Should find bill by id successfully")
    void shouldFindBillByIdSuccessfully() {
        // Arrange
        var id = 1L;
        var jpaEntity = createTestJpaEntity();

        when(jpaRepository.findById(id)).thenReturn(Optional.of(jpaEntity));

        // Act
        var result = repository.findById(BillId.of(id));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(jpaEntity.getId(), result.get().getId().getValue());
        verify(jpaRepository).findById(id);
    }

    @Test
    @DisplayName("Should return empty when bill not found")
    void shouldReturnEmptyWhenBillNotFound() {
        // Arrange
        var id = 1L;
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        var result = repository.findById(BillId.of(id));

        // Assert
        assertTrue(result.isEmpty());
        verify(jpaRepository).findById(id);
    }

    @Test
    @DisplayName("Should find bills by due date between and description")
    void shouldFindBillsByDueDateBetweenAndDescription() {
        // Arrange
        var startDate = LocalDate.now();
        var endDate = LocalDate.now().plusDays(30);
        var description = "Test";
        var page = 0;
        var size = 10;
        var jpaEntities = List.of(createTestJpaEntity());

        when(jpaRepository.findByDueDateBetweenAndDescriptionContainingIgnoreCase(
                eq(startDate), eq(endDate), eq(description), any(PageRequest.class)))
                .thenReturn(jpaEntities);

        // Act
        var result = repository.findByDueDateBetweenAndDescription(
                startDate, endDate, description, page, size);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(jpaEntities.size(), result.size());
        verify(jpaRepository).findByDueDateBetweenAndDescriptionContainingIgnoreCase(
                eq(startDate), eq(endDate), eq(description), any(PageRequest.class));
    }

    @Test
    @DisplayName("Should find bills by payment date between")
    void shouldFindBillsByPaymentDateBetween() {
        // Arrange
        var startDate = LocalDate.now();
        var endDate = LocalDate.now().plusDays(30);
        var jpaEntities = List.of(createTestJpaEntity());

        when(jpaRepository.findByPaymentDateBetween(startDate, endDate))
                .thenReturn(jpaEntities);

        // Act
        var result = repository.findByPaymentDateBetween(startDate, endDate);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(jpaEntities.size(), result.size());
        verify(jpaRepository).findByPaymentDateBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should save all bills successfully")
    void shouldSaveAllBillsSuccessfully() {
        // Arrange
        var bills = List.of(createTestBill());
        var jpaEntities = List.of(createTestJpaEntity());

        when(jpaRepository.saveAll(anyList())).thenReturn(jpaEntities);

        // Act
        var result = repository.saveAll(bills);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(bills.size(), result.size());
        verify(jpaRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should count bills by due date between and description")
    void shouldCountBillsByDueDateBetweenAndDescription() {
        // Arrange
        var startDate = LocalDate.now();
        var endDate = LocalDate.now().plusDays(30);
        var description = "Test";
        var expectedCount = 5L;

        when(jpaRepository.countByDueDateBetweenAndDescriptionContainingIgnoreCase(
                startDate, endDate, description))
                .thenReturn(expectedCount);

        // Act
        var result = repository.countByDueDateBetweenAndDescription(
                startDate, endDate, description);

        // Assert
        assertEquals(expectedCount, result);
        verify(jpaRepository).countByDueDateBetweenAndDescriptionContainingIgnoreCase(
                startDate, endDate, description);
    }

    private Bill createTestBill() {
        return Bill.create(
                DueDate.of(LocalDate.now().plusDays(30)),
                Amount.of(new BigDecimal("100.0")),
                Description.of("Test Bill")
        );
    }

    private BillJpaEntity createTestJpaEntity() {
        var entity = new BillJpaEntity();
        entity.setId(1L);
        entity.setDueDate(LocalDate.now().plusDays(30));
        entity.setAmount(new BigDecimal("100.0"));
        entity.setDescription("Test Bill");
        entity.setStatus(Status.PENDING);
        entity.setPaymentDate(LocalDate.now());
        return entity;
    }
}