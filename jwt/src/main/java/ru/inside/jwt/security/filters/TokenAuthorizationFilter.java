package ru.inside.jwt.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.inside.jwt.models.Account;
import ru.inside.jwt.repositories.AccessTokensRepository;
import ru.inside.jwt.repositories.AccountsRepository;
import ru.inside.jwt.security.config.SecurityConfiguration;
import ru.inside.jwt.security.details.AccountUserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    private final static String SECRET_KEY = "secret_key_34231";

    private final AccountsRepository accountsRepository;
    private final AccessTokensRepository accessTokensRepository;
    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().equals(SecurityConfiguration.LOGIN_FILTER_PROCESSES_URL) ||
                request.getRequestURI().equals(SecurityConfiguration.SIGN_UP_FILTER_PROCESSES_URL)) {
            filterChain.doFilter(request, response);
        } else {
            String tokenHeader = request.getHeader("Authorization");

            if (tokenHeader != null && tokenHeader.startsWith("Bearer_")) {

                String token = tokenHeader.substring("Bearer_".length());

                if (!accessTokensRepository.existsByAccessToken(token)) {
                    logger.warn("Token is not exist");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    objectMapper.writeValue(response.getWriter(), Collections.singletonMap("error", "user not found with token"));
                }else {

                    Optional<Account> account = getAccount(token);

                    if (account.isPresent()) {
                        AccountUserDetails userDetails = new AccountUserDetails(account.get());
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(token, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    } else {
                        logger.warn("Wrong token");
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        objectMapper.writeValue(response.getWriter(), Collections.singletonMap("error", "user not found with token"));
                    }
                }

            } else {
                logger.warn("Token is missing");
                filterChain.doFilter(request, response);
            }
        }
    }

    private Optional<Account> getAccount(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build().verify(token);
        return Optional.ofNullable(Account.builder()
                .name((String.valueOf(decodedJWT.getClaim("name"))
                        .replace("\"", "")))
                .build());
    }

}
