package ru.inside.jwt.services;

import ru.inside.jwt.dto.MessageDto;
import ru.inside.jwt.dto.SignUpDto;

import java.util.List;

public interface AccountsService {

    void signUp(SignUpDto signUpDto);

    String addMessage(String message);

    List<MessageDto> getMessages();

}
