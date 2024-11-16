package com.github.djoarns.payflow.infrastructure.persistence.repository;

import com.github.djoarns.payflow.infrastructure.persistence.entity.BillJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillJpaRepository extends JpaRepository<BillJpaEntity, Long> {
    List<BillJpaEntity> findByDueDateBetweenAndDescriptionContainingIgnoreCase(
            LocalDate startDate,
            LocalDate endDate,
            String description,
            Pageable pageable
    );

    List<BillJpaEntity> findByPaymentDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    long countByDueDateBetweenAndDescriptionContainingIgnoreCase(
            LocalDate startDate,
            LocalDate endDate,
            String description
    );
}