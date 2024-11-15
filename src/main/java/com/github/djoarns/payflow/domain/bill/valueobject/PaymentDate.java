package com.github.djoarns.payflow.domain.bill.valueobject;

import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentDate {
    LocalDate value;

    public static PaymentDate of(LocalDate date) {
        if (date == null) {
            throw new InvalidBillDataException("Payment date cannot be null");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidBillDataException("Payment date cannot be in the future");
        }
        return new PaymentDate(date);
    }
}
