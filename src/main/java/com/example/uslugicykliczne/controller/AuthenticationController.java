package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.LoginValidationRecord;
import com.example.uslugicykliczne.dataTypes.RegistrationValidationRecord;
import com.example.uslugicykliczne.entity.AccountDataEntity;
import com.example.uslugicykliczne.security.CustomUserDetails;
import com.example.uslugicykliczne.services.AccountManagementService;
import com.example.uslugicykliczne.services.JWTService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authentication")
@AllArgsConstructor
public class AuthenticationController {
    private final ValidationUtility validationUtility;
    private final JWTService jwtService;
    private final AccountManagementService accountManagementService;

    @GetMapping("/requestToken")
    public ResponseEntity<String> requestToken(){
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody() LoginValidationRecord loginValidationRecord, BindingResult bindingResult, HttpServletResponse httpServletResponse){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        accountManagementService.login(loginValidationRecord,httpServletResponse);


        return ResponseEntity.ok("Successfully logged in ");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse httpServletResponse){
        jwtService.addTokenToResponse(httpServletResponse, "");
        httpServletResponse.setHeader("frontRole","");
        return ResponseEntity.ok("Logging out was successfull");
    }





}
