package com.github.djoarns.payflow.domain.user.exception;

import com.github.djoarns.payflow.domain.exception.UserDomainException;

public class InvalidUserOperationException extends UserDomainException {
    public InvalidUserOperationException(String message) {
        super(message);
    }
}