package com.github.djoarns.payflow.domain.bill.valueobject;

import com.github.djoarns.payflow.domain.bill.exception.InvalidBillDataException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Description {
    String value;

    public static Description of(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidBillDataException("Description cannot be empty");
        }
        if (description.length() > 255) {
            throw new InvalidBillDataException("Description cannot be longer than 255 characters");
        }
        return new Description(description.trim());
    }
}