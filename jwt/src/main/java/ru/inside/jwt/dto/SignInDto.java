package ru.inside.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInDto {

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String password;
}
