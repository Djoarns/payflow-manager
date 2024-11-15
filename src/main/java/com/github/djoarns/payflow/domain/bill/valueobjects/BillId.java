package com.github.djoarns.payflow.domain.bill.valueobjects;

import com.github.djoarns.payflow.domain.bill.exceptions.InvalidBillDataException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BillId {
    Long value;

    public static BillId of(Long id) {
        if (id == null) {
            throw new InvalidBillDataException("Bill ID cannot be null");
        }
        if (id <= 0) {
            throw new InvalidBillDataException("Bill ID must be greater than zero");
        }
        return new BillId(id);
    }

    public static BillId of(String id) {
        try {
            return of(Long.parseLong(id));
        } catch (NumberFormatException e) {
            throw new InvalidBillDataException("Invalid Bill ID format");
        }
    }
}