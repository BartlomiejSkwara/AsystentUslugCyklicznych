package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.BusinessProjection;
import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.dataTypes.ServiceUserProjection;
import com.example.uslugicykliczne.entity.BusinessEntity;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessRepo extends ListCrudRepository<BusinessEntity,Integer> {

    default List<BusinessProjection> findBusinessesWithProjectedContactData(){
        List<BusinessEntity> all = findBJoinedEmailBy();
        if (all != null && !all.isEmpty()){
            return  findBJoinedPhoneBy(all);
        }
        return null;
    }
    @Query("select distinct b from BusinessEntity b JOIN FETCH b.contactData cd JOIN FETCH cd.emails ")
    List<BusinessEntity> findBJoinedEmailBy();
    @Query("select distinct b from BusinessEntity b JOIN FETCH b.contactData cd JOIN FETCH cd.phoneNumbers where b in (:oldB)")
    List<BusinessProjection> findBJoinedPhoneBy(List<BusinessEntity> oldB);



    default Optional<BusinessEntity> findBusinessWithContactDataById(int id){
        Optional<BusinessEntity> entity = findBSingleJoinedEmailBy(id);
        if (entity != null && entity.isPresent()){
            return  findBSingleJoinedPhoneBy(entity.get());
        }
        return Optional.empty();
    }


    @Query("select su from BusinessEntity su JOIN FETCH su.contactData cd JOIN FETCH cd.emails WHERE su.idBusiness = :id")
    Optional<BusinessEntity> findBSingleJoinedEmailBy(int id);

    @Query("select su from BusinessEntity su JOIN FETCH su.contactData cd JOIN FETCH cd.phoneNumbers where su = :oldB")
    Optional<BusinessEntity> findBSingleJoinedPhoneBy(BusinessEntity oldB);


}
