package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.RegistrationValidationRecord;
import com.example.uslugicykliczne.dataTypes.projections.AccountDataProjection;
import com.example.uslugicykliczne.repo.AccountDataRepo;
import com.example.uslugicykliczne.services.AccountManagementService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accountData")
@AllArgsConstructor
public class AccountDataController {
    private final AccountDataRepo accountDataRepo;
    private final AccountManagementService accountManagementService;
    private final ValidationUtility validationUtility;


    @GetMapping("/getAll")
    List<AccountDataProjection> getAllUsers(){
        return  accountDataRepo.findAllBy();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationValidationRecord registrationValidationRecord, BindingResult bindingResult, HttpServletResponse httpServletResponse){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return accountManagementService.register(registrationValidationRecord,httpServletResponse);

    }


}
