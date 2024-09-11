package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.BusinessDTO;
import com.example.uslugicykliczne.dataTypes.BusinessProjection;
import com.example.uslugicykliczne.entity.BusinessEntity;
import com.example.uslugicykliczne.repo.BusinessRepo;
import com.example.uslugicykliczne.services.BusinessService;
import jakarta.validation.Valid;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/get/{id}")
    public ResponseEntity<BusinessProjection> getDysponent (@PathVariable Integer id ){
        Optional<BusinessEntity> soughtEntity = businessRepo.findBusinessWithContactDataById(id);
        if(soughtEntity.isPresent()){
            ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
            BusinessProjection bp = pf.createProjection(BusinessProjection.class, soughtEntity.get());
            return ResponseEntity.ok(bp);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable Integer id ){
        businessService.deleteBusiness(id);
        return ResponseEntity.ok().body("Business was deleted");

    }


    @PostMapping("/insertBody")
    public ResponseEntity<String> insert(@Valid @RequestBody() BusinessDTO businessDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return businessService.insertNewBusinessEntity(businessDTO);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @Valid @RequestBody() BusinessDTO businessDTO, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return businessService.updateBusinessEntity(id, businessDTO);
    }

}
