package com.example.uslugicykliczne.configuration;

import com.example.uslugicykliczne.security.CsrfCookieFilter;
import com.example.uslugicykliczne.security.JPAUserDetailsService;
import com.example.uslugicykliczne.security.JWTAuthFilter;
import com.example.uslugicykliczne.security.SPATokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
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
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
//@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final JPAUserDetailsService userDetailsService;
    private final JWTAuthFilter jwtAuthFilter;
    private final CsrfCookieFilter csrfCookieFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        final CookieCsrfTokenRepository repo = CookieCsrfTokenRepository.withHttpOnlyFalse();

        repo.setCookieCustomizer((x) ->
                x
                    .sameSite(Cookie.SameSite.LAX.attributeValue())

        );

        httpSecurity.csrf(httpSecurityCsrfConfigurer ->
                httpSecurityCsrfConfigurer
                        .ignoringRequestMatchers(
                            "/api/authentication/login",
                            "/api/authentication/logout",
                                "/api/cyclicalservice/**"
                                )
                        .csrfTokenRepository(repo)
                        .csrfTokenRequestHandler(new SPATokenRequestHandler())
                        .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
        );
        httpSecurity.addFilterAfter(csrfCookieFilter, BasicAuthenticationFilter.class);



        httpSecurity.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeHttpRequests(auth ->
                auth
                    .requestMatchers("/","/index.html", "/js/*","/css/*","/favicon.ico").permitAll()
                    .requestMatchers("/api/authentication/login").permitAll()
                    .requestMatchers("/api/authentication/logout").hasAnyRole("user","editor","admin")
                    .requestMatchers(
                            "/api/cyclicalservice/getAllAwaiting?days=${days}",
                            "/api/cyclicalservice/getAllAwaiting",
                            "/api/cyclicalservice/getAll",
                            "/api/cyclicalservice/getAllExpired",

                            "/api/cyclicalservice/getAllByUser",
                            "/api/cyclicalservice/getAllByBusiness",
                            "/api/cyclicalservice/renewalRequest/*",
                            "/api/cyclicalservice/cancelRequest/*",
                            "/api/cyclicalservice/statusChangeHistory/*",
                            "/api/cyclicalservice/certificateHistory/*",
                            "/api/authentication/requestToken"
                            ).hasAnyRole("user","editor","admin")

                    .requestMatchers(
                            "/api/cyclicalservice/getAllCancelRequests",
                            "/api/cyclicalservice/renew/**",
                            "/api/cyclicalservice/insertBody",
                            "/api/cyclicalservice/statusChange/*",
                            "/api/serviceUser/getAll",
                            "/api/serviceUser/get/**",
                            "/api/serviceUser/insertBody",
                            "/api/business/update/*",
                            "/api/serviceUser/update/*",
                            "/api/business/getAll",
                            "/api/business/get/**",
                            "/api/business/insertBody",
                            "/api/business/getAllByUser",
                            "/api/accountData/getAll",
                            "/api/cyclicalservice/ignore/**",
//                            "/api/accountData/register",
                            "/api/accountData/changeUserRole"
                            ).hasAnyRole("editor","admin")

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
