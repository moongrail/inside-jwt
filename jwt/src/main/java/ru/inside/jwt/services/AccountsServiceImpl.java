package ru.inside.jwt.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inside.jwt.dto.SignUpDto;
import ru.inside.jwt.models.Account;
import ru.inside.jwt.repositories.AccountsRepository;

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
    public String sendMessage(String message) {
        return null;
    }
}
