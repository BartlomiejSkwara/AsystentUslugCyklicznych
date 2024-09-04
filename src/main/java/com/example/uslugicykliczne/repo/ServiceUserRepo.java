package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.dataTypes.ServiceUserProjection;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ServiceUserRepo  extends ListCrudRepository<ServiceUserEntity,Integer> {

    List<ServiceUserProjection> findProjectionsBy();


//    List<CustomerEntity> findCustomerEntitiesByEmailOrPhoneNumber(String email, String phoneNumber);
//    List<CyclicalServiceProjection.CustomerProjection> findProjectionBy();
}
