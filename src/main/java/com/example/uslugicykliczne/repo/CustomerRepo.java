package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CustomerEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface CustomerRepo extends ListCrudRepository<CustomerEntity,Integer> {


}
