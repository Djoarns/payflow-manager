package com.github.djoarns.payflow.domain.bill.exceptions;

import com.github.djoarns.payflow.domain.exceptions.BillDomainException;

public class InvalidBillStatusException extends BillDomainException {
    public InvalidBillStatusException(String message) {
        super(message);
    }
}
