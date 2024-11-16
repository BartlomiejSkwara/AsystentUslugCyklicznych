package com.example.uslugicykliczne.services;

import com.example.uslugicykliczne.dataTypes.LoginValidationRecord;
//import com.example.uslugicykliczne.dataTypes.RegistrationValidationRecord;
import com.example.uslugicykliczne.entity.AccountDataEntity;
import com.example.uslugicykliczne.repo.AccountDataRepo;
import com.example.uslugicykliczne.security.CustomUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountManagementService {

    private final PasswordEncoder passwordEncoder;
    private final AccountDataRepo accountDataRepo;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EntityManager entityManager;

    @Transactional
    public AccountDataEntity register(String password, String login) {
        if(accountDataRepo.findByUsername(login).isPresent()){
            return null;
        }
        AccountDataEntity accountDataEntity = new AccountDataEntity();

        String role = "ROLE_user";
//        switch (registrationValidationRecord.role()){
//            case 1:
//                role = "ROLE_editor";
//                break;
//            case 2:
//                role = "ROLE_admin";
//                break;
//            default:
//                role = "ROLE_user";
//                break;
//        }

        accountDataEntity.setRole(role);
        accountDataEntity.setUsername(login);
        accountDataEntity.setHashedPassword(passwordEncoder.encode(password));


        return  accountDataRepo.save(accountDataEntity);
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
        httpServletResponse.setHeader("username", userDetails.getUsername());

    }

    public ResponseEntity<?> changeUserRole(Integer accountId, String role) {
        Optional<AccountDataEntity> optionalAccountDataEntity = accountDataRepo.findById(accountId);
        if(optionalAccountDataEntity.isEmpty()){
            return ResponseEntity.badRequest().body("Nie można zmienić roli nie istniejącego użytkownika");
        }
        AccountDataEntity accountDataEntity = optionalAccountDataEntity.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientRole = ((CustomUserDetails) authentication.getPrincipal()).getRole();

        if(accountDataEntity.getRole().equals("ROLE_admin")&&!clientRole.equals("ROLE_admin")){
            return ResponseEntity.badRequest().body("Nie można zmienić roli użytkownika o wyższej roli");
        }

        if(role.equals("ROLE_user")||role.equals("ROLE_editor")){
        }
        else if (role.equals("ROLE_admin")) {
            if(!clientRole.equals("ROLE_admin")){
                return ResponseEntity.badRequest().body("Nie można ustawić roli przekraczającej nasze uprawnienia");
            }
        } else {
            return ResponseEntity.badRequest().body("Podana rola nie istnieje");
        }


        if(accountDataEntity.getRole().equals("ROLE_admin")){
            Query u  = entityManager.createQuery("SELECT COUNT(a) FROM AccountDataEntity a WHERE a.role = :role ");
            u.setParameter("role","ROLE_admin");
            Long count = (Long) u.getSingleResult();
            if(count <= 1){
                return ResponseEntity.badRequest().body("Nie można przeprowadzać operacji zmiany roli na adminie gdy jest tylko jeden admin !!!");
            }

        }
        accountDataEntity.setRole(role);
        accountDataRepo.save(accountDataEntity);
        return ResponseEntity.ok("Z powodzeniem dokonano zmiany roli");

    }
}
