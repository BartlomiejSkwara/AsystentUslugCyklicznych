package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.BusinessProjection;
import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.dataTypes.ServiceUserProjection;
import com.example.uslugicykliczne.entity.BusinessEntity;
import com.example.uslugicykliczne.entity.ServiceUserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

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
    @Query("select distinct b from BusinessEntity b JOIN FETCH b.contactData cd JOIN FETCH cd.phoneNumbers where b in (:oldb)")
    List<BusinessProjection> findBJoinedPhoneBy(List<BusinessEntity> oldb);


//    List<BusinessEntity> findBusinessEntitiesByEmailOrPhoneNumberOrMfnSerialNumber(String email, String phoneNumber, String mfnSerialNumber);
//    List<CyclicalServiceProjection.DysponentProjection> findProjectionsBy();

}
