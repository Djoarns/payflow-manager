package com.github.djoarns.payflow.domain.user.valueobject;

import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserId {
    Long value;

    public static UserId of(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidUserDataException("Invalid user ID");
        }
        return new UserId(id);
    }
}