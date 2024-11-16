package com.github.djoarns.payflow.domain.user.exception;

import com.github.djoarns.payflow.domain.exception.UserDomainException;

public class InvalidUserDataException extends UserDomainException {
    public InvalidUserDataException(String message) {
        super(message);
    }
}