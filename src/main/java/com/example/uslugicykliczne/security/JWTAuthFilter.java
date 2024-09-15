package com.example.uslugicykliczne.security;

import com.example.uslugicykliczne.services.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        if(request.getCookies()==null){
            filterChain.doFilter(request,response);
            return;
        }

        Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwt")).findFirst();
        if (jwtCookie.isEmpty()){
            filterChain.doFilter(request,response);
            return;
        }

        final String jwtToken = jwtCookie.get().getValue();
        if(jwtToken==null || jwtToken.isEmpty())
        {
            filterChain.doFilter(request,response);
            return;

        }
        //final String jwtToken  = tokenHeader.substring(7);

        try {
            final String username = jwtService.retrieveUsernameClaim(jwtToken);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            if (username!= null && securityContext.getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(userDetails, jwtToken)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(usernamePasswordAuthenticationToken);


                    String newJwtToken = jwtService.generateJWT(userDetails);
                    jwtService.addTokenToResponse(response,newJwtToken);


                }
            }

        //ExpiredJwtException | SignatureException expiredJwtException
        }catch (Exception e){
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jwtService.removeTokenCookie(response);
            //request.getRequestURI()
        }

        filterChain.doFilter(request,response);


    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/css") || path.startsWith("/images") || path.startsWith("/js");
    }




}