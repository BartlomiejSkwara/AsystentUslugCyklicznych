package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.projections.AccountDataProjection;
import com.example.uslugicykliczne.entity.AccountDataEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountDataRepo extends ListCrudRepository<AccountDataEntity,Integer> {
    Optional<AccountDataEntity> findByUsername(String username);
    List<AccountDataProjection> findAllBy();
}
