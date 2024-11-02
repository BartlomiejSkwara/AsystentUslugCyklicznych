package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.AddressEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface AddressRepo extends ListCrudRepository<AddressEntity,Integer> {
}
