package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.ServiceUserProjection;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ServiceUserRepo  extends ListCrudRepository<ServiceUserEntity,Integer> {


    default List<ServiceUserProjection> findUsersWithProjectedContactData(){
        List<ServiceUserEntity> all = findJoinedEmailBy();
        if (all != null && !all.isEmpty()){
            return  findJoinedPhoneBy(all);
        }
        return null;
    }

    @Query("select distinct su from ServiceUserEntity su JOIN FETCH su.contactData cd JOIN FETCH cd.emails ")
    List<ServiceUserEntity> findJoinedEmailBy();

    @Query("select distinct su from ServiceUserEntity su JOIN FETCH su.contactData cd JOIN FETCH cd.phoneNumbers where su in (:oldSu)")
    List<ServiceUserProjection> findJoinedPhoneBy(List<ServiceUserEntity> oldSu);




    default Optional<ServiceUserEntity> findUserWithContactDataById(int id){
        Optional<ServiceUserEntity> entity = findSingleJoinedEmailBy(id);
        if (entity != null && entity.isPresent()){
            return  findSingleJoinedPhoneBy(entity.get());
        }
        return Optional.empty();
    }
    @Query("select su from ServiceUserEntity su JOIN FETCH su.contactData cd JOIN FETCH cd.emails WHERE su.idServiceUser = :id")
    Optional<ServiceUserEntity> findSingleJoinedEmailBy(int id);

    @Query("select su from ServiceUserEntity su JOIN FETCH su.contactData cd JOIN FETCH cd.phoneNumbers where su = :oldSu")
    Optional<ServiceUserEntity> findSingleJoinedPhoneBy(ServiceUserEntity oldSu);
    //@EntityGraph(value = "ServiceUserEntity.eagerlyFetchSUEmails", type = EntityGraph.EntityGraphType.LOAD)


    //List<ServiceUserEntity> find
    List<ServiceUserProjection> findProjectionsBy();
    //@Query("SELECT  su from  ServiceUserEntity su JOIN FETCH su.contactData cd JOIN fetch cd.emails")
    //List<ServiceUserProjection> findProjections2By();

//    @Query("SELECT su FROM ServiceUserEntity su JOIN FETCH su.contactData cd LEFT JOIN FETCH cd.phoneNumbers LEFT JOIN FETCH cd.emails")
//    List<ServiceUserProjection> findProjectionsQuery();
    //List<ServiceUserProjection> findAll();
//    List<CustomerEntity> findCustomerEntitiesByEmailOrPhoneNumber(String email, String phoneNumber);
//    List<CyclicalServiceProjection.CustomerProjection> findProjectionBy();
}
