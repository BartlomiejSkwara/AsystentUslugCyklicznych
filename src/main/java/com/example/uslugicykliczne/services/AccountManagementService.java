package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.LoginValidationRecord;
import com.example.uslugicykliczne.dataTypes.RegistrationValidationRecord;
import com.example.uslugicykliczne.entity.AccountDataEntity;
import com.example.uslugicykliczne.repo.AccountDataRepo;
import com.example.uslugicykliczne.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountManagementService {

    private final PasswordEncoder passwordEncoder;
    private final AccountDataRepo accountDataRepo;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public void register(RegistrationValidationRecord registrationValidationRecord, HttpServletResponse httpServletResponse) {
        AccountDataEntity accountDataEntity = new AccountDataEntity();
        accountDataEntity.setRole("ROLE_user");
        accountDataEntity.setUsername(registrationValidationRecord.login());
        accountDataEntity.setHashedPassword(passwordEncoder.encode(registrationValidationRecord.password()));


        accountDataRepo.save(accountDataEntity);
//        String jwtToken = jwtService.generateJWT(accountDataEntity);
//        jwtService.addTokenToResponse(httpServletResponse,jwtToken);
//        login(,httpServletResponse);
//
//        try{
//            httpServletRequest.login(registerForm.getLogin(),registerForm.getPassword());
//        }
//        catch ( ServletException e){
//            return false;
//        }
//
//        return true;
    }

    public void login(LoginValidationRecord loginValidationRecord,HttpServletResponse httpServletResponse){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginValidationRecord.login(), loginValidationRecord.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt =  jwtService.generateJWT(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        jwtService.addTokenToResponse(httpServletResponse, jwt);
        httpServletResponse.setHeader("frontRole",jwtService.retrieveSpecificClaim(jwt,claims -> claims.get("frontPerm")).toString());

    }

}
