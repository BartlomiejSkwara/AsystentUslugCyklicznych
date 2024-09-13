package com.example.uslugicykliczne.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.
                authorizeHttpRequests(auth ->
                        auth
                            .requestMatchers(
                                    "/api/business/getAll",
                                    "/api/business/get/**",
                                    "/api/business/insertBody"
                                    ).hasAnyRole("user","admin")
                            .requestMatchers(
                                    "/api/serviceUser/getAll",
                                    "/api/serviceUser/get/**",
                                    "/api/serviceUser/insertBody"
                                    ).hasAnyRole("user","admin")
                            .requestMatchers(
                                    "/api/cyclicalservice/getAll",
                                    "/api/cyclicalservice/renew/**",
                                    "/api/cyclicalservice/insertBody"
                                    ).hasAnyRole("user","admin")
                            .anyRequest().hasRole("admin")
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
