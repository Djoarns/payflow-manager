package com.github.djoarns.payflow.domain.user.valueobject;

import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Username {
    String value;

    public static Username of(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUserDataException("Username cannot be empty");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new InvalidUserDataException("Username must be between 3 and 50 characters");
        }
        return new Username(username.trim().toLowerCase());
    }
}