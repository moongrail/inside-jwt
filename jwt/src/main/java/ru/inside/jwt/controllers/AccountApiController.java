package ru.inside.jwt.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.inside.jwt.dto.SignUpDto;
import ru.inside.jwt.services.AccountsService;

@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountsService accountsService;

    @PostMapping("/signUp/api/v1/createAccount")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody SignUpDto signUpDto){
        accountsService.signUp(signUpDto);
    }

}
