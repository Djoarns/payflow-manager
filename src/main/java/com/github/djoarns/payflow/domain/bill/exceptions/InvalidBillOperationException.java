package com.github.djoarns.payflow.domain.bill.exceptions;

import com.github.djoarns.payflow.domain.exceptions.BillDomainException;

public class InvalidBillOperationException extends BillDomainException {
    public InvalidBillOperationException(String message) {
        super(message);
    }
}