package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.BusinessDTO;
import com.example.uslugicykliczne.dataTypes.BusinessProjection;
import com.example.uslugicykliczne.repo.BusinessRepo;
import com.example.uslugicykliczne.services.BusinessService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business")
public class BusinessController
{

    private final BusinessRepo businessRepo;
    private final ValidationUtility validationUtility;
    private final BusinessService businessService;

    public BusinessController(BusinessRepo businessRepo, ValidationUtility validationUtility, BusinessService businessService) {
        this.businessRepo = businessRepo;
        this.validationUtility = validationUtility;
        this.businessService = businessService;
    }



    @GetMapping("/getAll")
    public List<BusinessProjection> getAllBusinesses(){
        return businessRepo.findBusinessesWithProjectedContactData();
    }
//
//    @GetMapping("/get/{id}")
//    public ResponseEntity<DysponentEntity> getDysponent (@PathVariable Integer id ){
//        Optional<DysponentEntity> soughtEntity = dysponentRepo.findById(id);
//        if(soughtEntity.isPresent()){
//            return ResponseEntity.ok(soughtEntity.get());
//        }
//        else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> delete (@PathVariable Integer id ){
//        dysponentRepo.deleteById(id);
//        return ResponseEntity.ok().body("Dysponent was deleted");
//
//    }
//
//
    @PostMapping("/insertBody")
    public ResponseEntity<String> insert(@Valid @RequestBody() BusinessDTO businessDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return businessService.insertNewBusinessEntity(businessDTO);
    }
//
//    @PostMapping("/update/{id}")
//    public ResponseEntity<String> update(@PathVariable Integer id, @Valid @RequestBody() DysponentDto dysponentDto, BindingResult bindingResult ){
//        if(bindingResult.hasErrors()){
//            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
//        }
//        return dysponentService.updateDysponentEntity(id, dysponentDto);
//    }
//
}
