package ru.inside.jwt.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.inside.jwt.dto.AccountWithMessages;
import ru.inside.jwt.dto.SignUpDto;
import ru.inside.jwt.services.AccountsService;
import ru.inside.jwt.services.DecoderHeaderService;

@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountsService accountsService;
    private final DecoderHeaderService decoderHeaderService;

    @PostMapping("/api/v1/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody SignUpDto signUpDto) {
        accountsService.signUp(signUpDto);
    }

    @PostMapping("/api/v1/messages")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<AccountWithMessages> sendMessage(@RequestHeader("Authorization") String tokenHeader
            , @RequestBody String message) {

        if (message == null) {
            return ResponseEntity.badRequest().build();
        }

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {

            String name = decoderHeaderService.getNameFromJwt(tokenHeader);



            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
