package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CustomerEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends ListCrudRepository<CustomerEntity,Integer> {
    List<CustomerEntity> findCustomerEntitiesByEmailOrPhoneNumber(String email, String phoneNumber);

}
