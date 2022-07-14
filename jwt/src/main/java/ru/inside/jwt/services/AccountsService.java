package ru.inside.jwt.services;

import ru.inside.jwt.dto.MessageDto;
import ru.inside.jwt.dto.SignUpDto;
import ru.inside.jwt.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountsService {

    Optional<SignUpDto> saveAccount(SignUpDto signUpDto);

    List<MessageDto> getMessages(String name);

}
