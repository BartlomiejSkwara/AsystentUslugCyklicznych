package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.entity.CertificateEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

public interface CustomBusinessRepo {
//    List<BusinessProjection> findBusinessByRelatedUserID(int userId);
}

class  CustomBusinessRepoImpl implements CustomBusinessRepo{
    @PersistenceContext
    private EntityManager entityManager;


//    @Override
//    public List<BusinessProjection> findBusinessByRelatedUserID(int userId) {
//        Query query = entityManager.createQuery(
//                "select  new com.example.uslugicykliczne.dataTypes.BusinessRecordProjection(" +
//                        "bu.id,bu.name,bu.adres,bu.regon,bu.nip,bu.comments," +
//                        "bu.contactData.emails, bu.contactData.phoneNumbers)" +
//                        "from com.example.uslugicykliczne.entity.CyclicalServiceEntity cs left join  cs.business bu " +
//                        "where cs.serviceUser.id=:userId"
//        );
//
//        query.setParameter("userId", userId);
//
//        return query.getResultList();
//    }
}