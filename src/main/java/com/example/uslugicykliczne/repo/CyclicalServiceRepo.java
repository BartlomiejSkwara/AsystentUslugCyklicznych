package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface CyclicalServiceRepo extends ListCrudRepository<CyclicalServiceEntity,Integer> {

    //Optional<CyclicalServiceEntity> findByNextRenewalNotNullAndNextRenewal_Min();
}
