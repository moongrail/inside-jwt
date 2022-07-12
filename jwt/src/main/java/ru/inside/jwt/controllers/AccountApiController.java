package ru.inside.jwt.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.inside.jwt.dto.ResponseMessages;
import ru.inside.jwt.dto.SignUpDto;
import ru.inside.jwt.services.AccountsService;

@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountsService accountsService;

    @PostMapping("/api/v1/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody SignUpDto signUpDto){
        accountsService.signUp(signUpDto);
    }
    @PostMapping("/api/v1/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseMessages> sendMessage(@RequestBody ResponseMessages messages){

        if (messages == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(ResponseMessages.builder()
                .name(messages.getName())
                .message(messages.getMessage())
                .build());
    }

}
