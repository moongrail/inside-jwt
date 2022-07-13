package ru.inside.jwt.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.inside.jwt.dto.MessageDto;
import ru.inside.jwt.dto.ResponseDto;
import ru.inside.jwt.dto.SignUpDto;
import ru.inside.jwt.services.AccountsService;
import ru.inside.jwt.services.DecoderHeaderService;
import ru.inside.jwt.services.MessagesService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final AccountsService accountsService;

    private final MessagesService messagesService;

    private final DecoderHeaderService decoderHeaderService;

    @PostMapping("/api/v1/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseDto> signUp(@RequestBody @Valid SignUpDto signUpDto) {

        Optional<SignUpDto> newAccount = accountsService.saveAccount(signUpDto);

        if (newAccount.isPresent()) {

            log.info("Account " + signUpDto.getName() + " saved.");

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .data("Account '" + signUpDto.getName() + "' created.")
                    .success(true)
                    .build());
        }

        log.error("Error signup account");

        return ResponseEntity.badRequest().body(ResponseDto.builder()
                .error("Form error")
                .success(false)
                .build());
    }

    @PostMapping("/api/v1/messages")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ResponseDto> sendMessage(@RequestHeader("Authorization") String tokenHeader
            , @RequestBody @Valid MessageDto message) {

        //Пустое тело сообщения -> бадреквест
        if (message == null) {
            log.error("Message is empty");
            return ResponseEntity.badRequest().body(ResponseDto.builder()
                    .error("Message form empty")
                    .success(false)
                    .build());
        }


        if (tokenHeader != null && tokenHeader.startsWith("Bearer_")) {

            String name = decoderHeaderService.getNameFromJwt(tokenHeader);

            //Если юзернейм из токена не совпадает с из запроса бадреквест.
            if (!name.equals(message.getName())) {

                log.error("Token name error");

                return ResponseEntity.badRequest().body(ResponseDto.builder()
                        .error("Error name account")
                        .success(false)
                        .build());
            }

            //Если сообщение из реквеста равно хистори 10, выводим список.
            if (message.getMessage().equals("history 10")) {

                List<MessageDto> messages = accountsService.getMessages(name);

                return ResponseEntity.ok().body(ResponseDto.builder()
                        .data(messages)
                        .success(true)
                        .build());
            }

            //Если сообщение из реквеста не равно хистори 10, сохраняем.
            messagesService.save(message);

            return ResponseEntity.ok().body(ResponseDto.builder()
                    .data("Message: '" + message.getMessage() + "' saved.")
                    .success(true)
                    .build());
        }
        //Можно добавить хендлеры.
        log.error("Error created message.");

        return ResponseEntity.badRequest().body(ResponseDto.builder()
                .error("Unknown error")
                .success(false)
                .build());
    }
}
