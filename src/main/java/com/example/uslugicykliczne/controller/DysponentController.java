package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.dataTypes.DysponentDto;
import com.example.uslugicykliczne.dataTypes.DysponentDto;
import com.example.uslugicykliczne.entity.DysponentEntity;
import com.example.uslugicykliczne.repo.DysponentRepo;
import com.example.uslugicykliczne.services.DysponentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dysponent")
public class DysponentController
{

    private final DysponentRepo dysponentRepo;
    private final ValidationUtility validationUtility;
    private final DysponentService dysponentService;

    public DysponentController(DysponentRepo dysponentRepo, ValidationUtility validationUtility, DysponentService dysponentService) {
        this.dysponentRepo = dysponentRepo;
        this.validationUtility = validationUtility;
        this.dysponentService = dysponentService;
    }

    @GetMapping("/getAll")
    public List<CyclicalServiceProjection.DysponentProjection> getAllDysponents(){
        return dysponentRepo.findProjectionsBy();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<DysponentEntity> getDysponent (@PathVariable Integer id ){
        Optional<DysponentEntity> soughtEntity = dysponentRepo.findById(id);
        if(soughtEntity.isPresent()){
            return ResponseEntity.ok(soughtEntity.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable Integer id ){
        dysponentRepo.deleteById(id);
        return ResponseEntity.ok().body("Dysponent was deleted");

    }


    @PostMapping("/insertBody")
    public ResponseEntity<String> insert(@Valid @RequestBody() DysponentDto DysponentDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return dysponentService.insertNewDysponentEntity(DysponentDto);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @Valid @RequestBody() DysponentDto dysponentDto, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return dysponentService.updateDysponentEntity(id, dysponentDto);
    }

}
