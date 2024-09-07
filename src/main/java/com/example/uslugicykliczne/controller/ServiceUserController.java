package com.example.uslugicykliczne.controller;


import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.ContactDataProjection;
import com.example.uslugicykliczne.dataTypes.ServiceUserDTO;
import com.example.uslugicykliczne.dataTypes.ServiceUserProjection;
import com.example.uslugicykliczne.entity.ContactDataEntity;
import com.example.uslugicykliczne.entity.EmailEntity;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import com.example.uslugicykliczne.repo.ServiceUserRepo;
import com.example.uslugicykliczne.services.ServiceUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

//    @GetMapping("/get/{id}")
//    public ResponseEntity<CustomerEntity> getCustomer (@PathVariable Integer id ){
//        Optional<CustomerEntity> soughtEntity = serviceUserRepo.findById(id);
//        if(soughtEntity.isPresent()){
//            return ResponseEntity.ok(soughtEntity.get());
//        }
//        else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable Integer id ){
        serviceUserRepo.deleteById(id);
        return ResponseEntity.ok().body("Customer was deleted");

    }


    @PostMapping("/insertBody")
    public ResponseEntity<String> insert(@Valid @RequestBody() ServiceUserDTO serviceUserDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return serviceUserService.insertNewServiceUserEntity(serviceUserDTO);
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id,@Valid @RequestBody() ServiceUserDTO serviceUserDTO, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return serviceUserService.updateCustomerEntity(id, serviceUserDTO);
    }

}
