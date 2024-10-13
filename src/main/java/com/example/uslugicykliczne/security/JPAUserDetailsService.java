package com.example.uslugicykliczne.security;

import com.example.uslugicykliczne.repo.AccountDataRepo;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JPAUserDetailsService implements UserDetailsService {
    private final AccountDataRepo accountDataRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = accountDataRepo.findByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new CustomUserDetails(user.get());
    }


}
