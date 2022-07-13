package ru.inside.jwt.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.inside.jwt.dto.MessageDto;
import ru.inside.jwt.models.Account;
import ru.inside.jwt.models.Message;
import ru.inside.jwt.repositories.AccountsRepository;
import ru.inside.jwt.repositories.MessagesRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessagesServiceImpl implements MessagesService {

    private final MessagesRepository messagesRepository;

    private final AccountsRepository accountsRepository;

    @Override
    public void save(MessageDto message, String name) {

        Optional<Account> account = accountsRepository.findByName(name);

        if (account.isPresent()) {

            Message newMessage = Message.builder()
                    .account(account.get())
                    .name(name)
                    .message(message.getMessage())
                    .timestamp(System.currentTimeMillis())
                    .build();

            messagesRepository.save(newMessage);

        }
    }
}
