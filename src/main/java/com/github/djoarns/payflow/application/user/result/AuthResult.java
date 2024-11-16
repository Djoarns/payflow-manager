package com.github.djoarns.payflow.application.user.result;

public sealed interface AuthResult permits AuthResult.Authentication {
    record Authentication(
            String token,
            String username
    ) implements AuthResult {}
}