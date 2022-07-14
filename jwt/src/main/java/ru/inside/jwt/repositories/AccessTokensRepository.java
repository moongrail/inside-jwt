package ru.inside.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inside.jwt.models.AccessTokenAccount;

import java.util.Optional;

public interface AccessTokensRepository extends JpaRepository<AccessTokenAccount,Long> {

    boolean existsByAccessToken(String token);

    Optional<AccessTokenAccount> findByAccessToken(String token);
}
