package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.CyclicalServiceDto;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
import com.example.uslugicykliczne.services.CyclicalServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cyclicalservice")
public class CyclicalServiceController {

    private final CyclicalServiceRepo cyclicalServiceRepo;
    private final ValidationUtility validationUtility;
    private final CyclicalServiceService cyclicalServiceService;

    public CyclicalServiceController(CyclicalServiceRepo cyclicalServiceRepo, ValidationUtility validationUtility, CyclicalServiceService cyclicalServiceService) {
        this.cyclicalServiceRepo = cyclicalServiceRepo;
        this.validationUtility = validationUtility;
        this.cyclicalServiceService = cyclicalServiceService;
    }

//
//
//
//    @GetMapping("/renew/{id}")
//    public ResponseEntity<String> renew (@PathVariable Integer id ){
//        return cyclicalServiceService.renewCyclicalService(id);
//
//    }
//
//
    @GetMapping("/getAll")
    public List<CyclicalServiceEntity> getAllCustomers(){
        return cyclicalServiceRepo.findAll();
    }

    @PostMapping("/insertBody")
    public ResponseEntity<String> insert(@Validated @RequestBody CyclicalServiceDto cyclicalServiceDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return cyclicalServiceService.insertNewCyclicalServiceEntity(cyclicalServiceDto);
    }

//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> delete (@PathVariable Integer id ){
//        cyclicalServiceRepo.deleteById(id);
//        return ResponseEntity.ok().body("Cyclical service was deleted");
//
//    }
//
//
//    @GetMapping("/get/{id}")
//    public ResponseEntity<CyclicalServiceEntity> getCyclicalService (@PathVariable Integer id ){
//        Optional<CyclicalServiceEntity> soughtEntity = cyclicalServiceRepo.findById(id);
//        if(soughtEntity.isPresent()){
//            return ResponseEntity.ok(soughtEntity.get());
//        }
//        else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    ///TODO obsłuż sytuację gdzie user podaje ujemny okres odnowienia
//    @PostMapping("/update/{id}")
//    public ResponseEntity<String> update(@PathVariable Integer id, @Valid @RequestBody() CyclicalServiceDto cyclicalServiceDto, BindingResult bindingResult ){
//        if(bindingResult.hasErrors()){
//            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
//        }
//        return cyclicalServiceService.updateCyclicalServiceEntity(id, cyclicalServiceDto);
//    }
}
