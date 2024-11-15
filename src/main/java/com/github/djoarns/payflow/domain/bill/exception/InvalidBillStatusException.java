package com.github.djoarns.payflow.domain.bill.exception;

import com.github.djoarns.payflow.domain.exception.BillDomainException;

public class InvalidBillStatusException extends BillDomainException {
    public InvalidBillStatusException(String message) {
        super(message);
    }
}
