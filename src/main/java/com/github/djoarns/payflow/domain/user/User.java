package com.github.djoarns.payflow.domain.user;

import com.github.djoarns.payflow.domain.user.valueobject.Password;
import com.github.djoarns.payflow.domain.user.valueobject.UserId;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private final UserId id;
    private final Username username;
    private final Password password;
    private final Set<Role> roles;
    private final boolean enabled;

    public static User create(Username username, Password password) {
        return new User(null, username, password, Set.of(Role.USER), true);
    }

    public static User reconstitute(UserId id, Username username, Password password, Set<Role> roles, boolean enabled) {
        return new User(id, username, password, roles, enabled);
    }
}