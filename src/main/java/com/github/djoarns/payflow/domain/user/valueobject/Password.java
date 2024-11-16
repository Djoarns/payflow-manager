package com.github.djoarns.payflow.domain.user.valueobject;

import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {
    String value;

    public static Password of(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidUserDataException("Password cannot be empty");
        }
        if (password.length() < 6) {
            throw new InvalidUserDataException("Password must be at least 6 characters");
        }
        return new Password(password);
    }

    public static Password ofHashed(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new InvalidUserDataException("Hashed password cannot be empty");
        }
        return new Password(hashedPassword);
    }
}
