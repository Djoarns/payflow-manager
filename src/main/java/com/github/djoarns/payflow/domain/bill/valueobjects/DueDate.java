package com.github.djoarns.payflow.domain.bill.valueobjects;

import com.github.djoarns.payflow.domain.bill.exceptions.InvalidBillDataException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DueDate {
    LocalDate value;

    public static DueDate of(LocalDate date) {
        if (date == null) {
            throw new InvalidBillDataException("Due date cannot be null");
        }
        return new DueDate(date);
    }
}