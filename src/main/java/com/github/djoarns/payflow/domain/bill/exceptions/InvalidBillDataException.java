package com.github.djoarns.payflow.domain.bill.exceptions;

import com.github.djoarns.payflow.domain.exceptions.BillDomainException;

public class InvalidBillDataException extends BillDomainException {
    public InvalidBillDataException(String message) {
        super(message);
    }
}
