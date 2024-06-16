package com.example.uslugicykliczne.controller;

import com.example.uslugicykliczne.entity.CustomerEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.repo.CyclicalServiceEntityRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service")
public class CyclicalServiceController {

    private final CyclicalServiceEntityRepo cyclicalServiceEntityRepo;


    CyclicalServiceController(CyclicalServiceEntityRepo cyclicalServiceEntityRepo){
        this.cyclicalServiceEntityRepo = cyclicalServiceEntityRepo;
    }
    @GetMapping("/getAll")
    public List<CyclicalServiceEntity> getAllCustomers(){
        return cyclicalServiceEntityRepo.findAll();
    }


    @PostMapping("/insert")
    public CyclicalServiceEntity insert(@RequestBody CyclicalServiceEntity cyclicalServiceEntity){
        return cyclicalServiceEntityRepo.save(cyclicalServiceEntity);
    }
}
