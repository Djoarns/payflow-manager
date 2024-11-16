package com.github.djoarns.payflow.domain.user;

import com.github.djoarns.payflow.domain.user.valueobject.Password;
import com.github.djoarns.payflow.domain.user.valueobject.UserId;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import com.github.djoarns.payflow.infrastructure.persistence.entity.UserJpaEntity;
import com.github.djoarns.payflow.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;

    @Override
    public User save(User user) {
        var entity = toJpaEntity(user);
        var savedEntity = jpaRepository.save(entity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return jpaRepository.findByUsername(username.getValue())
                .map(this::toDomainEntity);
    }

    @Override
    public boolean existsByUsername(Username username) {
        return jpaRepository.existsByUsername(username.getValue());
    }

    private UserJpaEntity toJpaEntity(User user) {
        var entity = new UserJpaEntity();
        if (user.getId() != null) {
            entity.setId(user.getId().getValue());
        }
        entity.setUsername(user.getUsername().getValue());
        entity.setPassword(user.getPassword().getValue());
        entity.setRoles(user.getRoles());
        entity.setEnabled(user.isEnabled());
        return entity;
    }

    private User toDomainEntity(UserJpaEntity entity) {
        return User.reconstitute(
                UserId.of(entity.getId()),
                Username.of(entity.getUsername()),
                Password.ofHashed(entity.getPassword()),
                entity.getRoles(),
                entity.isEnabled()
        );
    }
}