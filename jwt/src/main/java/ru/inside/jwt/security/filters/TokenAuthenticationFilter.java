package ru.inside.jwt.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.inside.jwt.dto.SignInDto;
import ru.inside.jwt.models.AccessTokenAccount;
import ru.inside.jwt.models.Account;
import ru.inside.jwt.repositories.AccessTokensRepository;
import ru.inside.jwt.repositories.AccountsRepository;
import ru.inside.jwt.security.details.AccountUserDetails;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final static String SECRET_KEY = "secret_key_34231";
    public static final String TOKEN = "token";

    private final ObjectMapper objectMapper;
    private final AccountsRepository accountsRepository;

    private final AccessTokensRepository accessTokensRepository;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
                                     AccountsRepository accountsRepository, AccessTokensRepository accessTokensRepository) {
        super(authenticationManager);
        this.objectMapper = objectMapper;
        this.accountsRepository = accountsRepository;
        this.accessTokensRepository = accessTokensRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            SignInDto dto = objectMapper.readValue(request.getReader(), SignInDto.class);
            log.info("Attempt authentication - name {}, password {}", dto.getName(), dto.getPassword());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getName(),
                    dto.getPassword());

            return super.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AccountUserDetails userDetails = (AccountUserDetails) authResult.getPrincipal();
        Account account = userDetails.getAccount();
        String accessToken = JWT.create()
                .withSubject(account.getId().toString())
                .withClaim("name", account.getName())
                .sign(Algorithm.HMAC256(SECRET_KEY));

        AccessTokenAccount tokenAccount = AccessTokenAccount.builder()
                .account(account)
                .accessToken(accessToken)
                .build();

        accessTokensRepository.save(tokenAccount);

        account.setToken(tokenAccount);

        accountsRepository.save(account);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), Collections.singletonMap(TOKEN, accessToken));
    }
}
