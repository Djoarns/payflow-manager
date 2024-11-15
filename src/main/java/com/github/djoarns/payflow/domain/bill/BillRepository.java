package com.github.djoarns.payflow.domain.bill;

import com.github.djoarns.payflow.domain.bill.valueobjects.BillId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillRepository {
    Bill save(Bill bill);

    Optional<Bill> findById(BillId id);

    List<Bill> findByDueDateBetweenAndDescription(
        LocalDate startDate,
        LocalDate endDate,
        String description,
        int page,
        int size
    );

    List<Bill> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    List<Bill> saveAll(List<Bill> bills);

    long countByDueDateBetweenAndDescription(
        LocalDate startDate,
        LocalDate endDate,
        String description
    );
}
