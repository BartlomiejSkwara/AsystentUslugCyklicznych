package com.example.uslugicykliczne.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
//@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final JPAUserDetailsService userDetailsService;
    private final JWTAuthFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(httpSecurityCsrfConfigurer ->
                httpSecurityCsrfConfigurer.disable()
        );

        httpSecurity.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeHttpRequests(auth ->
                auth
                    .requestMatchers("/api/authentication/login").permitAll()
                    .requestMatchers("/api/authentication/logout").hasAnyRole("user","admin")
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
                );
        //httpSecurity.authenticationProvider(authenticationProvider());

        return httpSecurity.build();

    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
