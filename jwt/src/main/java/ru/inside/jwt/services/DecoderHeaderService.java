package ru.inside.jwt.services;

import java.util.Optional;

public interface DecoderHeaderService {
    Optional<String> getNameFromJwt(String token);
}
