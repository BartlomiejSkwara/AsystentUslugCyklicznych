package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.entity.AccountDataEntity;
import com.example.uslugicykliczne.security.CustomUserDetails;
import com.example.uslugicykliczne.utility.TimeUtility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.token.key}")
    private String key;
    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public String retrieveUsernameClaim(String jwtToken){
        return retrieveSpecificClaim(jwtToken,Claims::getSubject);
    }
    public <T> T retrieveSpecificClaim(String jwtToken, Function<Claims,T> claimsResolver) {
        final Claims claims = retrieveAllClaims(jwtToken);
        return claimsResolver.apply(claims);

    }
    public Claims retrieveAllClaims(String jwtToken){
        return Jwts.parser().verifyWith(getSecretKey()).build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public boolean isTokenValid(UserDetails userDetails, String jwtToken) {
        return (userDetails.getUsername().equals(retrieveUsernameClaim(jwtToken)) && isTokenNonExpired(jwtToken));
    }

    private boolean isTokenNonExpired(String jwtToken) {
        return (retrieveExpirationDateClaim(jwtToken).after(TimeUtility.getCurrentDate()));
    }

    private Date retrieveExpirationDateClaim(String jwtToken) {
        return  retrieveSpecificClaim(jwtToken,Claims::getExpiration);
    }

    public String generateJWT(Map<String,Object> stringClaimsMap, UserDetails userDetails){
        Date now = TimeUtility.getCurrentDate();
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.claim("frontPerm",((CustomUserDetails)userDetails).getRole());
        if (stringClaimsMap != null){
            jwtBuilder.claims(stringClaimsMap);
        }

        return jwtBuilder
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(Date.from(now.toInstant().plusSeconds(30*60)))
                .signWith(getSecretKey())
                .compact();

    }
    public String generateJWT(UserDetails userDetails){
        return  generateJWT(null, userDetails);
    }
    public String generateJWT(AccountDataEntity accountDataEntity){
        return  generateJWT(null, new CustomUserDetails(accountDataEntity));
    }

    public void addTokenToResponse(HttpServletResponse httpServletResponse, String token){
        Cookie cookie = new Cookie("jwt",token);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite","Strict");
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    public void removeTokenCookie(HttpServletResponse httpServletResponse){
        Cookie cookie = new Cookie("jwt","");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }

}