package com.github.djoarns.payflow.domain.user;

import com.github.djoarns.payflow.domain.user.valueobject.Username;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUsername(Username username);
    boolean existsByUsername(Username username);
}