//package com.example.uslugicykliczne.services;
//
//import com.example.uslugicykliczne.dataTypes.CustomerDto;
//import com.example.uslugicykliczne.repo.CustomerRepo;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class CustomerService {
//    private final CustomerRepo customerRepo;
//
//    public CustomerService(CustomerRepo customerRepo) {
//        this.customerRepo = customerRepo;
//    }
//
//    public ResponseEntity<String> updateCustomerEntity(Integer id,CustomerDto customerDto){
//        Optional<CustomerEntity> customerEntity = customerRepo.findById(id);
//        if (customerEntity.isEmpty())
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't edit nonexistent customer !!!");
//        convertCustomerDTOtoEntity(customerEntity.get(), customerDto);
//        customerRepo.save(customerEntity.get());
//        return ResponseEntity.ok("Successfully updated the user");
//    }
//    public ResponseEntity<String> insertNewCustomerEntity(CustomerDto customerDto){
//        List<CustomerEntity> duplicateUniques = customerRepo.findCustomerEntitiesByEmailOrPhoneNumber(customerDto.getEmail(), customerDto.getPhoneNumber());
//        if(duplicateUniques.isEmpty()){
//            customerRepo.save(convertCustomerDTOtoEntity(new CustomerEntity(),customerDto));
//            return ResponseEntity.ok("Successfully added the user");
//        }else {
//            StringBuilder error = new StringBuilder("There can't be Customers with duplicate:");
//
//            HashSet<String> duplicates = new HashSet<>();
//            duplicateUniques.forEach(customerEntity ->
//                {
//                    if(customerEntity.getEmail().equals(customerDto.getEmail()))
//                        duplicates.add("[email]");
//                    if(customerEntity.getPhoneNumber().equals(customerDto.getPhoneNumber()))
//                        duplicates.add("[phone number]");
//                }
//
//            );
//
//            duplicates.forEach(s ->
//                        error.append(s)
//                    );
//
//            return ResponseEntity.status(409).body(error.toString());
//        }
//    }
//    public CustomerEntity convertCustomerDTOtoEntity(CustomerEntity customerEntity, CustomerDto dto){
//        customerEntity.setEmail(dto.getEmail());
//        customerEntity.setName(dto.getName());
//        customerEntity.setSurname(dto.getSurname());
//        customerEntity.setPhoneNumber(dto.getPhoneNumber());
//        return customerEntity;
//    }
//}
