package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface CyclicalServiceRepo extends ListCrudRepository<CyclicalServiceEntity,Integer>,CustomServiceRepo {
//    @Query("select cse from CyclicalServiceEntity cse join fetch  cse.assignedAccountDataEntity where cse.idCyclicalService = :id")
//    CyclicalServiceEntity  findServiceByIdAndFetchAccountData(Integer id);
}


