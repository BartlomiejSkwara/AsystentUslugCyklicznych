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


    //    default
//    @Query("select cs,be.idBusiness, be.name,cs.idCyclicalService, cs.renewalMessageSent, cs.price, cs.oneTime, cs.description, cs.agreementNumber  " +
//            "from CyclicalServiceEntity cs join fetch cs.business be")
//    List<CyclicalServiceProjection> findBy();
//
//    List<CyclicalServiceProjection> findProjectionsBy();
//    @Query("SELECT e FROM CyclicalServiceEntity e WHERE e.nextRenewal < :dateLimit AND  e.renewalMessageSent = false ORDER BY e.nextRenewal")
//    List<CyclicalServiceEntity> findAllDatesBeforeWithNoMessageSent(@Param("dateLimit") LocalDateTime dateLimit);
//    @Query("SELECT e FROM CyclicalServiceEntity e WHERE e.nextRenewal < :dateLimit AND  e.renewalMessageSent = false ORDER BY e.nextRenewal ASC LIMIT 1")
//    Optional<CyclicalServiceEntity> findFirstDateBeforeWithNoMessageSent(@Param("dateLimit") LocalDateTime dateLimit);
//
//    @Query("SELECT e FROM CyclicalServiceEntity e WHERE e.renewalMessageSent = false ORDER BY e.nextRenewal ASC LIMIT 1")
//    Optional<CyclicalServiceEntity> findFirstServiceWithNoMessageSent();
//
//
//    @Query("SELECT e FROM CyclicalServiceEntity e WHERE e.nextRenewal < :dateLimit ORDER BY e.nextRenewal ASC LIMIT 1")
//    Optional<CyclicalServiceEntity> findFirstDateBefore(@Param("dateLimit") LocalDateTime dateLimit);
//
//    @Query("SELECT  e FROM CyclicalServiceEntity e ORDER BY e.nextRenewal ASC LIMIT  1")
//    Optional<CyclicalServiceEntity> findServiceWithNextRenewalDate();
}


