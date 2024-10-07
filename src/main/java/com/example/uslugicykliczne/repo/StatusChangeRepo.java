package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.BusinessEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.entity.StatusChangeEntity;
import org.springframework.data.repository.ListCrudRepository;
public interface StatusChangeRepo extends ListCrudRepository<StatusChangeEntity,Integer>, CustomStatusChangeRepo {
    void deleteByCyclicalService(CyclicalServiceEntity cyclicalServiceEntity);

}
