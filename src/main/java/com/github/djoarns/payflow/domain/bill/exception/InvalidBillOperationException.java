package com.github.djoarns.payflow.domain.bill.exception;

import com.github.djoarns.payflow.domain.exception.BillDomainException;

public class InvalidBillOperationException extends BillDomainException {
    public InvalidBillOperationException(String message) {
        super(message);
    }
}