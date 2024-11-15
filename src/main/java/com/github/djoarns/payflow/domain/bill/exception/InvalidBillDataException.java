package com.github.djoarns.payflow.domain.bill.exception;

import com.github.djoarns.payflow.domain.exception.BillDomainException;

public class InvalidBillDataException extends BillDomainException {
    public InvalidBillDataException(String message) {
        super(message);
    }
}
