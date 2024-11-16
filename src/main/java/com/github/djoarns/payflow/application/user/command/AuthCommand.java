package com.github.djoarns.payflow.application.user.command;

public sealed interface AuthCommand permits
        AuthCommand.Register,
        AuthCommand.Login {

    record Register(
            String username,
            String password
    ) implements AuthCommand {}

    record Login(
            String username,
            String password
    ) implements AuthCommand {}
}