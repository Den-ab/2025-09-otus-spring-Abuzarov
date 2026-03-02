package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OtusUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.userService.findByLogin(username)
            .map(user -> User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
                .build()
            )
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }
}
