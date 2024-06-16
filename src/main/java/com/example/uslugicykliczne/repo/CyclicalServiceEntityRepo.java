package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface CyclicalServiceEntityRepo extends ListCrudRepository<CyclicalServiceEntity,Integer> {
}
