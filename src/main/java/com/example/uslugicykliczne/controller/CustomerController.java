package com.example.uslugicykliczne.controller;


import com.example.uslugicykliczne.entity.CustomerEntity;
import com.example.uslugicykliczne.repo.CustomerRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerRepo customerRepo;

    CustomerController(CustomerRepo customerRepo){
        this.customerRepo = customerRepo;
    }


    @GetMapping("/getAll")
    public List<CustomerEntity> getAllCustomers(){
        return customerRepo.findAll();
    }


    @PostMapping("/insert")
    public CustomerEntity insert(@RequestBody CustomerEntity customerEntity){
        return customerRepo.save(customerEntity);
    }


}
