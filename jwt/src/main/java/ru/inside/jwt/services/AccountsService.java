package ru.inside.jwt.services;

import ru.inside.jwt.dto.SignInDto;
import ru.inside.jwt.dto.SignUpDto;

public interface AccountsService {

    void signUp(SignUpDto signUpDto);

    String sendMessage(String message);

    void signIn(SignInDto signInDto);
}
