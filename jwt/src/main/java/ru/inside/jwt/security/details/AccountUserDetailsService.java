package ru.inside.jwt.security.details;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.inside.jwt.repositories.AccountsRepository;

@AllArgsConstructor
@Service
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountsRepository accountsRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return new AccountUserDetails(
                accountsRepository.findByName(name)
                        .orElseThrow(
                                () -> new UsernameNotFoundException("Account not found")));
    }
}
