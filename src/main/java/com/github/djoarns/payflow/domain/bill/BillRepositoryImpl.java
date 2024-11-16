package com.github.djoarns.payflow.domain.bill;

import com.github.djoarns.payflow.infrastructure.persistence.entity.BillJpaEntity;
import com.github.djoarns.payflow.infrastructure.persistence.repository.BillJpaRepository;
import com.github.djoarns.payflow.domain.bill.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BillRepositoryImpl implements BillRepository {
    private final BillJpaRepository jPARepository;

    @Override
    public Bill save(Bill bill) {
        var entity = toJpaEntity(bill);
        var savedEntity = jPARepository.save(entity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Bill> findById(BillId id) {
        return jPARepository.findById(id.getValue())
                .map(this::toDomainEntity);
    }

    @Override
    public List<Bill> findByDueDateBetweenAndDescription(
            LocalDate startDate,
            LocalDate endDate,
            String description,
            int page,
            int size
    ) {
        var pageable = PageRequest.of(page, size);
        return jPARepository
                .findByDueDateBetweenAndDescriptionContainingIgnoreCase(
                        startDate,
                        endDate,
                        description != null ? description : "",
                        pageable
                )
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Bill> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate) {
        return jPARepository
                .findByPaymentDateBetween(startDate, endDate)
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Bill> saveAll(List<Bill> bills) {
        var entities = bills.stream()
                .map(this::toJpaEntity)
                .collect(Collectors.toList());
        return jPARepository.saveAll(entities)
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long countByDueDateBetweenAndDescription(
            LocalDate startDate,
            LocalDate endDate,
            String description
    ) {
        return jPARepository.countByDueDateBetweenAndDescriptionContainingIgnoreCase(
                startDate,
                endDate,
                description != null ? description : ""
        );
    }

    private BillJpaEntity toJpaEntity(Bill bill) {
        var entity = new BillJpaEntity();
        if (bill.getId() != null) {
            entity.setId(bill.getId().getValue());
        }
        entity.setDueDate(bill.getDueDate().getValue());
        entity.setAmount(bill.getAmount().getValue());
        entity.setDescription(bill.getDescription().getValue());
        entity.setStatus(bill.getStatus());
        if (bill.getPaymentDate() != null) {
            entity.setPaymentDate(bill.getPaymentDate().getValue());
        }
        return entity;
    }

    private Bill toDomainEntity(BillJpaEntity entity) {
        return Bill.reconstitute(
                BillId.of(entity.getId()),
                DueDate.of(entity.getDueDate()),
                entity.getPaymentDate() != null ? PaymentDate.of(entity.getPaymentDate()) : null,
                Amount.of(entity.getAmount()),
                Description.of(entity.getDescription()),
                entity.getStatus()
        );
    }
}