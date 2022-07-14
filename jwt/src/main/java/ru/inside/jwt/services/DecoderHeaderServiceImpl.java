package ru.inside.jwt.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.inside.jwt.repositories.AccessTokensRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DecoderHeaderServiceImpl implements DecoderHeaderService {

    private final static String SECRET_KEY = "secret_key_34231";

    private AccessTokensRepository accessTokensRepository;

    @Override
    public Optional<String> getNameFromJwt(String token) {
        //Обрезка ненужной части токена

        String header = token.substring("Bearer_".length());

        if (accessTokensRepository.existsByAccessToken(header)) {
            //декодирование токена
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build().verify(header);
            //обрезаем ненужное
            return Optional.of(String.valueOf(decodedJWT
                    .getClaim("name"))
                    .replace("\"", ""));
        }

        return Optional.empty();
    }
}
