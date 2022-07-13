package ru.inside.jwt.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.inside.jwt.dto.AccountWithMessages;
import ru.inside.jwt.dto.MessageDto;
import ru.inside.jwt.dto.SignUpDto;
import ru.inside.jwt.services.AccountsService;
import ru.inside.jwt.services.DecoderHeaderService;
import ru.inside.jwt.services.MessagesService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountsService accountsService;

    private final MessagesService messagesService;
    private final DecoderHeaderService decoderHeaderService;

    @PostMapping("/api/v1/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody SignUpDto signUpDto) {

        if (signUpDto != null){
            accountsService.signUp(signUpDto);
        }

    }

    @PostMapping("/api/v1/messages")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<MessageDto>> sendMessage(@RequestHeader("Authorization") String tokenHeader
            , @RequestBody MessageDto message) {

        if (message == null) {
            return ResponseEntity.badRequest().build();
        }

        if (tokenHeader != null && tokenHeader.startsWith("Bearer_")) {

            String name = decoderHeaderService.getNameFromJwt(tokenHeader);

            if (!name.equals(message.getName())){
                return ResponseEntity.badRequest().build();
            }

            if (message.getMessage().equals("history 10")){

                List<MessageDto> messages = accountsService.getMessages(name);

                return ResponseEntity.ok().body(messages);
            }

            messagesService.save(message,name);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
