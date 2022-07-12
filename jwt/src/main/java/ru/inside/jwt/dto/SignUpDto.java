package ru.inside.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.inside.jwt.models.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private String name;

    private String password;

    public SignUpDto from(Account account) {
        return SignUpDto.builder()
                .name(account.getName())
                .password(account.getPassword())
                .build();
    }
}
