package com.github.djoarns.payflow.domain.bill.valueobjects;

import com.github.djoarns.payflow.domain.bill.exceptions.InvalidBillDataException;
import com.github.djoarns.payflow.domain.bill.exceptions.InvalidBillOperationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Amount {
    BigDecimal value;

    public static Amount of(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidBillDataException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBillDataException("Amount must be greater than zero");
        }
        return new Amount(amount);
    }

    public Amount add(Amount other) {
        if (other == null) {
            throw new InvalidBillOperationException("Cannot add null amount");
        }
        return new Amount(this.value.add(other.value));
    }

    public Amount subtract(Amount other) {
        if (other == null) {
            throw new InvalidBillOperationException("Cannot subtract null amount");
        }
        return new Amount(this.value.subtract(other.value));
    }
}