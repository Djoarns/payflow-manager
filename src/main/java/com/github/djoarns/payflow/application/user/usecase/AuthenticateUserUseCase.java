package com.github.djoarns.payflow.application.user.usecase;

import com.github.djoarns.payflow.application.user.command.AuthCommand;
import com.github.djoarns.payflow.application.user.result.AuthResult;
import com.github.djoarns.payflow.domain.user.UserRepository;
import com.github.djoarns.payflow.domain.user.exception.InvalidUserDataException;
import com.github.djoarns.payflow.domain.user.valueobject.Username;
import com.github.djoarns.payflow.infrastructure.security.JwtService;
import com.github.djoarns.payflow.infrastructure.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    public AuthResult.Authentication execute(AuthCommand.Login command) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        command.username(),
                        command.password()
                )
        );

        var user = userRepository.findByUsername(Username.of(command.username()))
                .orElseThrow(() -> new InvalidUserDataException("User not found"));

        var jwt = jwtService.generateToken(new UserDetailsImpl(user));
        return new AuthResult.Authentication(jwt, user.getUsername().getValue());
    }
}