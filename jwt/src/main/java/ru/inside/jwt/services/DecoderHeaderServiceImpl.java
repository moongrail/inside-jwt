package ru.inside.jwt.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

@Service
public class DecoderHeaderServiceImpl implements DecoderHeaderService {

    private final static String SECRET_KEY = "secret_key_34231";

    @Override
    public String getNameFromJwt(String token) {

        String header = token.substring("Bearer_".length());
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build().verify(header);

        String name = String.valueOf(decodedJWT.getClaim("name")).replace("\"", "");

        return name;
    }
}
