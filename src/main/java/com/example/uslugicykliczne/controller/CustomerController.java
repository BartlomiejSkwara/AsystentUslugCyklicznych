package com.example.uslugicykliczne.controller;


import com.example.uslugicykliczne.ValidationUtility;
import com.example.uslugicykliczne.dataTypes.CustomerDto;
import com.example.uslugicykliczne.entity.CustomerEntity;
import com.example.uslugicykliczne.repo.CustomerRepo;
import com.example.uslugicykliczne.services.CustomerService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerRepo customerRepo;
    private final CustomerService customerService;
    private final ValidationUtility validationUtility;

    CustomerController(CustomerRepo customerRepo, CustomerService customerService, ValidationUtility validationUtility){
        this.customerRepo = customerRepo;
        this.customerService = customerService;
        this.validationUtility = validationUtility;
    }





    @GetMapping("/getAll")
    public List<CustomerEntity> getAllCustomers(){
        return customerRepo.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CustomerEntity> getCustomer (@PathVariable Integer id ){
        Optional<CustomerEntity> soughtEntity = customerRepo.findById(id);
        if(soughtEntity.isPresent()){
            return ResponseEntity.ok(soughtEntity.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete (@PathVariable Integer id ){
        customerRepo.deleteById(id);
        return ResponseEntity.ok().body("Customer was deleted");

    }


    @PostMapping("/insertBody")
    public ResponseEntity<String> insert(@Valid @RequestBody() CustomerDto customerDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return customerService.insertNewCustomerEntity(customerDto);
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id,@Valid @RequestBody() CustomerDto customerDto, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(validationUtility.validationMessagesToJSON(bindingResult));
        }
        return customerService.updateCustomerEntity(id, customerDto);
    }

}
