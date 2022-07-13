package ru.inside.jwt.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.inside.jwt.repositories.AccessTokensRepository;

@Service
@AllArgsConstructor
public class DecoderHeaderServiceImpl implements DecoderHeaderService {

    private final static String SECRET_KEY = "secret_key_34231";

    private AccessTokensRepository accessTokensRepository;

    @Override
    public String getNameFromJwt(String token) {

        String header = token.substring("Bearer_".length());

        if (accessTokensRepository.existsByAccessToken(header)) {

            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build().verify(header);

            return String.valueOf(decodedJWT.getClaim("name")).replace("\"", "");
        }

        return "err";
    }
}
