package com.example.uslugicykliczne.repo;

import com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection;
import com.example.uslugicykliczne.dataTypes.StatusEnum;
import com.example.uslugicykliczne.entity.CertificateEntity;
import com.example.uslugicykliczne.entity.CyclicalServiceEntity;
import com.example.uslugicykliczne.entity.StatusChangeEntity;
import com.example.uslugicykliczne.entity.StatusTypeEntity;
import com.example.uslugicykliczne.utility.TimeUtility;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

interface CustomServiceRepo{
    List<CyclicalServiceProjection> customFindCyclicalProjectionsInNextNDays(int nDays);
    List<CyclicalServiceProjection> customFindCyclicalProjectionsWithCancelRequest();

    Optional<CyclicalServiceEntity> customFindNameOfAccountAssignedToService(int serviceID);
    void customUpdateAwaitingRenewal();

}
class  CustomServiceRepoImpl implements CustomServiceRepo{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CyclicalServiceProjection> customFindCyclicalProjectionsWithCancelRequest() {
        Query query = entityManager.createQuery(
                "select  new com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection(" +
                        "cs.idCyclicalService,cs.price,cs.oneTime,cs.agreementNumber,cs.description," +
                        "cs.business.id,cs.business.name, cs.serviceUser.id, cs.serviceUser.name, cs.serviceUser.surname," +
                        "ce.idCertificate, ce.certificateSerialNumber, ce.validFrom,ce.validTo,ce.cardType,ce.cardNumber,ce.nameInOrganisation, cs.statusBitmap)" +
                        "from com.example.uslugicykliczne.entity.CertificateEntity ce left join  ce.cyclicalServiceEntity cs " +
                        "where floor(mod(cs.statusBitmap/:status,:status)) = 1 "
        );
        query.setParameter("status", StatusEnum.MARKED_FOR_CANCEL.getMaskValue());
        return query.getResultList();
    }
    @Override
    public List<CyclicalServiceProjection> customFindCyclicalProjectionsInNextNDays(int nDays) {
        Query query = entityManager.createQuery(
                "select  new com.example.uslugicykliczne.dataTypes.CyclicalServiceProjection(" +
                        "cs.idCyclicalService,cs.price,cs.oneTime,cs.agreementNumber,cs.description," +
                        "cs.business.id,cs.business.name, cs.serviceUser.id, cs.serviceUser.name, cs.serviceUser.surname," +
                        "ce.idCertificate, ce.certificateSerialNumber, ce.validFrom,ce.validTo,ce.cardType,ce.cardNumber,ce.nameInOrganisation, cs.statusBitmap)" +
                        "from com.example.uslugicykliczne.entity.CertificateEntity ce left join  ce.cyclicalServiceEntity cs " +
                        "where ce.renewed = false  and ce.validTo<:desiredTime"
        );
        query.setParameter("desiredTime", LocalDateTime.now().plusDays(nDays));
        return query.getResultList();
    }

    @Override
    public Optional<CyclicalServiceEntity> customFindNameOfAccountAssignedToService(int serviceID) {


        EntityGraph entityGraph = entityManager.createEntityGraph(CyclicalServiceEntity.class);
        entityGraph.addAttributeNodes("statusBitmap", "assignedAccountDataEntity");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
        CyclicalServiceEntity cyclicalService = entityManager.find(CyclicalServiceEntity.class, serviceID, properties);

        if (cyclicalService==null)
            return Optional.empty();
        return Optional.of(cyclicalService);
//        Query query = entityManager.createQuery(
//                "select cse from CyclicalServiceEntity cse " +
//                        "left join cse.assignedAccountDataEntity aa " +
//                        "where cse.idCyclicalService=:serviceID "
//        );
//        query.setParameter("serviceID",serviceID);
//
//        var answer = query.getResultList();
//        return (CyclicalServiceEntity) answer.get(0);
    }

    @Override
    @Transactional
    public void customUpdateAwaitingRenewal() {
        Query disableSafeUpdate = entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 0");
        disableSafeUpdate.executeUpdate();

        Query query = entityManager.createQuery(
                "   select distinct ce.cyclicalServiceEntity.idCyclicalService from CertificateEntity ce" +
                        "   where ce.validTo<:desiredTime"
        );
        query.setParameter("desiredTime", LocalDateTime.now().plusDays(60));

        List<Integer> idList =  (List<Integer>) query.getResultList();


        query = entityManager.createQuery(
                "update  CyclicalServiceEntity cse " +
                        "set cse.statusBitmap = 1 " +
                        "where cse.idCyclicalService in (:idList) "+
                        " and cse.statusBitmap = 256"
        );

        query.setParameter("idList",idList);
        query.executeUpdate();


        Query enableSafeUpdate = entityManager.createNativeQuery("SET SQL_SAFE_UPDATES = 1");
        enableSafeUpdate.executeUpdate();

        LocalDateTime now = LocalDateTime.now();
        for(int id:idList){
            StatusChangeEntity statusChangeEntity = new StatusChangeEntity();
            statusChangeEntity.setChangeDate(now);
            statusChangeEntity.setStatusTypeEntity(entityManager.getReference(StatusTypeEntity.class,StatusEnum.AWAITING_RENEWAL.getMaskValue()));
            statusChangeEntity.setComment("Dokonano automatycznego zmianu stanu na \""+StatusEnum.AWAITING_RENEWAL.getStatusName()+"\"");
            statusChangeEntity.setCyclicalService(entityManager.getReference(CyclicalServiceEntity.class,id));
            entityManager.persist(statusChangeEntity);
        }




    }
}
