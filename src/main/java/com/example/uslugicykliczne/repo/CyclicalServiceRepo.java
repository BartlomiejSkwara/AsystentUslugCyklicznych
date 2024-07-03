package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface CyclicalServiceRepo extends ListCrudRepository<CyclicalServiceEntity,Integer> {
}
