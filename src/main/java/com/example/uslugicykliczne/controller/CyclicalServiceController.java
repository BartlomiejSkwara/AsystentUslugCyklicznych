//package com.example.uslugicykliczne.controller;
//
//import com.example.uslugicykliczne.ValidationUtility;
//import com.example.uslugicykliczne.dataTypes.CyclicalServiceDto;
//import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
//import com.example.uslugicykliczne.repo.CyclicalServiceRepo;
//import com.example.uslugicykliczne.services.CyclicalServiceService;
//import jakarta.validation.Valid;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/cyclicalservice")
//public class CyclicalServiceController {
//
//    private final CyclicalServiceRepo cyclicalServiceRepo;
//    private final CyclicalServiceService cyclicalServiceService;
//    private final ValidationUtility validationUtility;
//
//
//    CyclicalServiceController(CyclicalServiceRepo cyclicalServiceRepo, CyclicalServiceService cyclicalServiceService, ValidationUtility validationUtility, TaskScheduler taskScheduler){
//        this.cyclicalServiceRepo = cyclicalServiceRepo;
//        this.cyclicalServiceService = cyclicalServiceService;
//        this.validationUtility = validationUtility;
//    }
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
//    @GetMapping("/getAll")
//    public List<CyclicalServiceProjection> getAllCustomers(){
//        return cyclicalServiceRepo.findProjectionsBy();
//    }
//
//
//    @PostMapping("/insertBody")
//    public ResponseEntity<String> insert(@Validated @RequestBody CyclicalServiceDto cyclicalServiceDto, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
//        }
//        return cyclicalServiceService.insertNewCyclicalServiceEntity(cyclicalServiceDto);
//    }
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
//}
