package ru.inside.jwt.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inside.jwt.dto.MessageDto;
import ru.inside.jwt.dto.SignUpDto;
import ru.inside.jwt.models.Account;
import ru.inside.jwt.models.Message;
import ru.inside.jwt.repositories.AccountsRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.inside.jwt.dto.MessageDto.from;

@AllArgsConstructor
@Service
@Slf4j
public class AccountsServiceImpl implements AccountsService {

    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpDto signUpDto) {

        if (signUpDto == null){
            log.error("Empty data");
        }
        Account account = Account.builder()
                .name(signUpDto.getName())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();

        accountsRepository.save(account);
    }

    @Override
    public String addMessage(String message) {
        return null;
    }

    @Override
    public List<MessageDto> getMessages(String name) {

        Optional<Account> account = accountsRepository.findByName(name);

        List<Message> messageList = account.get().getMessages().stream()
                .sorted(Comparator.comparing(Message::getId).reversed())
                .limit(10)
                .collect(Collectors.toList());

        return from((messageList));
    }
}
