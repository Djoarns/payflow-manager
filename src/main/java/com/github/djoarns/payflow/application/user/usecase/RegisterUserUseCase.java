package com.github.djoarns.payflow.application.user.usecase;

import com.github.djoarns.payflow.application.user.command.AuthCommand;
import com.github.djoarns.payflow.application.user.result.AuthResult;
import com.github.djoarns.payflow.domain.user.User;
import com.github.djoarns.payflow.domain.user.UserRepository;
import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import com.github.djoarns.payflow.domain.user.valueobject.Password;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import com.github.djoarns.payflow.infrastructure.security.JwtService;
import com.github.djoarns.payflow.infrastructure.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResult.Authentication execute(AuthCommand.Register command) {
        var username = Username.of(command.username());

        if (userRepository.existsByUsername(username)) {
            throw new InvalidUserDataException("Username already exists");
        }

        var user = User.create(
                username,
                Password.of(passwordEncoder.encode(command.password()))
        );

        user = userRepository.save(user);

        var jwt = jwtService.generateToken(new UserDetailsImpl(user));
        return new AuthResult.Authentication(jwt, user.getUsername().getValue());
    }
}