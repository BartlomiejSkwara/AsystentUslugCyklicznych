package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.entity.CustomerEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CustomerRepo extends ListCrudRepository<CustomerEntity,Integer> {
    List<CustomerEntity> findCustomerEntitiesByEmailOrPhoneNumber(String email, String phoneNumber);
    List<CyclicalServiceProjection.CustomerProjection> findProjectionBy();

}
