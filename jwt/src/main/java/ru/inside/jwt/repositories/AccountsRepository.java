package ru.inside.jwt.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.inside.jwt.models.Account;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByName(String name);

}
