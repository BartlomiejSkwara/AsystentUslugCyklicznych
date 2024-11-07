package com.example.uslugicykliczne.controller;


import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.ServiceUserDTO;
import com.example.uslugicykliczne.dataTypes.ServiceUserProjection;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import com.example.uslugicykliczne.repo.ServiceUserRepo;
import com.example.uslugicykliczne.services.ServiceUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/serviceUser")
public class ServiceUserController {

    private final ServiceUserRepo serviceUserRepo;
    private final ServiceUserService serviceUserService;
    private final ValidationUtility validationUtility;



    ServiceUserController(ServiceUserRepo serviceUserRepo, ServiceUserService serviceUserService, ValidationUtility validationUtility){
        this.serviceUserRepo = serviceUserRepo;
        this.serviceUserService = serviceUserService;
        this.validationUtility = validationUtility;
    }





    @GetMapping("/getAll")
    public List<ServiceUserProjection> getAllCustomers(){

        return serviceUserRepo.findUsersWithProjectedContactData();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ServiceUserProjection> getCustomer (@PathVariable Integer id ){
        Optional<ServiceUserEntity> soughtEntity = serviceUserRepo.findUserWithContactDataById(id);
        if(soughtEntity.isPresent()){
            ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
            ServiceUserProjection sp = pf.createProjection(ServiceUserProjection.class, soughtEntity.get());
            return ResponseEntity.ok(sp);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable Integer id ){
        serviceUserService.deleteServiceUser(id);
        return ResponseEntity.ok().body("Z powodzeniem usunięto użytkownika");

    }


    @PostMapping("/insertBody")
    public ResponseEntity<String> insert(
            @RequestParam(defaultValue = "true", required = false) String checkForDuplicates,
            @Valid @RequestBody() ServiceUserDTO serviceUserDTO,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }

        return serviceUserService.insertNewServiceUserEntity(serviceUserDTO,!checkForDuplicates.equals("false"));
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(
            @RequestParam(defaultValue = "true", required = false) String checkForDuplicates,
            @PathVariable Integer id,@Valid @RequestBody() ServiceUserDTO serviceUserDTO,
            BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return serviceUserService.updateServiceUserEntity(id, serviceUserDTO,!checkForDuplicates.equals("false"));
    }

}
