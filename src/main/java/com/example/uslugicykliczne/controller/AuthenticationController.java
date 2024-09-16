package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.BusinessProjection;
import com.example.uslugicykliczne.dataTypes.LoginValidationRecord;
import com.example.uslugicykliczne.entity.BusinessEntity;
import com.example.uslugicykliczne.security.CustomUserDetails;
import com.example.uslugicykliczne.security.JPAUserDetailsService;
import com.example.uslugicykliczne.services.JWTService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authentication")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final ValidationUtility validationUtility;
    private final JWTService jwtService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody() LoginValidationRecord loginValidationRecord, BindingResult bindingResult, HttpServletResponse httpServletResponse){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginValidationRecord.login(), loginValidationRecord.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt =  jwtService.generateJWT(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        jwtService.addTokenToResponse(httpServletResponse, jwt);

        return ResponseEntity.ok("Successfully logged in ");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse httpServletResponse){
        jwtService.addTokenToResponse(httpServletResponse, "");
        return ResponseEntity.ok("Logging out was successfull");
    }



}
