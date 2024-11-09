package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.LoginValidationRecord;
//import com.example.uslugicykliczne.dataTypes.RegistrationValidationRecord;
import com.example.uslugicykliczne.services.AccountManagementService;
import com.example.uslugicykliczne.services.ActivityTrackerService;
import com.example.uslugicykliczne.services.JWTService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/api/authentication")
@AllArgsConstructor
public class AuthenticationController {
    private final ValidationUtility validationUtility;
    private final JWTService jwtService;
    private final AccountManagementService accountManagementService;
    private final ActivityTrackerService activityTrackerService;

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
        activityTrackerService.addUserActivity(loginValidationRecord.login(), LocalDateTime.now());

        return ResponseEntity.ok("Successfully logged in ");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse httpServletResponse){

        activityTrackerService.removeUserActivity(SecurityContextHolder.getContext().getAuthentication().getName());

        jwtService.addTokenToResponse(httpServletResponse, "");
        httpServletResponse.setHeader("frontRole","");
        return ResponseEntity.ok("Z powodzeniem wylogowano użytkownika");
    }




    @GetMapping("/getRecentActivities")
    public HashMap<String, Integer> getRecentActivities(HttpServletResponse httpServletResponse){

        return activityTrackerService.getCalculatedMinutesActivities() ;
    }

    @PostMapping("/forceLogout")
    public ResponseEntity<?> forceLogout(
            @RequestParam(required = true) String login,
            HttpServletResponse httpServletResponse){
        activityTrackerService.forceLogout(login);
        return ResponseEntity.ok("Z powodzeniem wymuszono wylogowanie");
    }



}
